package com.project.vetProject.dao;

import com.project.vetProject.entity.AvailableDate;
import com.project.vetProject.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailableDateRepo extends JpaRepository<AvailableDate, Integer> {
    List<AvailableDate> findByDateAndDoctor(LocalDate availableDate, Doctor doctor);
}
