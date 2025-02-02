package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.model.BeerDTO;
import guru.springframework.spring6restmvc.controller.model.BeerStyle;
import guru.springframework.spring6restmvc.controller.model.PageBeerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by jt, Spring Framework Guru.
 */
@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDTO beer1 = BeerDTO.builder()
            .id(UUID.randomUUID())
            .version(1)
            .beerName("Galaxy Cat")
            .beerStyle(BeerStyle.PALE_ALE)
            .upc("12356")
            .price(new BigDecimal("12.99"))
            .quantityOnHand(122)
            .createdDate(LocalDateTime.now())
            .updateDate(LocalDateTime.now())
            .build();

        BeerDTO beer2 = BeerDTO.builder()
            .id(UUID.randomUUID())
            .version(1)
            .beerName("Crank")
            .beerStyle(BeerStyle.PALE_ALE)
            .upc("12356222")
            .price(new BigDecimal("11.99"))
            .quantityOnHand(392)
            .createdDate(LocalDateTime.now())
            .updateDate(LocalDateTime.now())
            .build();

        BeerDTO beer3 = BeerDTO.builder()
            .id(UUID.randomUUID())
            .version(1)
            .beerName("Sunshine City")
            .beerStyle(BeerStyle.IPA)
            .upc("12356")
            .price(new BigDecimal("13.99"))
            .quantityOnHand(144)
            .createdDate(LocalDateTime.now())
            .updateDate(LocalDateTime.now())
            .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public void patchBeerById(UUID beerId, BeerDTO beer) {
        BeerDTO existing = beerMap.get(beerId);

        if (StringUtils.hasText(beer.getBeerName())){
            existing.setBeerName(beer.getBeerName());
        }

        if (beer.getBeerStyle() != null) {
            existing.setBeerStyle(beer.getBeerStyle());
        }

        if (beer.getPrice() != null) {
            existing.setPrice(beer.getPrice());
        }

        if (beer.getQuantityOnHand() != null){
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }

        if (StringUtils.hasText(beer.getUpc())) {
            existing.setUpc(beer.getUpc());
        }
    }

    @Override
    public PageBeerDTO listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        return PageBeerDTO.builder().content(new ArrayList<>(beerMap.values())).build();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        log.debug("Get Beer by Id - in service. Id: " + id.toString());
        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {

        BeerDTO savedBeer = BeerDTO.builder()
            .id(UUID.randomUUID())
            .createdDate(LocalDateTime.now())
            .updateDate(LocalDateTime.now())
            .beerName(beer.getBeerName())
            .beerStyle(beer.getBeerStyle())
            .quantityOnHand(beer.getQuantityOnHand())
            .upc(beer.getUpc())
            .price(beer.getPrice())
            .version(beer.getVersion())
            .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID id, BeerDTO updatedBeer) {

        BeerDTO beerToUpdate = this.beerMap.get(id);

        beerToUpdate.setBeerName(updatedBeer.getBeerName());
        beerToUpdate.setBeerStyle(updatedBeer.getBeerStyle());
        beerToUpdate.setPrice(updatedBeer.getPrice());
        beerToUpdate.setUpc(updatedBeer.getUpc());
        beerToUpdate.setQuantityOnHand(updatedBeer.getQuantityOnHand());
        beerToUpdate.setUpdateDate(LocalDateTime.now());
        beerToUpdate.setVersion(beerToUpdate.getVersion() + 1);

        this.beerMap.put(beerToUpdate.getId(), beerToUpdate);

        return Optional.of(beerToUpdate);
    }

    @Override
    public boolean deleteById(UUID id) {
        if (beerMap.containsKey(id)) {
            this.beerMap.remove(id);
            return true;
        }
        return false;
    }

}

















