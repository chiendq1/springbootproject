package org.example.springbootproject.service;

import org.example.springbootproject.entity.Utility;
import org.example.springbootproject.repository.UtilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UtilityService {

    @Autowired
    private UtilityRepository utilityRepository;

    public Map<String, Object> getListUtilities() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Utility> utilities = utilityRepository.findAll();
        map.put("utilities", utilities);

        return map;
    }
}
