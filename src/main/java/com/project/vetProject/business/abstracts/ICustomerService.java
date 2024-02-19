package com.project.vetProject.business.abstracts;

import com.project.vetProject.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICustomerService {
    Customer save(Customer customer);
    Customer get(int id);
    Page<Customer> cursor(int page, int pageSize);
    Customer update(Customer customer);
    List<Customer> findByName(String name);

    boolean delete(int id);
}
