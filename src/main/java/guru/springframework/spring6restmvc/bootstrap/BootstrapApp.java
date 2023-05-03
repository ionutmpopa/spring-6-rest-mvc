package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.controller.model.BeerStyle;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.repository.BeerRepository;
import guru.springframework.spring6restmvc.repository.CustomerRepository;
import guru.springframework.spring6restmvc.services.BeerCsvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class BootstrapApp implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    private final BeerCsvService beerCsvService;

    @Transactional
    @Override
    public void run(String... args) throws FileNotFoundException {

        if (beerRepository.count() == 0) {
            loadBeers();
        }

        if (customerRepository.count() == 0) {
            loadCustomers();
        }

        if (beerRepository.count() < 10) {
            loadCsvData();
        }

        log.info("Beers in DB: {}", beerRepository.count());
        log.info("Customers in DB: {}", customerRepository.count());

    }

    private void loadCsvData() throws FileNotFoundException {

        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

        List<Beer> beerList = beerCsvService.convertCSV(file).stream()
                .map(beerCSVRecord -> {
                    BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                        case "American Pale Lager" -> BeerStyle.LAGER;
                        case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                            BeerStyle.ALE;
                        case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                        case "American Porter" -> BeerStyle.PORTER;
                        case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                        case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                        case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                        case "English Pale Ale" -> BeerStyle.PALE_ALE;
                        default -> BeerStyle.PILSNER;
                    };

                    return Beer.builder()
                        .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                        .beerStyle(beerStyle)
                        .price(BigDecimal.TEN)
                        .upc(beerCSVRecord.getRow().toString())
                        .quantityOnHand(beerCSVRecord.getCountX())
                        .build();
                })
                    .toList();
        beerRepository.saveAllAndFlush(beerList);
    }

    private void loadCustomers() {
        Customer customer1 = Customer.builder()
            .customerName("Gogu Freciparu")
            .build();

        Customer customer2 = Customer.builder()
            .customerName("Damigean Prabuseanu")
            .build();

        Customer customer3 = Customer.builder()
            .customerName("Sandu Ciorba")
            .build();

        customerRepository.saveAllAndFlush(List.of(customer1, customer2, customer3));
    }

    private void loadBeers() {
        Beer beer1 = Beer.builder()
            .beerName("Galaxy Cat")
            .beerStyle(BeerStyle.PALE_ALE)
            .upc("12356")
            .price(new BigDecimal("12.99"))
            .quantityOnHand(122)
            .build();

        Beer beer2 = Beer.builder()
            .beerName("Crank")
            .beerStyle(BeerStyle.PALE_ALE)
            .upc("12356222")
            .price(new BigDecimal("11.99"))
            .quantityOnHand(392)
            .build();

        Beer beer3 = Beer.builder()
            .beerName("Sunshine City")
            .beerStyle(BeerStyle.IPA)
            .upc("12356")
            .price(new BigDecimal("13.99"))
            .quantityOnHand(144)
            .build();

        beerRepository.saveAllAndFlush(List.of(beer1, beer2, beer3));
    }
}
