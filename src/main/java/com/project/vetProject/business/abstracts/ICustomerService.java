package com.project.vetProject.business.abstracts;

import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.response.customer.CustomerResponse;
import com.project.vetProject.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICustomerService {
    ResultData<CustomerResponse> save(Customer customer);
    Customer get(int id);
    Page<Customer> cursor(int page, int pageSize);
    Customer update(Customer customer);
    ResultData<List<Customer>> findByName(String name);
    List<Customer> findByNameAndMailAndPhone(String name, String mail, String phone);
    boolean delete(int id);
}
