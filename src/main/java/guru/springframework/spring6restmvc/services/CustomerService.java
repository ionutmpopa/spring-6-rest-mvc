package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.model.CustomerDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    default List<CustomerDTO> listCustomers() {
        return Collections.emptyList();
    }

    default Optional<CustomerDTO> getCustomer(UUID id) {
        return Optional.of(CustomerDTO.builder().build());
    }

    default CustomerDTO saveCustomer(CustomerDTO customer) {
        return CustomerDTO.builder().build();
    }

    default CustomerDTO updateCustomer(UUID id, CustomerDTO customer) {
        return CustomerDTO.builder().build();
    }

    void deleteById(UUID id);

    void patchCustomerById(UUID beerId, CustomerDTO customer);
}
