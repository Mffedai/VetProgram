package com.project.vetProject.business.abstracts;

import com.project.vetProject.entity.Doctor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IDoctorService {
    Doctor save(Doctor doctor);
    Doctor get(int id);
    Page<Doctor> cursor(int page, int pageSize);
    Doctor update(Doctor doctor);
    boolean delete(int id);
    List<Doctor> findByIdAndAvailableDateDate(int id, LocalDate localDate);
}
