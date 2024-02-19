package com.project.vetProject.dto.request.animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalUpdateRequest {
    private int id;
    private String name;
    private String species;
    private String breed;
    private String gender;
    private String color;
    private LocalDate birthday;
    private int customerId;
}
