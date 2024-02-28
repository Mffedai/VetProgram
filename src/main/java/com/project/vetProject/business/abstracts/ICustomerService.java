package com.project.vetProject.business.abstracts;

import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.customer.CustomerSaveRequest;
import com.project.vetProject.dto.request.customer.CustomerUpdateRequest;
import com.project.vetProject.dto.response.customer.CustomerResponse;
import com.project.vetProject.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICustomerService {
    ResultData<CustomerResponse> save(CustomerSaveRequest customerSaveRequest);
    Customer get(int id);
    ResultData<CursorResponse<CustomerResponse>> cursor(int page, int pageSize);
    ResultData<CustomerResponse> update(CustomerUpdateRequest customerUpdateRequest);
    ResultData<List<CustomerResponse>> findByName(String name);
    List<Customer> findByNameAndMailAndPhone(String name, String mail, String phone);
    boolean delete(int id);
}
