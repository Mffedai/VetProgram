package com.project.vetProject.dto.request.availableDate;

import com.project.vetProject.entity.Doctor;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDateSaveRequest {
    @NotNull(message = "Müsait gün boş olamaz")
    private LocalDate date;
    private Doctor doctor;
}
