package com.project.vetProject.dto.response.vaccine;

import com.project.vetProject.entity.Animal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineResponse {
    private int id;
    private String name;
    private String code;
    private LocalDate protectionStrtDate;
    private LocalDate protectionFnshDate;
    private Integer animalId;
}
