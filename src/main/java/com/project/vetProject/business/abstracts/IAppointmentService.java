package com.project.vetProject.business.abstracts;

import com.project.vetProject.entity.Appointment;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService {
    Appointment save(Appointment appointment);
    Appointment get(int id);
    Page<Appointment> cursor(int page, int pageSize);
    Appointment update(Appointment appointment);
    boolean delete(int id);
    List<Appointment> findByDateTime(LocalDateTime localDateTime);
    List<Appointment> findByDoctorIdAndDateTimeBetween(int id, LocalDateTime entryDate, LocalDateTime exitDate);
}
