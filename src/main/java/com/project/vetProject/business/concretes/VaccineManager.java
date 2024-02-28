package com.project.vetProject.business.concretes;

import com.project.vetProject.business.abstracts.IAnimalService;
import com.project.vetProject.business.abstracts.IVaccineService;
import com.project.vetProject.core.config.ConvertEntityToResponse;
import com.project.vetProject.core.config.modelMapper.IModelMapperService;
import com.project.vetProject.core.exception.DataAlreadyExistException;
import com.project.vetProject.core.exception.NotFoundException;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.Msg;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dao.VaccineRepo;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.vaccine.VaccineSaveRequest;
import com.project.vetProject.dto.request.vaccine.VaccineUpdateRequest;
import com.project.vetProject.dto.response.vaccine.VaccineResponse;
import com.project.vetProject.entity.Animal;
import com.project.vetProject.entity.Vaccine;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VaccineManager implements IVaccineService {
    private final VaccineRepo vaccineRepo;
    private final IModelMapperService modelMapperService;
    private final IAnimalService animalService;
    private final ConvertEntityToResponse<Vaccine, VaccineResponse> convert;
    @Override
    public ResultData<VaccineResponse> save(VaccineSaveRequest vaccineSaveRequest) {

        List<Vaccine> existVaccines = this.findByCodeAndName(vaccineSaveRequest.getCode(), vaccineSaveRequest.getName());
        if (!existVaccines.isEmpty() && existVaccines.get(0).getProtectionFnshDate().isAfter(LocalDate.now())){
            return ResultHelper.error("Aynı koda sahip aşının bitiş tarihi bitmemiş! ");
        }
        if (!existVaccines.isEmpty()){
            throw new DataAlreadyExistException(Msg.getEntityForMsg(Vaccine.class));
        }
        Animal animal = this.animalService.get(vaccineSaveRequest.getAnimalId());
        vaccineSaveRequest.setAnimalId(null);

        Vaccine saveVaccine = this.modelMapperService.forRequest().map(vaccineSaveRequest, Vaccine.class);
        saveVaccine.setAnimal(animal);

        return ResultHelper.created(this.modelMapperService.forResponse().map(this.vaccineRepo.save(saveVaccine), VaccineResponse.class));
    }

    @Override
    public Vaccine get(int id) {
        return this.vaccineRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public ResultData<CursorResponse<VaccineResponse>> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Vaccine> vaccinePage = this.vaccineRepo.findAll(pageable);
        Page<VaccineResponse> vaccineResponsePage = vaccinePage.map(vaccine -> this.modelMapperService.forResponse().map(vaccine, VaccineResponse.class));
        return ResultHelper.cursor(vaccineResponsePage);
    }

    @Override
    public ResultData<List<VaccineResponse>> findByAnimalId(int id) {
        List<Vaccine> vaccineList = this.vaccineRepo.findByAnimalId(id);
        List<VaccineResponse> vaccineResponseList = this.convert.convertToResponseList(vaccineList, VaccineResponse.class);
        return ResultHelper.success(vaccineResponseList);
    }

    @Override
    public ResultData<List<VaccineResponse>> findByDate(LocalDate entryDate, LocalDate exitDate) {
        List<Vaccine> vaccineList = this.vaccineRepo.findByprotectionFnshDateBetween(entryDate, exitDate);
        List<VaccineResponse> vaccineResponseList = this.convert.convertToResponseList(vaccineList, VaccineResponse.class);
        return ResultHelper.success(vaccineResponseList);
    }

    @Override
    public List<Vaccine> findByCodeAndName(String code, String name) {
        return this.vaccineRepo.findByCodeAndName(code,name);
    }

    @Override
    public ResultData<VaccineResponse> update(VaccineUpdateRequest vaccineUpdateRequest) {
        this.get(vaccineUpdateRequest.getId());
        Vaccine updateVaccine = this.modelMapperService.forRequest().map(vaccineUpdateRequest, Vaccine.class);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateVaccine, VaccineResponse.class));
    }

    @Override
    public boolean delete(int id) {
        Vaccine vaccine = this.get(id);
        this.vaccineRepo.delete(vaccine);
        return true;
    }
}
