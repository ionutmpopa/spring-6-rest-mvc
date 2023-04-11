package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(CustomerController.API_V_1_CUSTOMER)
@RestController
public class CustomerController {

    public static final String API_V_1_CUSTOMER = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = API_V_1_CUSTOMER + "/{customerId}";

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Void> addCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = this.customerService.saveCustomer(customer);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", API_V_1_CUSTOMER + "/" + savedCustomer.getCustomerId().toString());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}")
    public Customer updateBeerById(@PathVariable("customerId") UUID id, @RequestBody Customer customer){
        log.debug("Update Customer by Id - in controller");
        return customerService.updateCustomer(id, customer);
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<Void> partiallyUpdateCustomerById(@PathVariable("customerId") UUID id, @RequestBody Customer customer){
        log.debug("Partially update Customer by Id - in controller");
        customerService.patchCustomerById(id, customer);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return this.customerService.listCustomers();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") UUID id) {
        return this.customerService.getCustomer(id);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable("customerId") UUID id) {
        this.customerService.deleteById(id);
        return ResponseEntity.accepted().build();
    }

}
