package guru.springframework.spring6restmvc.repository;

import guru.springframework.spring6restmvc.controller.model.BeerStyle;
import guru.springframework.spring6restmvc.domain.Beer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void saveBeer() {

        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("Ciuc")
                .beerStyle(BeerStyle.LAGER)
            .build());

        Beer beerFromDb = beerRepository.findById(savedBeer.getId()).get();

        Assertions.assertThat(savedBeer.getId()).isNotNull();
        Assertions.assertThat(beerFromDb.getBeerName()).isEqualTo("Ciuc");
    }

}
