package com.project.vetProject.dto.request.animal;

import com.project.vetProject.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalSaveRequest {
    private String name;
    private String species;
    private String breed;
    private String gender;
    private String color;
    private LocalDate birthday;
    private Customer customer;
}
