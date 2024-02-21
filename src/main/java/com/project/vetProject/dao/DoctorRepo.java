package com.project.vetProject.dao;

import com.project.vetProject.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Integer> {
    List<Doctor> findByIdAndAvailableDateDate(int id, LocalDate localDate);
    List<Doctor> findByNameAndMailAndPhone(String name, String mail, String phone);
}
