package guru.springframework.spring6restmvc.services;

import com.opencsv.bean.CsvToBeanBuilder;
import guru.springframework.spring6restmvc.controller.model.BeerCSVRecord;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.List;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {
    @Override
    public List<BeerCSVRecord> convertCSV(BufferedReader bufferedReader) {

        return new CsvToBeanBuilder<BeerCSVRecord>(bufferedReader)
            .withType(BeerCSVRecord.class)
            .build()
            .parse();
    }
}
