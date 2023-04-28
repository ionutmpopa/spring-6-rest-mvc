package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.controller.model.BeerDTO;
import guru.springframework.spring6restmvc.exception.NotFoundException;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.springframework.spring6restmvc.controller.BeerController.BEER_PATH_ID;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    BeerService beerServiceImpl;

//    BeerService getBeerServiceJPA;
//
//    @Autowired
//    BeerRepository beerRepository;
//
//    @Autowired
//    BeerMapper beerMapper;

    @BeforeEach
    public void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void testPatchBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null).get(0);
        beer.setBeerName("Ursus");

        mockMvc.perform(patch(BEER_PATH_ID, beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
            .andExpect(status().isAccepted());
        verify(beerService, times(1)).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        Assertions.assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        Assertions.assertThat(beer).isEqualTo(beerArgumentCaptor.getValue());
    }

    @Test
    void testDeleteBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null).get(0);

        doReturn(true).when(beerService).deleteById(beer.getId());

        mockMvc.perform(delete(BEER_PATH_ID, beer.getId()))
            .andExpect(status().isAccepted());

        verify(beerService, times(1)).deleteById(uuidArgumentCaptor.capture());
        Assertions.assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());

    }

    @Test
    void getBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willThrow(NotFoundException.class);

        mockMvc.perform(get(BEER_PATH_ID, UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null).get(0);
        beer.setBeerName("Ciuc");

        given(beerService.updateBeerById(beer.getId(), beer)).willReturn(Optional.of(beer));

        mockMvc.perform(put(BEER_PATH_ID, beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.beerName", is("Ciuc")));

        verify(beerService, times(1)).updateBeerById(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void testCreateNewBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null).get(0);
        beer.setVersion(null);
        beer.setId(null);

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers(null, null).get(1));

        mockMvc.perform(post(BeerController.API_V_1_BEER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
    }

    @Test
    void testCreateNewBeer_validationException() throws Exception {
        BeerDTO beer = BeerDTO.builder().build();

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beer);

        MvcResult mvcResult = mockMvc.perform(post(BeerController.API_V_1_BEER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.length()", is(5)))
            .andReturn();
    }

    @Test
    void getBeerById() throws Exception {

        BeerDTO testBeer = beerServiceImpl.listBeers(null, null).get(0);

        when(beerService.getBeerById(testBeer.getId())).thenReturn(Optional.of(testBeer));

        mockMvc.perform(get(BEER_PATH_ID, testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
            .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));

    }

    @Test
    void getAllBeers() throws Exception {

        List<BeerDTO> beers = beerServiceImpl.listBeers(null, null);

        when(beerService.listBeers(null, null)).thenReturn(beers);

        mockMvc.perform(get(BeerController.API_V_1_BEER)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()", is(3)))
            .andExpect(jsonPath("$.[1].id", is(beers.get(1).getId().toString())))
            .andExpect(jsonPath("$.[1].beerName", is(beers.get(1).getBeerName())));
    }
}
