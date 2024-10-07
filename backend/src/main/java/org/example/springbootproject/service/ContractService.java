package org.example.springbootproject.service;

import org.example.springbootproject.dto.ContractDto;
import org.example.springbootproject.entity.*;
import org.example.springbootproject.mapper.ContractMapper;
import org.example.springbootproject.payload.request.CreateContractRequest;
import org.example.springbootproject.payload.request.GetContractListRequest;
import org.example.springbootproject.repository.ContractRepository;
import org.example.springbootproject.repository.RoomRepository;
import org.example.springbootproject.repository.UserRepository;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ContractService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ContractRepository contractRepository;

    public Map<String, Object> getListContracts(GetContractListRequest request, String currentUserName) {
        User currentUser = userRepository.findUserByUsername(currentUserName);
        boolean isAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("ADMIN"));
        boolean isLandLord = currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("LANDLORD"));
        boolean isTenant = currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("TENANT"));

        Pageable paging = PageRequest.of(request.getPageNo(), Constants.PAGE_SIZE, Sort.by(request.getSortBy()).descending());

        Page<Contract> listContracts = contractRepository.getListContracts(
                currentUser.getId(),
                isAdmin,
                isLandLord,
                isTenant,
                request.getSearchValue(),
                request.getStartDate(),
                request.getEndDate(),
                request.getRoomId(),
                request.getStatus(),
                request.getTenantId(),
                request.getLandlordId(),
                request.getType(),
                paging
        );

        List<ContractDto> contracts = listContracts.stream()
                .map(contract -> contractMapper.toContractDto(contract))
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("contracts", contracts);
        response.put("currentPage", listContracts.getNumber());
        response.put("totalItems", listContracts.getTotalElements());
        response.put("totalPages", listContracts.getTotalPages());

        return response;
    }

    public Map<String, Object> getContractData(int contractId, float exchangeRate) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        Map<String, Object> response = new HashMap<>();
        Contract contract = contractRepository.getContractById(contractId);
        response.put("landlordName", contract.getRoom().getLandlord().getFullName());
        response.put("landlordPhone", contract.getRoom().getLandlord().getPhoneNumber());
        response.put("monthlyRent", contract.getTypeText());
        response.put("roomCode", contract.getRoom().getRoomCode());
        response.put("tenants", contract.getRoom().getRoomsTenants().stream().toList());
        response.put("securityDeposit", decimalFormat.format(contract.getDeposit() * exchangeRate));
        response.put("contractDuration", contract.getStartDate() + " ~ " + contract.getEndDate());
        response.put("services", contract.getContractDetails().stream().map(contractDetails -> {
            Utility utility = contractDetails.getUtility();
            utility.setUnitPrice(contractDetails.getUnitPrice() * exchangeRate);

            return utility;
        }));

        return response;
    }

    @Transactional
    public Map<String, Object> createContract(CreateContractRequest request) {
        Map<String, Object> response = new HashMap<>();
        Contract contract = new Contract();
        Room room = roomRepository.getRoomByRoomId(request.getRoomId());
        if (!request.getTenants().isEmpty()) {
            if (request.getTenants().size() + room.getRoomsTenants().size() > room.getCapacity()) {
                response.put("tenants", "E-CM-019");

                return response;
            }
            Set<User> users = userRepository.getUsersByIdIn(request.getTenants());
            room.setRoomsTenants(users);
        }
        room.setStatus(Constants.ROOM_STATUS_RENT);
        room.getContracts().forEach(contractChange -> contractChange.setStatus(Constants.CONTRACT_STATUS_TERMINATED));
        contract.setContractName(request.getContractName());
        contract.setType(request.getType());
        contract.setStartDate(Date.valueOf(request.getStartDate()));
        contract.setEndDate(Date.valueOf(request.getEndDate()));
        contract.setDeposit(request.getDeposit());
        contract.setContractDetails(room.getUtilities().stream().map(utility -> {
            ContractDetails contractDetails = new ContractDetails();
            contractDetails.setContract(contract);
            contractDetails.setUtility(utility);
            contractDetails.setUnitPrice(utility.getUnitPrice());

            return contractDetails;
        }).collect(Collectors.toSet()));
        contract.setRoom(room);
        contractRepository.save(contract);
        response.put("contract", contractMapper.toContractDto(contract));

        return response;
    }

    public Map<String, Object> getContractDetails(int contractId) {
        Map<String, Object> response = new HashMap<>();
        Contract contract = contractRepository.getContractById(contractId);
        response.put("contract", contractMapper.toContractDto(contract));

        return response;
    }

    @Transactional
    public Map<String, Object> terminateContract(int contractId) {
        Map<String, Object> response = new HashMap<>();
        Contract contract = contractRepository.getContractById(contractId);
        contract.setStatus(Constants.CONTRACT_STATUS_TERMINATED);
        contract.getRoom().setStatus(Constants.ROOM_STATUS_VACANT);
        contract.getRoom().setRoomsTenants(null);
        contractRepository.save(contract);
        response.put("contract", contract);

        return response;
    }

    @Transactional
    public void changeListContractStatus(List<Contract> listContracts, int status) {
        listContracts.forEach(contract -> {
            if(status == Constants.CONTRACT_STATUS_TERMINATED) {
                terminateContract(contract.getId());
            } else {
                contract.setStatus(status);
            }
            contractRepository.save(contract);
        });
    }

    public Map<String, List<Contract>> handleCheckContractDaily() {
        Map<String, List<Contract>> response = new HashMap<>();
        Date currentDate = Date.valueOf(LocalDate.now());
        Date dateAfter = Date.valueOf(LocalDate.now().plusDays(Constants.DATE_PLUS_CONTRACT));
        response.put("listOverdueContracts", contractRepository.getContractsByEndDateBeforeAndStatusNot(currentDate, Constants.CONTRACT_STATUS_TERMINATED));
        response.put("listRenewedContracts", contractRepository.getContractsByEndDateBetweenAndStatus(currentDate, dateAfter, Constants.CONTRACT_STATUS_ACTIVE));

        return response;
    }

    public boolean checkContractExistByField(String field, String value, String userName) {
        switch (field) {
            case "contractName":
                return contractRepository.existsContractByContractName(value, userName);
        }

        return false;
    }

    public boolean checkContractDateValid(Date date, int roomId) {
        return contractRepository.existsContractByDateWithinRange(date, roomId);
    }

    public boolean checkContractRelatedTo(int contractId, String userName, boolean isCheckTenant) {
        Contract contract = contractRepository.getContractById(contractId);
        boolean result = false;
        if (contract != null) {
            result = contract.getRoom().getLandlord().getUsername().equals(userName);
            if (isCheckTenant) {
                result = contract.getRoom().getRoomsTenants().stream().anyMatch(tenant -> tenant.getUsername().equals(userName));
            }
        }

        return result;
    }
}
