package com.ali.customerService.controllers;


import com.ali.customerService.dto.CustomerDTO;
import com.ali.customerService.services.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CustomerController {
    private CustomerService customerService;

    @GetMapping("/customers")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers();
    }
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomerById(@PathVariable Long id){
        return customerService.findCustomerById(id);
    }
    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(@RequestParam String keyword){
        return customerService.searchCustomers(keyword);
    }
    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO saveCustomer(@RequestBody @Valid CustomerDTO customerDTO){
        return customerService.saveNewCustomer(customerDTO);
    }
    @PutMapping("/customers/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id,@RequestBody CustomerDTO customerDTO){
        return customerService.updateCustomer(id,customerDTO);
    }
    @DeleteMapping("/customers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
    }
}
