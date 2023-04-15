package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.model.BeerDTO;
import guru.springframework.spring6restmvc.domain.Beer;
import guru.springframework.spring6restmvc.exception.NotFoundException;
import guru.springframework.spring6restmvc.mapper.BeerMapper;
import guru.springframework.spring6restmvc.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Primary
@Service
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper mapper;

    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository.findAll().stream()
            .map(mapper::beerToBeerDto)
            .toList();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return beerRepository.findById(id).map(mapper::beerToBeerDto);
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        beer.setCreatedDate(LocalDateTime.now());
        return mapper.beerToBeerDto(beerRepository.save(mapper.beerDtoToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID id, BeerDTO updatedBeer) {

        Optional<Beer> beerToUpdate = beerRepository.findById(id);

        if (beerToUpdate.isPresent()) {
            Beer beer = beerToUpdate.get();
            beer.setBeerName(updatedBeer.getBeerName());
            beer.setBeerStyle(updatedBeer.getBeerStyle());
            beer.setUpdateDate(LocalDateTime.now());
            beer.setPrice(updatedBeer.getPrice());
            beer.setUpc(updatedBeer.getUpc());
            beer.setQuantityOnHand(updatedBeer.getQuantityOnHand());
            return Optional.of(mapper.beerToBeerDto(beerRepository.save(beer)));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(UUID beerId) throws IllegalArgumentException {

        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

    @Override
        public void patchBeerById(UUID beerId, BeerDTO beerDTO) {
        Optional<Beer> beer = beerRepository.findById(beerId);

        Beer beerToUpdate;

        if (beer.isPresent()) {
            beerToUpdate = beer.get();
            if (StringUtils.hasText(beerDTO.getBeerName())) {
                beerToUpdate.setBeerName(beerDTO.getBeerName());
            }

            if (beerDTO.getBeerStyle() != null) {
                beerToUpdate.setBeerStyle(beerDTO.getBeerStyle());
            }

            if (beerDTO.getPrice() != null) {
                beerToUpdate.setPrice(beerDTO.getPrice());
            }

            if (beerDTO.getQuantityOnHand() != null) {
                beerToUpdate.setQuantityOnHand(beerDTO.getQuantityOnHand());
            }

            if (StringUtils.hasText(beerDTO.getUpc())) {
                beerToUpdate.setUpc(beerDTO.getUpc());
            }
            beerRepository.save(beerToUpdate);
        } else {
            throw new NotFoundException();
        }
    }
}
