package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.controller.model.BeerDTO;
import guru.springframework.spring6restmvc.controller.model.BeerStyle;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.exception.NotFoundException;
import guru.springframework.spring6restmvc.repository.BeerRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.*;

import static guru.springframework.spring6restmvc.controller.BeerController.BEER_PATH_ID;
import static guru.springframework.spring6restmvc.controller.BeerControllerTest.PASSWORD;
import static guru.springframework.spring6restmvc.controller.BeerControllerTest.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerControllerIT {
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();
    }

    @Test
    void testListBeersByStyle() throws Exception {

        mockMvc.perform(get(BeerController.API_V_1_BEER)
                .with(httpBasic(USER, PASSWORD))
                .queryParam("beerStyle", "IPA")
                .queryParam("pageSize", "1000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.size()", is(548)));
    }

    @Test
    void testListBeersByStyle_401Unauthorized() throws Exception {

        mockMvc.perform(get(BeerController.API_V_1_BEER)
                .queryParam("beerStyle", "IPA")
                .queryParam("pageSize", "1000"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void testListBeersByName() throws Exception {

        mockMvc.perform(get(BeerController.API_V_1_BEER)
                .with(httpBasic(USER, PASSWORD))
                .queryParam("beerName", "IPA")
                .queryParam("pageSize", "1000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.size()", is(336)));
    }

    @Test
    void testListBeersByNameAndStyle() throws Exception {

        mockMvc.perform(get(BeerController.API_V_1_BEER)
                .with(httpBasic(USER, PASSWORD))
                .queryParam("beerName", "IPA")
                .queryParam("beerStyle", String.valueOf(BeerStyle.IPA))
                .queryParam("pageSize", "1000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.size()", is(310)));
    }

    @Test
    void testListBeersByNameAndStyleShowInventoryTrue() throws Exception {

        mockMvc.perform(get(BeerController.API_V_1_BEER)
                .with(httpBasic(USER, PASSWORD))
                .queryParam("beerName", "IPA")
                .queryParam("beerStyle", String.valueOf(BeerStyle.IPA))
                .queryParam("showInventory", "true")
                .queryParam("pageSize", "1000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.size()", is(310)))
            .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void testListBeersByNameAndStyleShowInventoryTruePage2() throws Exception {

        mockMvc.perform(get(BeerController.API_V_1_BEER)
                .with(httpBasic(USER, PASSWORD))
                .queryParam("beerName", "IPA")
                .queryParam("beerStyle", String.valueOf(BeerStyle.IPA))
                .queryParam("showInventory", "true")
                .queryParam("pageNumber", "2")
                .queryParam("pageSize", "50")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.size()", is(50)))
            .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void testListBeersByNameAndStyleShowInventoryFalse() throws Exception {

        mockMvc.perform(get(BeerController.API_V_1_BEER)
                .with(httpBasic(USER, PASSWORD))
                .queryParam("beerName", "IPA")
                .queryParam("beerStyle", String.valueOf(BeerStyle.IPA))
                .queryParam("showInventory", "false")
                .queryParam("pageSize", "1000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.size()", is(310)))
            .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.nullValue()));
    }


    @Test
    void testPatchBeer_badRequest() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name 123456789123456789123456789123456789ffddsdsdsdsdsd");

        mockMvc.perform(patch(BEER_PATH_ID, beer.getId())
                .with(httpBasic(USER, PASSWORD))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getBeerById() {
        Beer beer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = beerController.getBeerById(beer.getId());

        Assertions.assertThat(beerDTO).isNotNull();

    }

    @Test
    void getBeerById_notFound() {
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    void testListBeers() {
        Page<BeerDTO> dtos = beerController.listBeers("", null, false, 1, 1000);

        assertThat(dtos.getContent()).hasSize(1000);
    }

    @Rollback
    @Transactional
    @Test
    void saveNewBeer() {
        ResponseEntity<Void> responseEntity = beerController.addNewBeer(BeerDTO.builder()
            .beerName("Corona")
            .beerStyle(BeerStyle.PILSNER)
            .upc("54321")
            .quantityOnHand(1)
            .price(new BigDecimal("12.22"))
            .build());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String[] locationUUID = Objects.requireNonNull(responseEntity.getHeaders().getLocation()).getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Beer beer = beerRepository.findById(savedUUID).get();

        assertThat(beer).isNotNull();
        assertThat(beer.getCreatedDate()).isNotNull();

    }

    @Rollback
    @Transactional
    @Test
    void updateBeerById() {

        Beer beer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = BeerDTO.builder()
            .price(BigDecimal.valueOf(15.55))
            .quantityOnHand(5)
            .beerName("Radler")
            .upc("4342123")
            .beerStyle(BeerStyle.GOSE)
            .build();

        ResponseEntity<BeerDTO> responseEntity = beerController.updateBeerById(beer.getId(), beerDTO);

        String[] locationUUID = Objects.requireNonNull(responseEntity.getHeaders().getLocation()).getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(beer.getId()).isEqualTo(Objects.requireNonNull(savedUUID));
        assertThat(beer.getUpdateDate()).isNotNull();
    }

    @Test
    void updateBeerById_notFound() {

        BeerDTO beerDTO = BeerDTO.builder()
            .price(BigDecimal.valueOf(15.55))
            .quantityOnHand(5)
            .beerName("Radler")
            .upc("4342123")
            .beerStyle(BeerStyle.GOSE)
            .build();

        assertThrows(NotFoundException.class, () -> beerController.updateBeerById(UUID.randomUUID(), beerDTO));
    }

    @Rollback
    @Transactional
    @Test
    void patchBeerById() {

        Beer beer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = BeerDTO.builder()
            .price(BigDecimal.valueOf(18.55))
            .build();

        ResponseEntity<Void> responseEntity = beerController.partiallyUpdateBeerById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    void patchBeerById_notFound() {

        BeerDTO beerDTO = BeerDTO.builder()
            .price(BigDecimal.valueOf(18.55))
            .build();

        assertThrows(NotFoundException.class, () -> beerController.partiallyUpdateBeerById(UUID.randomUUID(), beerDTO));
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        Page<BeerDTO> dtos = beerController.listBeers("", null, false, 1, 25);

        assertThat(dtos.getContent()).isEmpty();
    }

    @Rollback
    @Transactional
    @Test
    void deleteBeerById() {
        Beer beer = beerRepository.findAll().get(0);

        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(2413);

        ResponseEntity<Void> responseEntity = beerController.deleteBeerById(beer.getId());

        beerList = beerRepository.findAll();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(beerList).hasSize(2412);

    }

    @Test
    void deleteBeerById_notFound() {
        assertThrows(NotFoundException.class, () -> beerController.deleteBeerById(UUID.randomUUID()));
    }
}







