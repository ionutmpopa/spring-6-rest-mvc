package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.controller.model.BeerStyle;
import guru.springframework.spring6restmvc.domain.Beer;
import guru.springframework.spring6restmvc.domain.Customer;
import guru.springframework.spring6restmvc.repository.BeerRepository;
import guru.springframework.spring6restmvc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class BootstrapApp implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        loadBeers();
        loadCustomers();
        log.info("Beers in DB: {}", beerRepository.count());
        log.info("Customers in DB: {}", customerRepository.count());

    }

    private void loadCustomers() {
        Customer customer1 = Customer.builder()
            .customerName("Gogu Freciparu")
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

        Customer customer2 = Customer.builder()
            .customerName("Damigean Prabuseanu")
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

        Customer customer3 = Customer.builder()
            .customerName("Sandu Ciorba")
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

        customerRepository.saveAllAndFlush(List.of(customer1, customer2, customer3));
    }

    private void loadBeers() {
        Beer beer1 = Beer.builder()
            .beerName("Galaxy Cat")
            .beerStyle(BeerStyle.PALE_ALE)
            .upc("12356")
            .price(new BigDecimal("12.99"))
            .quantityOnHand(122)
            .createdDate(LocalDateTime.now())
            .updateDate(LocalDateTime.now())
            .build();

        Beer beer2 = Beer.builder()
            .beerName("Crank")
            .beerStyle(BeerStyle.PALE_ALE)
            .upc("12356222")
            .price(new BigDecimal("11.99"))
            .quantityOnHand(392)
            .createdDate(LocalDateTime.now())
            .updateDate(LocalDateTime.now())
            .build();

        Beer beer3 = Beer.builder()
            .beerName("Sunshine City")
            .beerStyle(BeerStyle.IPA)
            .upc("12356")
            .price(new BigDecimal("13.99"))
            .quantityOnHand(144)
            .createdDate(LocalDateTime.now())
            .updateDate(LocalDateTime.now())
            .build();

        beerRepository.saveAllAndFlush(List.of(beer1, beer2, beer3));
    }
}
