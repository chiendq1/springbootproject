package org.example.springbootproject.service;

import org.apache.tomcat.util.bcel.Const;
import org.example.springbootproject.dto.BillDto;
import org.example.springbootproject.dto.BillServiceDto;
import org.example.springbootproject.dto.ContractDto;
import org.example.springbootproject.entity.*;
import org.example.springbootproject.mapper.BillMapper;
import org.example.springbootproject.payload.request.CreateBillDetailsRequest;
import org.example.springbootproject.payload.request.CreateBillRequest;
import org.example.springbootproject.payload.request.GetBillListRequest;
import org.example.springbootproject.payload.request.GetContractListRequest;
import org.example.springbootproject.repository.*;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UtilityRepository utilityRepository;

    @Autowired
    private BillMapper billMapper;
    @Autowired
    private ContractRepository contractRepository;

    public Map<String, Object> getListBills(GetBillListRequest request, String currentUserName) {
        User currentUser = userRepository.findUserByUsername(currentUserName);
        boolean isAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("ADMIN"));
        boolean isLandLord = currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("LANDLORD"));
        boolean isTenant = currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("TENANT"));

        Pageable paging = PageRequest.of(request.getPageNo(), Constants.PAGE_SIZE, Sort.by(request.getSortBy()).descending());

        Page<Bill> listBills = billRepository.getListBills(
                currentUser.getId(),
                isAdmin,
                isLandLord,
                isTenant,
                request.getSearchValue(),
                request.getMonth(),
                request.getRoomId(),
                request.getStatus(),
                paging
        );

        List<BillDto> bills = listBills.stream()
                .map(bill -> billMapper.toBillDto(bill))
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("bills", bills);
        response.put("currentPage", listBills.getNumber());
        response.put("totalItems", listBills.getTotalElements());
        response.put("totalPages", listBills.getTotalPages());

        return response;
    }

    @Transactional
    public Map<String, Object> createBill(CreateBillRequest request) {
        Map<String, Object> response = new HashMap<>();
        Date currentDate = Date.valueOf(LocalDate.now());
        Room room = roomRepository.getRoomByRoomId(request.getRoomId());
        Set<Utility> utilities = utilityRepository.findAllByIdIn(
                request.getDetails().stream().map(CreateBillDetailsRequest::getUtilityId).collect(Collectors.toSet())
        );
        if (room != null && room.getContracts().stream().anyMatch(contract -> contract.getStatus() == Constants.CONTRACT_STATUS_ACTIVE)) {
            Bill bill = new Bill();
            bill.setBillCode(request.getBillCode());
            bill.setRoom(room);
            bill.setMonth(currentDate);
            bill.setCreateAt(currentDate);
            bill.setPaymentStatus(Constants.BILL_STATUS_UNPAID);
            bill.setBillDetails(request.getDetails().stream().map(details -> {
                BillDetails billDetails = new BillDetails();
                billDetails.setBill(bill);
                billDetails.setAmount(details.getAmount());
                billDetails.setUtility(
                        utilities.stream()
                                .filter(util -> util.getId() == details.getUtilityId()) // Use equals for object comparison
                                .findFirst()
                                .orElse(null)
                );

                return billDetails;
            }).collect(Collectors.toList()));
            billRepository.save(bill);
            response.put("bill", bill);

            return response;
        }

        return null;
    }

    public Map<String, Object> updateBill(int id, int status) {
        Map<String, Object> response = new HashMap<>();
        Bill bill = billRepository.findById(id);
        if(bill.getPaymentStatus() == Constants.BILL_STATUS_PAID) return null;
        bill.setPaymentStatus(status);
        billRepository.save(bill);
        response.put("bill", bill);

        return response;
    }

    public  Map<String, List<Bill>> handleCheckBillDaily() {
        Map<String, List<Bill>> response = new HashMap<>();
        response.put("listUnpaidBills", billRepository.getListBillOverDaysUnpaid(Constants.BILL_STATUS_UNPAID, Constants.OVERDUE_BILL_DATE));

        return response;
    }

    public Map<String, Object> getBillPdfData(Bill bill) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        Map<String, Object> response = new HashMap<>();
        boolean existedOtherBill = billRepository.checkExistEarlierBillByRoomIdAndMonth(bill.getRoom().getRoomId(), bill.getId(), bill.getMonth());
        Contract contract = contractRepository.getContractsByStatusAndRoom_RoomId(Constants.CONTRACT_STATUS_ACTIVE, bill.getRoom().getRoomId());
        float rentPrice = existedOtherBill ? 0 : getBillRentPrice(bill, contract);
        float totalServicePrice = 0;
        List<BillServiceDto> listServices = new ArrayList<>();

        for (BillDetails billDetails : bill.getBillDetails()) {
            BillServiceDto serviceDto = new BillServiceDto();
            float servicePriceValue = billDetails.getAmount() * billDetails.getUtility().getUnitPrice();
            String servicePrice = decimalFormat.format(servicePriceValue);
            Float unitPrice = contract.getContractDetails().stream()
                    .filter(contractDetails -> contractDetails.getUtility().getId() == billDetails.getUtility().getId())
                    .map(ContractDetails::getUnitPrice)
                    .findFirst()
                    .orElse(null);
            // Set the properties of the DTO
            serviceDto.setEnName(billDetails.getUtility().getEnName());
            serviceDto.setUnitPrice(decimalFormat.format(unitPrice));
            serviceDto.setAmount(billDetails.getAmount());
            serviceDto.setUnit(billDetails.getUtility().getUnit());
            serviceDto.setPrice(servicePrice);

            // Add the service to the list
            listServices.add(serviceDto);

            // Accumulate the total service price
            totalServicePrice += servicePriceValue;
        }
        response.put("date", dateFormat.format(bill.getMonth()));
        response.put("billCode", bill.getBillCode());
        response.put("roomCode", bill.getRoom().getRoomCode());
        response.put("tenants", bill.getRoom().getRoomsTenants().stream().toList());
        response.put("services", listServices);
        response.put("rentPrice", decimalFormat.format(rentPrice));
        response.put("totalPrice", decimalFormat.format(totalServicePrice + rentPrice));

        return response;
    }

    public float getBillRentPrice(Bill bill, Contract contract) {
        float rentPrice = bill.getRoom().getRentPrice();
        if (contract.getStatus() == Constants.CONTRACT_STATUS_ACTIVE) {
            int contractStartDate = contract.getStartDate().toLocalDate().getMonth().getValue();
            int currentDate = LocalDate.now().getMonth().getValue();
            int monthsDifference = currentDate - contractStartDate;

            switch (contract.getType()) {
                case Constants.CONTRACT_TYPE_MONTHLY:
                    return rentPrice;

                case Constants.CONTRACT_TYPE_QUARTER:
                    if (monthsDifference % 3 == 0) {
                        return rentPrice * 3;
                    }
                    break;

                case Constants.CONTRACT_TYPE_HALF_YEAR:
                    if (monthsDifference % 6 == 0) {
                        return rentPrice * 6;
                    }
                    break;

                case Constants.CONTRACT_TYPE_YEARLY:
                    if (monthsDifference % 12 == 0) {
                        return rentPrice * 12;
                    }
                    break;

                default:
                    return 0;
            }
        }


        return 0;
    }

    public boolean checkBillCodeExist(String billCode) {
        return billRepository.checkBillCodeExist(billCode);
    }

    public boolean checkBillRelatedTo(int billId, String userName, boolean isCheckTenant) {
        Bill bill = billRepository.findById(billId);
        boolean result = false;
        if (bill != null) {
            if (isCheckTenant) {
                result = bill.getRoom().getRoomsTenants().stream().anyMatch(tenant -> tenant.getUsername().equals(userName));
            }
            result = bill.getRoom().getLandlord().getUsername().equals(userName);
        }

        return result;
    }

    public Map<String, Object> getBillDetails(int billId, boolean isGetDto) {
        Map<String, Object> response = new HashMap<>();
        Bill bill = billRepository.findById(billId);
        response.put("bill", isGetDto ? billMapper.toBillDto(bill) : bill);

        return response;
    }
}
