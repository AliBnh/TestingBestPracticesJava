package com.ali.customerService.services;

import com.ali.customerService.dto.CustomerDTO;
import com.ali.customerService.entities.Customer;
import com.ali.customerService.exceptions.CustomerNotFoundException;
import com.ali.customerService.exceptions.EmailAlreadyExistException;
import com.ali.customerService.mapper.CustomerMapper;
import com.ali.customerService.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerServiceImpl underTest;
    @Test
    void shouldSaveNewCustomer() {
        CustomerDTO customerDTO= CustomerDTO.builder()
                .firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        Customer customer= Customer.builder()
                .firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        Customer savedCustomer= Customer.builder()
                .id(1L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        CustomerDTO expected= CustomerDTO.builder()
                .id(1L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.empty());
        when(customerMapper.fromCustomerDTO(customerDTO)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(savedCustomer);
        when(customerMapper.fromCustomer(savedCustomer)).thenReturn(expected);
        CustomerDTO result = underTest.saveNewCustomer(customerDTO);
        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldNotSaveNewCustomerWhenEmailExist() {
        CustomerDTO customerDTO= CustomerDTO.builder()
                .firstName("Ismail").lastName("Matar").email("xxxxx@gmail.com").build();
        Customer customer= Customer.builder()
                .id(5L).firstName("Ismail").lastName("Matar").email("xxxxx@gmail.com").build();
        when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.of(customer));
        assertThatThrownBy(()->underTest.saveNewCustomer(customerDTO))
                .isInstanceOf(EmailAlreadyExistException.class);
    }

    @Test
    void shouldGetAllCustomers() {
        List<Customer> customers = List.of(
                Customer.builder().firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build(),
                Customer.builder().firstName("Ahmed").lastName("Yassine").email("ahmed@gmail.com").build()
        );
        List<CustomerDTO> expected = List.of(
                CustomerDTO.builder().firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build(),
                CustomerDTO.builder().firstName("Ahmed").lastName("Yassine").email("ahmed@gmail.com").build()
        );
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.fromListCustomers(customers)).thenReturn(expected);
        List<CustomerDTO> result = underTest.getAllCustomers();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldFindCustomerById() {
        Long customerId = 1L;
        Customer customer=Customer.builder().id(1L).firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build();
        CustomerDTO expected=CustomerDTO.builder().id(1L).firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.fromCustomer(customer)).thenReturn(expected);
        CustomerDTO result = underTest.findCustomerById(customerId);
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }
    @Test
    void shouldNotFindCustomerById() {
        Long customerId = 8L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThatThrownBy(()->underTest.findCustomerById(customerId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage(null);
    }

    @Test
    void shouldSearchCustomers() {
        String keyword="m";
        List<Customer> customers = List.of(
                Customer.builder().firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build(),
                Customer.builder().firstName("Ahmed").lastName("Yassine").email("ahmed@gmail.com").build()
        );
        List<CustomerDTO> expected = List.of(
                CustomerDTO.builder().firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build(),
                CustomerDTO.builder().firstName("Ahmed").lastName("Yassine").email("ahmed@gmail.com").build()
        );
        when(customerRepository.findByFirstNameContainingIgnoreCase(keyword)).thenReturn(customers);
        when(customerMapper.fromListCustomers(customers)).thenReturn(expected);
        List<CustomerDTO> result = underTest.searchCustomers(keyword);
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void updateCustomer() {
        Long customerId= 6L;
        CustomerDTO customerDTO= CustomerDTO.builder()
                .id(6L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        Customer customer= Customer.builder()
                .id(6L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        Customer updatedCustomer= Customer.builder()
                .id(6L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        CustomerDTO expected= CustomerDTO.builder()
                .id(6L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.fromCustomerDTO(customerDTO)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(updatedCustomer);
        when(customerMapper.fromCustomer(updatedCustomer)).thenReturn(expected);
        CustomerDTO result = underTest.updateCustomer(customerId,customerDTO);
        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldDeleteCustomer() {
        Long customerId =1L;
        Customer customer= Customer.builder()
                .id(6L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        underTest.deleteCustomer(customerId);
        Mockito.verify(customerRepository).deleteById(customerId);
    }
    @Test
    void shouldNotDeleteCustomerIfNotExist() {
        Long customerId =9L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThatThrownBy(()->underTest.deleteCustomer(customerId))
                .isInstanceOf(CustomerNotFoundException.class);
    }
}