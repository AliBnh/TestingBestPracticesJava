package com.ali.customerService.mapper;
import com.ali.customerService.dto.CustomerDTO;
import com.ali.customerService.entities.Customer;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CustomerMapperTest {
    private CustomerMapper customerMapper = new CustomerMapper();

    @Test
    void shouldMapCustomerToCustomerDTO() {
        Customer givenCustomer = Customer.builder()
                .id(1L).firstName("Mohamed").lastName("Youssfi").email("med@gmail.com")
                .build();
        CustomerDTO expected = CustomerDTO.builder()
                .id(1L).firstName("Mohamed").lastName("Youssfi").email("med@gmail.com")
                .build();

        CustomerDTO result = customerMapper.fromCustomer(givenCustomer);
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldMapCustomerDTOtoCustomer() {
        CustomerDTO givenCustomerDTO = CustomerDTO.builder()
                .id(1L).firstName("Mohamed").lastName("Youssfi").email("med@gmail.com")
                .build();
        Customer expected = Customer.builder()
                .id(1L).firstName("Mohamed").lastName("Youssfi").email("med@gmail.com")
                .build();
        Customer result = customerMapper.fromCustomerDTO(givenCustomerDTO);
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldMapListOfCustomersToListCustomerDTOs() {
        List<Customer> givenCustomers=List.of(
                Customer.builder().id(1L).firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build() ,
                Customer.builder().id(2L).firstName("Imane").lastName("Ibrahimi").email("ibrahimi@gmail.com").build()
        );
        List<CustomerDTO> expected=List.of(
                CustomerDTO.builder().id(1L).firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build() ,
                CustomerDTO.builder().id(2L).firstName("Imane").lastName("Ibrahimi").email("ibrahimi@gmail.com").build()
        );
        List<CustomerDTO> result = customerMapper.fromListCustomers(givenCustomers);
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }
    
    @Test
    void shouldNotMapNullCustomerToCustomerDTO() {
        assertThatThrownBy(
                ()->customerMapper.fromCustomer(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}