package com.project.vetProject.business.abstracts;

import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.appointment.AppointmentSaveRequest;
import com.project.vetProject.dto.request.appointment.AppointmentUpdateRequest;
import com.project.vetProject.dto.response.appointment.AppointmentResponse;
import com.project.vetProject.entity.Appointment;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IAppointmentService{
    ResultData<AppointmentResponse> save(AppointmentSaveRequest appointmentSaveRequest);
    Appointment get(int id);
    ResultData<CursorResponse<AppointmentResponse>> cursor(int page, int pageSize);
    ResultData<AppointmentResponse> update(AppointmentUpdateRequest appointmentUpdateRequest);
    boolean delete(int id);
    List<Appointment> findByDateTime(LocalDateTime localDateTime);
    ResultData<List<AppointmentResponse>> findByDoctorIdAndDateTimeBetween(int id, LocalDate entryDate, LocalDate exitDate);
    ResultData<List<AppointmentResponse>> findByAnimalIdAndDateTimeBetween(int id, LocalDate entryDate, LocalDate exitDate);
    Optional<Appointment> findByValueForValid(LocalDateTime dateTime, Integer doctorId, Integer animalId);
}
