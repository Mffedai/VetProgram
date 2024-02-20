package com.project.vetProject.business.concretes;

import com.project.vetProject.business.abstracts.IVaccineService;
import com.project.vetProject.core.exception.NotFoundException;
import com.project.vetProject.core.utilies.Msg;
import com.project.vetProject.dao.VaccineRepo;
import com.project.vetProject.entity.Vaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VaccineManager implements IVaccineService {
    private final VaccineRepo vaccineRepo;

    public VaccineManager(VaccineRepo vaccineRepo) {
        this.vaccineRepo = vaccineRepo;
    }

    @Override
    public Vaccine save(Vaccine vaccine) {
        return this.vaccineRepo.save(vaccine);
    }

    @Override
    public Vaccine get(int id) {
        return this.vaccineRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Page<Vaccine> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.vaccineRepo.findAll(pageable);
    }

    @Override
    public List<Vaccine> findByAnimalId(int id) {
        return this.vaccineRepo.findByAnimalId(id);
    }

    @Override
    public List<Vaccine> findByDate(LocalDate entryDate, LocalDate exitDate) {
        return this.vaccineRepo.findByprotectionFnshDateBetween(entryDate, exitDate);
    }

    @Override
    public List<Vaccine> findByCodeAndName(String code, String name) {
        return this.vaccineRepo.findByCodeAndName(code,name);
    }

    @Override
    public Vaccine update(Vaccine vaccine) {
        this.get(vaccine.getId());
        return this.save(vaccine);
    }

    @Override
    public boolean delete(int id) {
        Vaccine vaccine = this.get(id);
        this.vaccineRepo.delete(vaccine);
        return true;
    }
}
