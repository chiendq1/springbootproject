package org.example.springbootproject.repository;

import org.example.springbootproject.entity.Utility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UtilityRepository extends JpaRepository<Utility, Integer> {
    Set<Utility> findAllByIdIn(Set<Integer> ids);

    Utility findById(int id);
}
