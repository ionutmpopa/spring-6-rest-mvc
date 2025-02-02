package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.model.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        customerMap = new HashMap<>();

        CustomerDTO customer1 = CustomerDTO.builder()
            .id(UUID.randomUUID())
            .customerName("Gogu Freciparu")
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .version(1)
            .build();

        CustomerDTO customer2 = CustomerDTO.builder()
            .id(UUID.randomUUID())
            .customerName("Damigean Prabuseanu")
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .version(1)
            .build();

        CustomerDTO customer3 = CustomerDTO.builder()
            .id(UUID.randomUUID())
            .customerName("Sandu Ciorba")
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .version(1)
            .build();

        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return new ArrayList<>(this.customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomer(UUID id) {
        return Optional.of(this.customerMap.get(id));
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        CustomerDTO customerBuilder = CustomerDTO.builder()
            .id(UUID.randomUUID())
            .version(customer.getVersion())
            .customerName(customer.getCustomerName())
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

        this.customerMap.put(customerBuilder.getId(), customerBuilder);

        return customerBuilder;
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customer) {
        CustomerDTO customerToUpdate = this.customerMap.get(id);

        customerToUpdate.setCustomerName(customer.getCustomerName());
        customerToUpdate.setVersion(customerToUpdate.getVersion()+1);
        customerToUpdate.setUpdatedDate(LocalDateTime.now());
        this.customerMap.put(customerToUpdate.getId(), customerToUpdate);

        return Optional.of(customerToUpdate);
    }

    @Override
    public boolean deleteById(UUID id) {
        this.customerMap.remove(id);
        return true;
    }

    @Override
    public boolean patchCustomerById(UUID id, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(id);

        if (StringUtils.hasText(customer.getCustomerName())){
            existing.setCustomerName(customer.getCustomerName());
        }
        return true;
    }

}
