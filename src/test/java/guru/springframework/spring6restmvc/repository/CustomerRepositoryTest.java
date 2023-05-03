package guru.springframework.spring6restmvc.repository;

import guru.springframework.spring6restmvc.bootstrap.BootstrapApp;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.services.BeerCsvServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({BootstrapApp.class, BeerCsvServiceImpl.class})
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void saveCustomer() {

        Customer savedCustomer = customerRepository.save(Customer.builder()
                .customerName("Gicu Grecu")
            .build());

        Customer customerFromDb = customerRepository.findById(savedCustomer.getId()).orElse(null);

        Assertions.assertThat(savedCustomer.getId()).isNotNull();
        Assertions.assertThat(customerFromDb.getCustomerName()).isEqualTo("Gicu Grecu");
    }

    @Test
    void customerInitialization() {
        Assertions.assertThat(customerRepository.count()).isEqualTo(3L);
    }
}
