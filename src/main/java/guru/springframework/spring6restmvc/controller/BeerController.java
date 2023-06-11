package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.controller.model.BeerDTO;
import guru.springframework.spring6restmvc.controller.model.BeerStyle;
import guru.springframework.spring6restmvc.controller.model.PageBeerDTO;
import guru.springframework.spring6restmvc.exception.NotFoundException;
import guru.springframework.spring6restmvc.services.BeerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping(BeerController.API_V_1_BEER)
@RestController
public class BeerController implements BeerControllerApi {
    public static final String API_V_1_BEER = "/api/v1/beer";
    public static final String BEER_ID = "/{beerId}";
    public static final String BEER_PATH_ID = API_V_1_BEER + BEER_ID;

    private final BeerService beerService;

    @Override
    @PostMapping
    public ResponseEntity<Void> addNewBeer(@Valid @RequestBody BeerDTO beer){
        BeerDTO savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", API_V_1_BEER + "/" + savedBeer.getId().toString());

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @Override
    @PutMapping(BEER_ID)
    public ResponseEntity<BeerDTO> updateBeerById(@PathVariable("beerId") UUID id, @Valid @RequestBody BeerDTO updatedBeer){
        log.debug("Update Beer by Id - in controller");

        BeerDTO updatedBeerDTO = beerService.updateBeerById(id, updatedBeer).orElseThrow(NotFoundException::new);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", API_V_1_BEER + "/" + updatedBeerDTO.getId().toString());

        return ResponseEntity.ok().headers(httpHeaders).body(updatedBeerDTO);
    }

    @Override
    @PatchMapping(BEER_ID)
    public ResponseEntity<Void> partiallyUpdateBeerById(@PathVariable("beerId") UUID id, @RequestBody BeerDTO updatedBeer){
        log.debug("Update Beer by Id - in controller");
        beerService.patchBeerById(id, updatedBeer);
        return ResponseEntity.accepted().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<PageBeerDTO> listBeers(@RequestParam(value = "beerName", required = false) String beerName,
                                  @RequestParam(value = "beerStyle", required = false) BeerStyle beerStyle,
                                  @RequestParam(value = "showInventory", required = false) Boolean showInventory,
                                  @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                  @RequestParam(value = "pageSize", required = false) Integer pageSize){

        PageBeerDTO pageBeerDTO = beerService.listBeers(beerName, beerStyle, showInventory, pageNumber, pageSize);
        return ResponseEntity.ok(pageBeerDTO);
    }

    @Override
    @GetMapping(BEER_ID)
    public ResponseEntity<BeerDTO> getBeerById(@PathVariable("beerId") UUID id){
        log.debug("Get Beer by Id - in controller");
        BeerDTO beerDTO = beerService.getBeerById(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(beerDTO);
    }

    @Override
    @DeleteMapping(BEER_ID)
    public ResponseEntity<Void> deleteBeerById(@PathVariable("beerId") UUID id) {
        if (this.beerService.deleteById(id)) {
            return ResponseEntity.accepted().build();
        }
        throw new NotFoundException();
    }
}
