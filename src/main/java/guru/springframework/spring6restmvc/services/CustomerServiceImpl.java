package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        customerMap = new HashMap<>();

        Customer customer1 = Customer.builder()
            .customerId(UUID.randomUUID())
            .customerName("Gogu Freciparu")
            .createdDate(LocalDateTime.now())
            .version("v1")
            .build();

        Customer customer2 = Customer.builder()
            .customerId(UUID.randomUUID())
            .customerName("Damigean Prabuseanu")
            .createdDate(LocalDateTime.now())
            .version("v1")
            .build();

        Customer customer3 = Customer.builder()
            .customerId(UUID.randomUUID())
            .customerName("Sandu Ciorba")
            .createdDate(LocalDateTime.now())
            .version("v1")
            .build();

        customerMap.put(customer1.getCustomerId(), customer1);
        customerMap.put(customer2.getCustomerId(), customer2);
        customerMap.put(customer3.getCustomerId(), customer3);
    }

    public List<Customer> listCustomers() {
        return new ArrayList<>(this.customerMap.values());
    }

    public Customer getCustomer(UUID id) {
        return this.customerMap.get(id);
    }

}
