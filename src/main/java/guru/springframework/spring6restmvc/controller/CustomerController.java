package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.controller.model.CustomerDTO;
import guru.springframework.spring6restmvc.exception.NotFoundException;
import guru.springframework.spring6restmvc.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping(CustomerController.API_V_1_CUSTOMER)
@RestController
public class CustomerController implements CustomerControllerApi {

    public static final String API_V_1_CUSTOMER = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = API_V_1_CUSTOMER + "/{customerId}";

    private final CustomerService customerService;

    @Override
    @PostMapping
    public ResponseEntity<Void> addCustomer(@Valid @RequestBody CustomerDTO customer) {
        CustomerDTO savedCustomer = this.customerService.saveCustomer(customer);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", API_V_1_CUSTOMER + "/" + savedCustomer.getId().toString());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomerById(@PathVariable("customerId") UUID id, @Valid @RequestBody CustomerDTO customer){
        log.debug("Update Customer by Id - in controller");

        CustomerDTO customerDTO = customerService.updateCustomer(id, customer).orElseThrow(NotFoundException::new);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("location", API_V_1_CUSTOMER + "/" + customerDTO.getId().toString());

        return ResponseEntity.ok().headers(httpHeaders).body(customerDTO);

    }

    @Override
    @PatchMapping("/{customerId}")
    public ResponseEntity<Void> partiallyUpdateCustomerById(@PathVariable("customerId") UUID id, @Valid @RequestBody CustomerDTO customer){
        log.debug("Partially update Customer by Id - in controller");
        if (customerService.patchCustomerById(id, customer)) {
            return ResponseEntity.accepted().build();
        }
        throw new NotFoundException();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getCustomers() {
        List<CustomerDTO> customerDTOList = this.customerService.listCustomers();
        return ResponseEntity.ok(customerDTOList);
    }

    @Override
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("customerId") UUID id) {
        CustomerDTO customerDTO = this.customerService.getCustomer(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(customerDTO);
    }

    @Override
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable("customerId") UUID id) {
        if (this.customerService.deleteById(id)) {
            return ResponseEntity.accepted().build();
        }
        throw new NotFoundException();
    }

}
