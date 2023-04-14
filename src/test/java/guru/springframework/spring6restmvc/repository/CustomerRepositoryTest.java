package guru.springframework.spring6restmvc.repository;

import guru.springframework.spring6restmvc.domain.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
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
}
