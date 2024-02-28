package com.project.vetProject.business.concretes;

import com.project.vetProject.business.abstracts.IAnimalService;
import com.project.vetProject.business.abstracts.ICustomerService;
import com.project.vetProject.core.config.modelMapper.IModelMapperService;
import com.project.vetProject.core.exception.DataAlreadyExistException;
import com.project.vetProject.core.exception.NotFoundException;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.config.ConvertEntityToResponse;
import com.project.vetProject.core.utilies.Msg;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dao.AnimalRepo;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.animal.AnimalSaveRequest;
import com.project.vetProject.dto.request.animal.AnimalUpdateRequest;
import com.project.vetProject.dto.response.animal.AnimalResponse;
import com.project.vetProject.entity.Animal;
import com.project.vetProject.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalManager implements IAnimalService {
    private final ConvertEntityToResponse<Animal, AnimalResponse> convert;
    private final AnimalRepo animalRepo;
    private final IModelMapperService modelMapperService;
    private final ICustomerService customerService;

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
    public ResultData<CursorResponse<AnimalResponse>> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Animal> animalPage = this.animalRepo.findAll(pageable);
        Page<AnimalResponse> animalResponsePage = animalPage.map(animal -> this.modelMapperService.forResponse().map(animal, AnimalResponse.class));
        return ResultHelper.cursor(animalResponsePage);
    }

    @Override
    public ResultData<List<AnimalResponse>> findByName(String name) {
        List<Animal> animalList = this.animalRepo.findByName(name);
        List<AnimalResponse> animalResponseList = this.convert.convertToResponseList(animalList, AnimalResponse.class);
        return ResultHelper.success(animalResponseList);
    }

    @Override
    public ResultData<List<AnimalResponse>> findByCustomerId(int id) {
        List<Animal> animalList = this.animalRepo.findByCustomerId(id);
        List<AnimalResponse> animalResponseList = this.convert.convertToResponseList(animalList, AnimalResponse.class);
        return ResultHelper.success(animalResponseList);
    }

    @Override
    public List<Animal> findByNameAndSpeciesAndBreedAndGender(String name, String species, String breed, String gender) {
        return this.animalRepo.findByNameAndSpeciesAndBreedAndGender(name, species, breed, gender);
    }

    @Override
    public ResultData<AnimalResponse> update(AnimalUpdateRequest animalUpdateRequest) {
        this.get(animalUpdateRequest.getId());
        Animal updateAnimal = this.modelMapperService.forRequest().map(animalUpdateRequest, Animal.class);
        this.animalRepo.save(updateAnimal);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateAnimal, AnimalResponse.class));
    }

    @Override
    public boolean delete(int id) {
        Animal animal = this.get(id);
        this.animalRepo.delete(animal);
        return true;
    }
}
