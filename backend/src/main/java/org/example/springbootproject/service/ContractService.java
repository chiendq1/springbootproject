package org.example.springbootproject.service;

import org.example.springbootproject.dto.ContractDto;
import org.example.springbootproject.dto.RoomDto;
import org.example.springbootproject.entity.Contract;
import org.example.springbootproject.entity.Room;
import org.example.springbootproject.entity.User;
import org.example.springbootproject.mapper.ContractMapper;
import org.example.springbootproject.payload.request.GetContractListRequest;
import org.example.springbootproject.repository.ContractRepository;
import org.example.springbootproject.repository.UserRepository;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContractService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ContractRepository contractRepository;

    public Map<String, Object>  getListContracts(GetContractListRequest request, String currentUserName) {
        User currentUser = userRepository.findUserByUsername(currentUserName);
        boolean isAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("ADMIN"));
        boolean isLandLord = currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("LANDLORD"));
        boolean isTenant = currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("TENANT"));

        Pageable paging = PageRequest.of(request.getPageNo(), Constants.PAGE_SIZE, Sort.by(request.getSortBy()));

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
}
