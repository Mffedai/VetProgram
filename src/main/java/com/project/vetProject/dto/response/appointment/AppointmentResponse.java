package com.project.vetProject.dto.response.appointment;

import com.project.vetProject.entity.Animal;
import com.project.vetProject.entity.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {
    private int id;
    private LocalDateTime dateTime;
    private Animal animal;
    private Doctor doctor;
}
