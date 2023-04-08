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
            .updatedDate(LocalDateTime.now())
            .version(1)
            .build();

        Customer customer2 = Customer.builder()
            .customerId(UUID.randomUUID())
            .customerName("Damigean Prabuseanu")
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .version(1)
            .build();

        Customer customer3 = Customer.builder()
            .customerId(UUID.randomUUID())
            .customerName("Sandu Ciorba")
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .version(1)
            .build();

        customerMap.put(customer1.getCustomerId(), customer1);
        customerMap.put(customer2.getCustomerId(), customer2);
        customerMap.put(customer3.getCustomerId(), customer3);
    }

    @Override
    public List<Customer> listCustomers() {
        return new ArrayList<>(this.customerMap.values());
    }

    @Override
    public Customer getCustomer(UUID id) {
        return this.customerMap.get(id);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        Customer customerBuilder = Customer.builder()
            .customerId(UUID.randomUUID())
            .version(customer.getVersion())
            .customerName(customer.getCustomerName())
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

        this.customerMap.put(customerBuilder.getCustomerId(), customerBuilder);

        return customerBuilder;
    }

    @Override
    public Customer updateCustomer(UUID id, Customer customer) {
        Customer customerToUpdate = this.customerMap.get(id);

        customerToUpdate.setCustomerName(customer.getCustomerName());
        customerToUpdate.setVersion(customerToUpdate.getVersion()+1);
        customerToUpdate.setUpdatedDate(LocalDateTime.now());
        this.customerMap.put(customerToUpdate.getCustomerId(), customerToUpdate);

        return customerToUpdate;
    }

    @Override
    public void deleteById(UUID id) {
        this.customerMap.remove(id);
    }

}
