package guru.springframework.spring6restmvc.services;

import com.opencsv.bean.CsvToBeanBuilder;
import guru.springframework.spring6restmvc.controller.model.BeerCSVRecord;
import guru.springframework.spring6restmvc.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {
    @Override
    public List<BeerCSVRecord> convertCSV(File csvFile) {

        try {
            return new CsvToBeanBuilder<BeerCSVRecord>(new FileReader(csvFile))
                .withType(BeerCSVRecord.class)
                .build()
                .parse();
        } catch (FileNotFoundException e) {
            throw new NotFoundException("File not found!", e);
        }
    }
}
