package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.model.CustomerDTO;
import guru.springframework.spring6restmvc.domain.Customer;
import guru.springframework.spring6restmvc.mapper.CustomerMapper;
import guru.springframework.spring6restmvc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Primary
@Service
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream()
            .map(customerMapper::customerToCustomerDto)
            .toList();
    }

    @Override
    public Optional<CustomerDTO> getCustomer(UUID id) {
        return customerRepository.findById(id).map(customerMapper::customerToCustomerDto);
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        customer.setCreatedDate(LocalDateTime.now());
        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customerDTO) {
        Optional<Customer> customerToUpdate = customerRepository.findById(id);

        if (customerToUpdate.isPresent()) {
            Customer customer = customerToUpdate.get();
            customer.setCustomerName(customerDTO.getCustomerName());
            customer.setUpdatedDate(LocalDateTime.now());
            return Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(customer)));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean patchCustomerById(UUID customerId, CustomerDTO customerDTO) {
        Optional<Customer> customer = customerRepository.findById(customerId);

        Customer customerToUpdate;

        if (customer.isPresent()) {
            customerToUpdate = customer.get();
            if (StringUtils.hasText(customerDTO.getCustomerName())) {
                customerToUpdate.setCustomerName(customerDTO.getCustomerName());
                customerRepository.save(customerToUpdate);
                return true;
            }
        }
        return false;
    }
}
