package com.spring_test.spring_test_one.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring_test.spring_test_one.entity.Fire;

@Repository
public interface FireRepository extends JpaRepository<Fire,Long> {
    
}
