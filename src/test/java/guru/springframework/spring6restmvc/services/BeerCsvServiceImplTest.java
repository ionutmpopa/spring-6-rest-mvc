package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.model.BeerCSVRecord;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BeerCsvServiceImplTest {

    BeerCsvService beerCsvService = new BeerCsvServiceImpl();

    @Test
    void convertCSV() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/beers.csv")) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                List<BeerCSVRecord> recs = beerCsvService.convertCSV(reader);
                assertThat(recs).isNotEmpty();
                assertThat(recs.get(0).getBeer()).isEqualTo("Pub Beer");
            }
        }
    }
}
