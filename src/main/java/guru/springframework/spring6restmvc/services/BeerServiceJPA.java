package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.model.BeerDTO;
import guru.springframework.spring6restmvc.controller.model.BeerStyle;
import guru.springframework.spring6restmvc.domain.Beer;
import guru.springframework.spring6restmvc.exception.NotFoundException;
import guru.springframework.spring6restmvc.mapper.BeerMapper;
import guru.springframework.spring6restmvc.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@RequiredArgsConstructor
@Primary
@Service
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper mapper;

    @Override
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory) {

        List<Beer> beerList;

        if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerList = beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle);
        } else if (StringUtils.hasText(beerName)) {
            beerList = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%");
        } else if (beerStyle != null) {
            beerList = beerRepository.findAllByBeerStyle(beerStyle);
        } else {
            beerList = beerRepository.findAll();
        }

        if (showInventory != null && !showInventory) {
            beerList.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerList.stream()
            .map(mapper::beerToBeerDto)
            .toList();

    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return beerRepository.findById(id).map(mapper::beerToBeerDto);
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return mapper.beerToBeerDto(beerRepository.saveAndFlush(mapper.beerDtoToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID id, BeerDTO updatedBeer) {

        Optional<Beer> beerToUpdate = beerRepository.findById(id);

        if (beerToUpdate.isPresent()) {
            Beer beer = beerToUpdate.get();
            beer.setBeerName(updatedBeer.getBeerName());
            beer.setBeerStyle(updatedBeer.getBeerStyle());
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
