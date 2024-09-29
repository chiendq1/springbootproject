package org.example.springbootproject.service;

import org.example.springbootproject.dto.ContractDto;
import org.example.springbootproject.entity.Contract;
import org.example.springbootproject.entity.Room;
import org.example.springbootproject.entity.User;
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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        response.put("contracts", listContracts.getContent());
        response.put("currentPage", listContracts.getNumber());
        response.put("totalItems", listContracts.getTotalElements());
        response.put("totalPages", listContracts.getTotalPages());

        return response;
    }

    public Map<String, Object> getContractData(int contractId) {
        Map<String, Object> response = new HashMap<>();
        Contract contract = contractRepository.getContractById(contractId);
        response.put("landlordName", contract.getRoom().getLandlord().getFullName());
        response.put("landlordPhone", contract.getRoom().getLandlord().getPhoneNumber());
        response.put("monthlyRent", contract.getTypeText());
        response.put("roomCode", contract.getRoom().getRoomCode());
        response.put("tenants", contract.getRoom().getRoomsTenants().stream().toList());
        response.put("securityDeposit", contract.getDeposit());
        response.put("contractDuration", contract.getStartDate() + " ~ " + contract.getEndDate());
        response.put("services", contract.getRoom().getUtilities().stream().toList());

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
        contract.setContractName(request.getContractName());
        contract.setType(request.getType());
        contract.setStartDate(Date.valueOf(request.getStartDate()));
        contract.setEndDate(Date.valueOf(request.getEndDate()));
        contract.setDeposit(request.getDeposit());
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

    public Map<String, Object> terminateContract(int contractId) {
        Map<String, Object> response = new HashMap<>();
        Contract contract = contractRepository.getContractById(contractId);
        contract.setStatus(Constants.CONTRACT_STATUS_TERMINATED);
        contractRepository.save(contract);
        response.put("contract", contract);

        return response;
    }

    public boolean checkContractExistByField(String field, String value) {
        switch (field) {
            case "contractName":
                return contractRepository.existsContractByContractName(value);
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
            if (isCheckTenant) {
                result = contract.getRoom().getRoomsTenants().stream().anyMatch(tenant -> tenant.getUsername().equals(userName));
            }
            result = contract.getRoom().getLandlord().getUsername().equals(userName);
        }

        return result;
    }
}
