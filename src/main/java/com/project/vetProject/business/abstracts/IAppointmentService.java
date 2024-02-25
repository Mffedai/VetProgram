package com.project.vetProject.business.abstracts;

import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.request.appointment.AppointmentSaveRequest;
import com.project.vetProject.dto.response.appointment.AppointmentResponse;
import com.project.vetProject.entity.Appointment;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService{
    ResultData<AppointmentResponse> save(AppointmentSaveRequest appointmentSaveRequest);
    Appointment get(int id);
    Page<Appointment> cursor(int page, int pageSize);
    Appointment update(Appointment appointment);
    boolean delete(int id);
    List<Appointment> findByDateTime(LocalDateTime localDateTime);
    List<Appointment> findByDoctorIdAndDateTimeBetween(int id, LocalDateTime entryDate, LocalDateTime exitDate);
    List<Appointment> findByAnimalIdAndDateTimeBetween(int id, LocalDateTime entryDate, LocalDateTime exitDate);
}
