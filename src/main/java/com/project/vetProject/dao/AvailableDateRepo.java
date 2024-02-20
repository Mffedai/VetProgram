package com.project.vetProject.dao;

import com.project.vetProject.entity.AvailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableDateRepo extends JpaRepository<AvailableDate, Integer> {
}
