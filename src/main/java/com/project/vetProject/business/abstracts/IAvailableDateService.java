package com.project.vetProject.business.abstracts;

import com.project.vetProject.entity.AvailableDate;
import org.springframework.data.domain.Page;


public interface IAvailableDateService {
    AvailableDate save(AvailableDate availableDate);
    AvailableDate get(int id);
    Page<AvailableDate> cursor(int page, int pageSize);
    AvailableDate update(AvailableDate availableDate);
    boolean delete(int id);
}
