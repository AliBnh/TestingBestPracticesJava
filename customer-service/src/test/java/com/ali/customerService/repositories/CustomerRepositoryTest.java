package com.ali.customerService.repositories;

import com.ali.customerService.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        System.out.println("-----------------------------------------------");
        customerRepository.save(Customer.builder()
                .firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build());
        customerRepository.save(Customer.builder()
                .firstName("Ahmed").lastName("Yassine").email("ahmed@gmail.com").build());
        customerRepository.save(Customer.builder()
                .firstName("Hanane").lastName("yamal").email("hanane@gmail.com").build());
        System.out.println("-----------------------------------------------");
    }
    @Test
    void shouldFindCustomersByFirstName(){
        List<Customer> expected = List.of(
                Customer.builder().firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build(),
                Customer.builder().firstName("Ahmed").lastName("Yassine").email("ahmed@gmail.com").build()
        );
        List<Customer> result = customerRepository.findByFirstNameContainingIgnoreCase("m");
        assertThat(result.size()).isEqualTo(expected.size());
        assertThat(expected).usingRecursiveComparison().ignoringFields("id").isEqualTo(result);
    }

    @Test
    void shouldFindCustomersByEmail(){
        String givenEmail="med@gmail.com";
        Customer expected=Customer.builder().firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build();
        Optional<Customer> result = customerRepository.findByEmail(givenEmail);
        assertThat(result).isPresent();
        assertThat(expected).usingRecursiveComparison().ignoringFields("id").isEqualTo(result.get());
    }
    @Test
    void shouldNotFindCustomersByEmail(){
        String givenEmail="xxx@gmail.com";
        Optional<Customer> result = customerRepository.findByEmail(givenEmail);
        assertThat(result).isEmpty();
    }
}