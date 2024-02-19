package com.project.vetProject.business.abstracts;

import com.project.vetProject.entity.Vaccine;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IVaccineService {
    Vaccine save(Vaccine vaccine);
    Vaccine get(int id);
    Page<Vaccine> cursor(int page, int pageSize);
    List<Vaccine> findByAnimalId(int id);
    List<Vaccine> findByDate(LocalDate entryDate, LocalDate exitDate);
    Vaccine update(Vaccine vaccine);
    boolean delete(int id);
}
