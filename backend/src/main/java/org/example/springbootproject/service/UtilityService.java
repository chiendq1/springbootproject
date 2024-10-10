package org.example.springbootproject.service;

import org.example.springbootproject.entity.Utility;
import org.example.springbootproject.mapper.UtilityMapper;
import org.example.springbootproject.payload.request.CreateUtilityRequest;
import org.example.springbootproject.payload.request.UpdateUtilityRequest;
import org.example.springbootproject.repository.UtilityRepository;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UtilityService {

    @Autowired
    private UtilityRepository utilityRepository;

    @Autowired
    private UtilityMapper utilityMapper;

    public Map<String, Object> getListUtilities(String searchValue, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Utility> utilities = utilityRepository.getListUtilities(searchValue, status);
        map.put("utilities", utilityMapper.toDtoList(utilities));

        return map;
    }

    public Map<String, Object> getUtilityDetails(Integer utilityId) {
        Map<String, Object> response = new HashMap<>();
        Utility utility = utilityRepository.getUtilityById(utilityId);
        response.put("utility", utilityMapper.toDto(utility));

        return response;
    }

    public Map<String, Object> createNewUtility(CreateUtilityRequest request, float exchangeRate) {
        Map<String, Object> response = new HashMap<>();
        Utility utility = new Utility();
        utility.setEnName(request.getName());
        utility.setJaName(request.getName());
        utility.setUnit(request.getUnit());
        utility.setUnitPrice(request.getUnitPrice() / exchangeRate);
        utilityRepository.save(utility);
        response.put("utility", utilityMapper.toDto(utility));

        return response;
    }

    public Map<String, Object> updateUtility(UpdateUtilityRequest request, int id, float exchangeRate) {
        Map<String, Object> response = new HashMap<>();
        Utility utility = utilityRepository.getUtilityById(id);
        utility.setEnName(request.getName());
        utility.setJaName(request.getName());
        utility.setStatus(Constants.ROOM_UTILITY_STATUS_ACTIVE);
        if (utility.getBillDetails().stream().noneMatch(billDetails -> billDetails.getBill().getPaymentStatus() == Constants.BILL_STATUS_UNPAID)) {
            utility.setUnit(request.getUnit());
            utility.setUnitPrice(request.getUnitPrice() / exchangeRate);
        }
        utilityRepository.save(utility);
        response.put("utility", utilityMapper.toDto(utility));

        return response;
    }

    public Map<String, Object> deactivateUtility(Integer utilityId) {
        Map<String, Object> response = new HashMap<>();
        Utility utility = utilityRepository.getUtilityById(utilityId);
        if (utility == null || utility.getBillDetails().stream().anyMatch(billDetails -> billDetails.getBill().getPaymentStatus() == Constants.BILL_STATUS_UNPAID))
            return null;

        utility.setStatus(Constants.ROOM_UTILITY_STATUS_INACTIVE);
        utilityRepository.save(utility);
        response.put("utility", utilityMapper.toDto(utility));
        return response;
    }

    public boolean checkDuplicateName(String fieldValue, int id) {
        return utilityRepository.duplicateName(fieldValue, id, Constants.ROOM_UTILITY_STATUS_INACTIVE);
    }
}
