package guru.springframework.spring6restmvc.repository;

import guru.springframework.spring6restmvc.bootstrap.BootstrapApp;
import guru.springframework.spring6restmvc.controller.model.BeerStyle;
import guru.springframework.spring6restmvc.domain.Beer;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(BootstrapApp.class)
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void saveBeer() {

        Beer savedBeer = beerRepository.saveAndFlush(Beer.builder()
                .beerName("Ciuc")
                .beerStyle(BeerStyle.LAGER)
                .upc("12345")
                .price(BigDecimal.valueOf(12.12))
            .build());

        Beer beerFromDb = beerRepository.findById(savedBeer.getId()).get();

        Assertions.assertThat(savedBeer.getId()).isNotNull();
        Assertions.assertThat(beerFromDb.getBeerName()).isEqualTo("Ciuc");
    }

    @Test
    void saveBeer_constraintValidation() {

        assertThrows(ConstraintViolationException.class, () -> {
            Beer savedBeer = beerRepository.saveAndFlush(Beer.builder()
                .beerName("Ciuc 123456789123456789123456789123456789123456789123456789")
                .beerStyle(BeerStyle.LAGER)
                .upc("12345")
                .price(BigDecimal.valueOf(12.12))
                .build());

            Beer beerFromDb = beerRepository.findById(savedBeer.getId()).get();
        });
    }

    @Test
    void beerInitialization() {
        Assertions.assertThat(beerRepository.count()).isEqualTo(3L);
    }
}
