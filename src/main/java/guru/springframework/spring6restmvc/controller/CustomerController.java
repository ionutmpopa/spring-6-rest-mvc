package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/customer")
@RestController
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping
    public List<Customer> getCustomers() {
        return this.customerService.listCustomers();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") UUID id) {
        return this.customerService.getCustomer(id);
    }

}
