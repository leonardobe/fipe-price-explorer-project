package br.com.fipe.fipepriceexplorer.service;

import br.com.fipe.fipepriceexplorer.client.FipeClient;
import br.com.fipe.fipepriceexplorer.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FipeService {
    private final FipeClient fipeClient;

    public FipeService(FipeClient fipeClient) {
        this.fipeClient = fipeClient;
    }

    public List<FipeBrandDTO> listCarBrands() {
        return fipeClient.getCarsBrands();
    }

    public List<FipeModelDTO> listModelsByBrand(String brandCode) {

        return fipeClient.getModelsByBrand(brandCode);
    }

    public List<FipePriceDTO> listPricesForAllYears(String brandCode, String modelCode, Optional<String> year) {

        List<FipeYearDTO> years = fipeClient.getYearsByModel(brandCode, modelCode);

        if (year.isEmpty()) {
            return years.stream()
                    .map(y -> fipeClient.getPriceByYear(brandCode, modelCode, y.getYear()))
                    .toList();
        }

        String yearInput = year.get();

        List<FipeYearDTO> matchedYears = years.stream()
                .filter(y -> y.getYear().startsWith(yearInput + "-"))
                .toList();

        return matchedYears.stream()
                .map(y -> fipeClient.getPriceByYear(brandCode, modelCode, y.getYear()))
                .toList();
    }
    ;

    public List<VehicleModelsDTO> listModelsByBrandAndYear(String brandCode, String year) {

        if (year == null || !year.matches("\\d{4}")) {
            throw new IllegalArgumentException("Invalid year format. Use YYYY.");
        }

        List<VehicleFuelDTO> allYears = fipeClient.getYearsByAllModels(brandCode);

        List<VehicleFuelDTO> matchedYears =
                allYears.stream().filter(y -> y.yearCode().startsWith(year)).toList();

        if (matchedYears.isEmpty()) {
            throw new IllegalArgumentException("No data found for this year.");
        }

        return matchedYears.stream()
                .flatMap(y -> fipeClient.getModelsByBrandAndYear(brandCode, y.yearCode()).stream())
                .toList();
    }
    ;
}
