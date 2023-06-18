package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.model.BeerCSVRecord;

import java.io.BufferedReader;
import java.util.List;

public interface BeerCsvService {
    List<BeerCSVRecord> convertCSV(BufferedReader bufferedReader);
}
