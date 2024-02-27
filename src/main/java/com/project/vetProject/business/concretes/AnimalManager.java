package com.project.vetProject.business.concretes;

import com.project.vetProject.business.abstracts.IAnimalService;
import com.project.vetProject.business.abstracts.ICustomerService;
import com.project.vetProject.core.config.modelMapper.IModelMapperService;
import com.project.vetProject.core.exception.DataAlreadyExistException;
import com.project.vetProject.core.exception.NotFoundException;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.Msg;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dao.AnimalRepo;
import com.project.vetProject.dto.request.animal.AnimalSaveRequest;
import com.project.vetProject.dto.response.animal.AnimalResponse;
import com.project.vetProject.entity.Animal;
import com.project.vetProject.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalManager implements IAnimalService {

    private final AnimalRepo animalRepo;
    private final IModelMapperService modelMapperService;
    private final ICustomerService customerService;

    public AnimalManager(AnimalRepo animalRepo, IModelMapperService modelMapperService, ICustomerService customerService) {
        this.animalRepo = animalRepo;
        this.modelMapperService = modelMapperService;
        this.customerService = customerService;
    }

    @Override
    public ResultData<AnimalResponse> save(AnimalSaveRequest animalSaveRequest) {
        Customer customer = this.customerService.get(animalSaveRequest.getCustomerId());
        animalSaveRequest.setCustomerId(null);
        Animal saveAnimal = this.modelMapperService.forRequest().map(animalSaveRequest, Animal.class);
        saveAnimal.setCustomer(customer);
        List<Animal> animalList = this.findByNameAndSpeciesAndBreedAndGender(
                animalSaveRequest.getName(),
                animalSaveRequest.getSpecies(),
                animalSaveRequest.getBreed(),
                animalSaveRequest.getGender()
        );
        if (!animalList.isEmpty()){
            throw new DataAlreadyExistException(Msg.getEntityForMsg(Animal.class));
        }
        return ResultHelper.created(this.modelMapperService.forResponse().map(this.animalRepo.save(saveAnimal), AnimalResponse.class));
    }
    @Override
    public Animal get(int id) {
        return this.animalRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Page<Animal> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.animalRepo.findAll(pageable);
    }

    @Override
    public List<Animal> findByName(String name) {
        return this.animalRepo.findByName(name);
    }

    @Override
    public List<Animal> findByCustomerId(int id) {
        return this.animalRepo.findByCustomerId(id);
    }

    @Override
    public List<Animal> findByNameAndSpeciesAndBreedAndGender(String name, String species, String breed, String gender) {
        return this.animalRepo.findByNameAndSpeciesAndBreedAndGender(name, species, breed, gender);
    }

    @Override
    public Animal update(Animal animal) {
        this.get(animal.getId());
        return this.animalRepo.save(animal);
    }

    @Override
    public boolean delete(int id) {
        Animal animal = this.get(id);
        this.animalRepo.delete(animal);
        return true;
    }
}
