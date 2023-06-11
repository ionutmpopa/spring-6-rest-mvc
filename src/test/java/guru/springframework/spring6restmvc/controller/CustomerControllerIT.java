package guru.springframework.spring6restmvc.controller;


import guru.springframework.spring6restmvc.controller.model.CustomerDTO;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.exception.NotFoundException;
import guru.springframework.spring6restmvc.repository.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void getCustomerById() {

        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId()).getBody();

        Assertions.assertThat(customerDTO.getId()).isNotNull();
    }

    @Test
    void getCustomerById_notFound() {
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(UUID.randomUUID()));
    }

    @Test
    void testListCustomers() {
        List<CustomerDTO> dtos = customerController.getCustomers().getBody();

        assertThat(dtos).hasSize(3);
    }

    @Rollback
    @Transactional
    @Test
    void saveNewCustomer() {
        ResponseEntity<Void> responseEntity = customerController.addCustomer(CustomerDTO.builder()
            .customerName("Sandu Ciorba")
            .build());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String[] locationUUID = Objects.requireNonNull(responseEntity.getHeaders().getLocation()).getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Customer customer = customerRepository.findById(savedUUID).get();

        assertThat(customer).isNotNull();
        assertThat(customer.getCreatedDate()).isNotNull();

    }

    @Rollback
    @Transactional
    @Test
    void updateCustomerById() {

        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO customerDTO = CustomerDTO.builder()
            .customerName("Sandu Ciorba")
            .build();

        ResponseEntity<CustomerDTO> responseEntity = customerController.updateCustomerById(customer.getId(), customerDTO);

        String[] locationUUID = Objects.requireNonNull(responseEntity.getHeaders().getLocation()).getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(customer.getId()).isEqualTo(Objects.requireNonNull(savedUUID));
        assertThat(customer.getUpdatedDate()).isNotNull();
    }

    @Test
    void updateCustomerById_notFound() {

        CustomerDTO customerDTO = CustomerDTO.builder()
            .customerName("Sandu Ciorba")
            .build();

        assertThrows(NotFoundException.class, () -> customerController.updateCustomerById(UUID.randomUUID(), customerDTO));
    }

    @Rollback
    @Transactional
    @Test
    void patchCustomerById() {

        Customer Customer = customerRepository.findAll().get(0);

        CustomerDTO CustomerDTO = guru.springframework.spring6restmvc.controller.model.CustomerDTO.builder()
            .customerName("HEHE")
            .build();

        ResponseEntity<Void> responseEntity = customerController.partiallyUpdateCustomerById(Customer.getId(), CustomerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    void patchCustomerById_notFound() {

        CustomerDTO CustomerDTO = guru.springframework.spring6restmvc.controller.model.CustomerDTO.builder()
            .customerName("HEHE")
            .build();

        assertThrows(NotFoundException.class, () -> customerController.partiallyUpdateCustomerById(UUID.randomUUID(), CustomerDTO));
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.getCustomers().getBody();

        assertThat(dtos).isEmpty();
    }

    @Rollback
    @Transactional
    @Test
    void deleteCustomerById() {
        Customer customer = customerRepository.findAll().get(0);

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(3);

        ResponseEntity<Void> responseEntity = customerController.deleteCustomerById(customer.getId());

        customerList = customerRepository.findAll();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(customerList).hasSize(2);

    }

    @Test
    void deleteCustomerById_notFound() {
        assertThrows(NotFoundException.class, () -> customerController.deleteCustomerById(UUID.randomUUID()));
    }
}
