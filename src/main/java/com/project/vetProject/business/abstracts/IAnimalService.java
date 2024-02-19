package com.project.vetProject.business.abstracts;

import com.project.vetProject.entity.Animal;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAnimalService {
    Animal save(Animal animal);
    Animal get(int id);
    Page<Animal> cursor(int page, int pageSize);
    List<Animal> findByName(String name);
    List<Animal> findByCustomerId(int id);
    Animal update(Animal animal);
    boolean delete(int id);
}
