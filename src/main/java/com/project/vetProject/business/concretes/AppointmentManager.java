package com.project.vetProject.business.concretes;

import com.project.vetProject.business.abstracts.IAppointmentService;
import com.project.vetProject.core.exception.NotFoundException;
import com.project.vetProject.core.utilies.Msg;
import com.project.vetProject.dao.AppointmentRepo;
import com.project.vetProject.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentManager implements IAppointmentService {
    private final AppointmentRepo appointmentRepo;

    public AppointmentManager(AppointmentRepo appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    @Override
    public Appointment save(Appointment appointment) {
        return this.appointmentRepo.save(appointment);
    }

    @Override
    public Appointment get(int id) {
        return this.appointmentRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Page<Appointment> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.appointmentRepo.findAll(pageable);
    }

    @Override
    public Appointment update(Appointment appointment) {
        this.get(appointment.getId());
        return this.appointmentRepo.save(appointment);
    }

    @Override
    public boolean delete(int id) {
        Appointment appointment = this.get(id);
        this.appointmentRepo.delete(appointment);
        return true;
    }

    @Override
    public List<Appointment> findByDateTime(LocalDateTime localDateTime) {
        return this.appointmentRepo.findByDateTime(localDateTime);
    }

    @Override
    public List<Appointment> findByDoctorIdAndDateTimeBetween(int id, LocalDateTime entryDate, LocalDateTime exitDate) {
        return this.appointmentRepo.findByDoctorIdAndDateTimeBetween(id, entryDate, exitDate);
    }
}
