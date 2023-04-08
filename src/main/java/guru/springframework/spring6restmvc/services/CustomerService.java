package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Customer;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public interface CustomerService {

    default List<Customer> listCustomers() {
        return Collections.emptyList();
    }

    default Customer getCustomer(UUID id) {
        return Customer.builder().build();
    }

    default Customer saveCustomer(Customer customer) {
        return Customer.builder().build();
    }

    default Customer updateCustomer(UUID id, Customer customer) {
        return Customer.builder().build();
    }

}
