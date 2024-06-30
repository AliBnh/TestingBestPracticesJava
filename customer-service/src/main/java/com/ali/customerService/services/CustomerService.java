package com.ali.customerService.services;




import com.ali.customerService.dto.CustomerDTO;
import com.ali.customerService.exceptions.CustomerNotFoundException;
import com.ali.customerService.exceptions.EmailAlreadyExistException;

import java.util.List;

public interface CustomerService {
    CustomerDTO saveNewCustomer(CustomerDTO customerDTO) throws EmailAlreadyExistException;
    List<CustomerDTO> getAllCustomers();
    CustomerDTO findCustomerById(Long id) throws CustomerNotFoundException;
    List<CustomerDTO> searchCustomers(String keyword);
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO)throws CustomerNotFoundException;
    void deleteCustomer(Long id)throws CustomerNotFoundException;
}