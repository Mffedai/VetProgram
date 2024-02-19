package com.project.vetProject.dao;

import com.project.vetProject.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepo extends JpaRepository<Animal, Integer> {
    List<Animal> findByName(String name);
    List<Animal> findByCustomerId(int id);
}
