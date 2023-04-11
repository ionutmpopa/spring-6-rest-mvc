package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping(BeerController.API_V_1_BEER)
@RestController
public class BeerController {
    public static final String API_V_1_BEER = "/api/v1/beer";
    public static final String BEER_ID = "/{beerId}";
    public static final String BEER_PATH_ID = API_V_1_BEER + BEER_ID;

    private final BeerService beerService;

    @PostMapping
    public ResponseEntity<Void> handlePost(@RequestBody Beer beer){
        Beer savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("location", API_V_1_BEER + "/" + savedBeer.getId().toString());

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(BEER_ID)
    public Beer updateBeerById(@PathVariable("beerId") UUID id, @RequestBody Beer updatedBeer){
        log.debug("Update Beer by Id - in controller");
        return beerService.updateBeerById(id, updatedBeer);
    }

    @PatchMapping(BEER_ID)
    public ResponseEntity<Void> partiallyUpdateBeerById(@PathVariable("beerId") UUID id, @RequestBody Beer updatedBeer){
        log.debug("Update Beer by Id - in controller");
        beerService.patchBeerById(id, updatedBeer);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public List<Beer> listBeers(){
        return beerService.listBeers();
    }

    @GetMapping(BEER_ID)
    public Beer getBeerById(@PathVariable("beerId") UUID id){
        log.debug("Get Beer by Id - in controller");
        return beerService.getBeerById(id);
    }

    @DeleteMapping(BEER_ID)
    public ResponseEntity<Void> deleteBeerById(@PathVariable("beerId") UUID id) {
        this.beerService.deleteById(id);
        return ResponseEntity.accepted().build();
    }

}
