package com.project.vetProject.business.abstracts;

import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.request.doctor.DoctorSaveRequest;
import com.project.vetProject.dto.response.doctor.DoctorResponse;
import com.project.vetProject.entity.Doctor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IDoctorService {
    ResultData<DoctorResponse> save(DoctorSaveRequest doctorSaveRequest);
    Doctor get(int id);
    Page<Doctor> cursor(int page, int pageSize);
    Doctor update(Doctor doctor);
    boolean delete(int id);
    List<Doctor> findByIdAndAvailableDateDate(int id, LocalDate localDate);
    List<Doctor> findByNameAndMailAndPhone(String name, String mail, String phone);
}
