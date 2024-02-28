package com.project.vetProject.business.abstracts;

import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.vaccine.VaccineSaveRequest;
import com.project.vetProject.dto.request.vaccine.VaccineUpdateRequest;
import com.project.vetProject.dto.response.vaccine.VaccineResponse;
import com.project.vetProject.entity.Vaccine;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IVaccineService {
    ResultData<VaccineResponse> save(VaccineSaveRequest vaccineSaveRequest);
    Vaccine get(int id);
    ResultData<CursorResponse<VaccineResponse>> cursor(int page, int pageSize);
    ResultData<List<VaccineResponse>> findByAnimalId(int id);
    ResultData<List<VaccineResponse>> findByDate(LocalDate entryDate, LocalDate exitDate);
    List<Vaccine> findByCodeAndName(String code, String name);
    ResultData<VaccineResponse> update(VaccineUpdateRequest vaccineUpdateRequest);
    boolean delete(int id);
}
