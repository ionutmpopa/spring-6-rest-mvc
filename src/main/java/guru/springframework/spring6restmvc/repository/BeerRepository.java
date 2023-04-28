package guru.springframework.spring6restmvc.repository;

import guru.springframework.spring6restmvc.controller.model.BeerStyle;
import guru.springframework.spring6restmvc.domain.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

    List<Beer> findAllByBeerStyle(BeerStyle beerStyle);

    List<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName);

}
