package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
public interface BeerService {

    List<BeerDTO> listBeers();

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    BeerDTO updateBeerById(UUID id, BeerDTO updatedBeer);

    void deleteById(UUID beerId);

    void patchBeerById(UUID beerId, BeerDTO beer);
}
