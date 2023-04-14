package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.model.CustomerDTO;
import guru.springframework.spring6restmvc.mapper.CustomerMapper;
import guru.springframework.spring6restmvc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        return CustomerService.super.listCustomers();
    }

    @Override
    public Optional<CustomerDTO> getCustomer(UUID id) {
        return CustomerService.super.getCustomer(id);
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        return CustomerService.super.saveCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(UUID id, CustomerDTO customer) {
        return CustomerService.super.updateCustomer(id, customer);
    }

    @Override
    public void deleteById(UUID id) {
        //TODO later
    }

    @Override
    public void patchCustomerById(UUID beerId, CustomerDTO customer) {
        //TODO later
    }
}
