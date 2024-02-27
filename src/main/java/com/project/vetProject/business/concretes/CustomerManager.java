package com.project.vetProject.business.concretes;

import com.project.vetProject.business.abstracts.ICustomerService;
import com.project.vetProject.core.config.modelMapper.IModelMapperService;
import com.project.vetProject.core.exception.DataAlreadyExistException;
import com.project.vetProject.core.exception.NotFoundException;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.Msg;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dao.CustomerRepo;
import com.project.vetProject.dto.response.customer.CustomerResponse;
import com.project.vetProject.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerManager implements ICustomerService {
    private final CustomerRepo customerRepo;
    private final IModelMapperService modelMapperService;

    @Autowired
    public CustomerManager(CustomerRepo customerRepo, IModelMapperService modelMapperService) {
        this.customerRepo = customerRepo;
        this.modelMapperService = modelMapperService;
    }
    @Override
    public ResultData<CustomerResponse> save(Customer customer) {
        List<Customer> getByNamePhoneMail = this.findByNameAndMailAndPhone(customer.getName(), customer.getMail(), customer.getPhone());
        if (!getByNamePhoneMail.isEmpty()){
            throw new DataAlreadyExistException(Msg.getEntityForMsg(Customer.class));
        }
        return ResultHelper.created(this.modelMapperService.forResponse().map(this.customerRepo.save(customer), CustomerResponse.class));
    }

    @Override
    public Customer get(int id) {
        return this.customerRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Page<Customer> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.customerRepo.findAll(pageable);
    }

    @Override
    public Customer update(Customer customer) {
        this.get(customer.getId());
        return this.customerRepo.save(customer);
    }

    @Override
    public ResultData<List<Customer>> findByName(String name) {

        List<Customer> customerList = this.customerRepo.findByName(name);
        if (customerList.isEmpty()){
            return ResultHelper.FoundByName();
        }
        return ResultHelper.success(customerList);
    }

    @Override
    public List<Customer> findByNameAndMailAndPhone(String name, String mail, String phone) {
        return this.customerRepo.findByNameAndMailAndPhone(name, mail, phone);
    }

    @Override
    public boolean delete(int id) {
        Customer customer = this.get(id);
        this.customerRepo.delete(customer);
        return true;
    }
}
