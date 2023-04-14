package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.model.BeerDTO;
import guru.springframework.spring6restmvc.domain.Beer;
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
        return mapper.beerToBeerDto(beerRepository.save(mapper.beerDtoToBeer(beer)));
    }

    @Override
    public BeerDTO updateBeerById(UUID id, BeerDTO updatedBeer) {

        Optional<Beer> beerToUpdate = beerRepository.findById(id);

        BeerDTO.BeerDTOBuilder beerDTO = BeerDTO.builder();

        if (beerToUpdate.isPresent()) {
           beerDTO
                .beerName(updatedBeer.getBeerName())
                .beerStyle(updatedBeer.getBeerStyle())
                .createdDate(updatedBeer.getCreatedDate())
                .updateDate(LocalDateTime.now())
                .price(updatedBeer.getPrice())
                .upc(updatedBeer.getUpc())
                .quantityOnHand(updatedBeer.getQuantityOnHand())
                .build();
        }

        return mapper.beerToBeerDto(beerRepository.save(mapper.beerDtoToBeer(beerDTO.build())));
    }

    @Override
    public void deleteById(UUID beerId) {
        beerRepository.deleteById(beerId);
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
        }
    }
}
