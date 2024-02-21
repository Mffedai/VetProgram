package com.project.vetProject.business.abstracts;

import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.request.animal.AnimalSaveRequest;
import com.project.vetProject.dto.response.animal.AnimalResponse;
import com.project.vetProject.entity.Animal;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAnimalService {
    ResultData<AnimalResponse> save(AnimalSaveRequest animalSaveRequest);
    Animal get(int id);
    Page<Animal> cursor(int page, int pageSize);
    List<Animal> findByName(String name);
    List<Animal> findByCustomerId(int id);
    List<Animal> findByNameAndSpeciesAndBreedAndGender(String name,String species,String breed,String gender);
    Animal update(Animal animal);
    boolean delete(int id);
}
