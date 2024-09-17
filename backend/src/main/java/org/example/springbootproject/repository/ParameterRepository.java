package org.example.springbootproject.repository;

import org.example.springbootproject.entity.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterRepository extends JpaRepository<Parameter, Long> {
    Parameter findByName(String name);
}
