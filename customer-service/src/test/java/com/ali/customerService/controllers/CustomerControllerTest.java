package com.ali.customerService.controllers;

import com.ali.customerService.dto.CustomerDTO;
import com.ali.customerService.exceptions.CustomerNotFoundException;
import com.ali.customerService.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @MockBean
    private CustomerService customerService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    List<CustomerDTO> customers;

    @BeforeEach
    void setUp() {
        this.customers = List.of(
                CustomerDTO.builder().id(1L).firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build() ,
                CustomerDTO.builder().id(2L).firstName("Imane").lastName("Tawil").email("imane@gmail.com").build(),
                CustomerDTO.builder().id(3L).firstName("yasmine").lastName("Ibrahimi").email("yasmine@gmail.com").build()
        );
    }

    @Test
    void shouldGetAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(customers);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers)));
    }

    @Test
    void shouldGetCustomerById() throws Exception {
        Long id = 1L;
        when(customerService.findCustomerById(id)).thenReturn(customers.get(0));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers.get(0))));
    }

    @Test
    void shouldNotGetCustomerByInvalidId() throws Exception {
        Long id = 9L;
        when(customerService.findCustomerById(id)).thenThrow(CustomerNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void searchCustomers() throws Exception {
        String keyword="m";
        when(customerService.getAllCustomers()).thenReturn(customers);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers?keyword="+keyword))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers)));
    }

    @Test
    void shouldSaveCustomer() throws Exception {
        CustomerDTO customerDTO= customers.get(0);
        String expected = """
                {
                  "id":1, "firstName":"Mohamed", "lastName":"Youssfi", "email":"med@gmail.com"
                }
                """;
        when(customerService.saveNewCustomer(Mockito.any())).thenReturn(customers.get(0));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Long customerId=1L;
        CustomerDTO customerDTO= customers.get(0);
        when(customerService.updateCustomer(Mockito.eq(customerId),Mockito.any())).thenReturn(customers.get(0));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customerDTO)));
    }
    @Test
    void shouldDeleteCustomer() throws Exception {
        Long customerId=1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers/{id}",customerId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}