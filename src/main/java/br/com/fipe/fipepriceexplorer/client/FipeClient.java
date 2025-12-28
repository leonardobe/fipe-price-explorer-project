package br.com.fipe.fipepriceexplorer.client;

import br.com.fipe.fipepriceexplorer.dto.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class FipeClient {

    private final WebClient webClient;

    public FipeClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<FipeBrandDTO> getCarsBrands() {
        try {
            return webClient
                    .get()
                    .uri("/cars/brands")
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                            .flatMap(body -> Mono.error(new RuntimeException(
                                    "Failed to fetch brands from FIPE API. HTTP Status: " + r.statusCode()))))
                    .bodyToFlux(FipeBrandDTO.class)
                    .collectList()
                    .block();

        } catch (WebClientResponseException ex) {
            throw new RuntimeException(
                    "FIPE API responded with error " + ex.getStatusCode() + " while retrieving brands.");
        } catch (WebClientRequestException ex) {
            throw new RuntimeException(
                    "Connectivity error while accessing FIPE API to retrieve brands. Please check your network.");
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error while retrieving brands: " + ex.getMessage());
        }
    }

    public List<FipeModelDTO> getModelsByBrand(String brandCode) {
        try {
            return webClient
                    .get()
                    .uri("/cars/brands/{code}/models", brandCode)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            r -> Mono.error(new RuntimeException(
                                    "Invalid brand code '" + brandCode + "'. Unable to fetch models.")))
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            r -> Mono.error(new RuntimeException(
                                    "FIPE API server error while fetching models for brand '" + brandCode + "'.")))
                    .bodyToFlux(FipeModelDTO.class)
                    .collectList()
                    .block();

        } catch (WebClientResponseException ex) {
            throw new RuntimeException("API response error " + ex.getStatusCode()
                    + " while retrieving models for brand " + brandCode + ".");
        } catch (WebClientRequestException ex) {
            throw new RuntimeException("Connectivity issue while retrieving models for brand " + brandCode + ".");
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error while retrieving models: " + ex.getMessage());
        }
    }

    public List<VehicleFuelDTO> getYearsByAllModels(String brandCode) {
        try {
            return webClient
                    .get()
                    .uri("/cars/brands/{brandId}/years", brandCode)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            r -> Mono.error(new RuntimeException("Unable to retrieve years for brand '" + brandCode
                                    + "'. HTTP Status: " + r.statusCode())))
                    .bodyToFlux(VehicleFuelDTO.class)
                    .collectList()
                    .block();

        } catch (WebClientResponseException ex) {
            throw new RuntimeException("FIPE API error " + ex.getStatusCode()
                    + " while retrieving available years for brand " + brandCode + ".");
        } catch (WebClientRequestException ex) {
            throw new RuntimeException("Network error while retrieving years for brand " + brandCode + ".");
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error while retrieving years: " + ex.getMessage());
        }
    }

    public List<FipeYearDTO> getYearsByModel(String brandCode, String modelCode) {
        try {
            return webClient
                    .get()
                    .uri("/cars/brands/{brandId}/models/{modelId}/years", brandCode, modelCode)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            r -> Mono.error(new RuntimeException("Invalid brand or model code. Brand: '" + brandCode
                                    + "', Model: '" + modelCode + "'.")))
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            r -> Mono.error(new RuntimeException(
                                    "FIPE API server error while retrieving years for model " + modelCode + ".")))
                    .bodyToFlux(FipeYearDTO.class)
                    .collectList()
                    .block();

        } catch (WebClientResponseException ex) {
            throw new RuntimeException(
                    "API error " + ex.getStatusCode() + " while retrieving years for model " + modelCode + ".");
        } catch (WebClientRequestException ex) {
            throw new RuntimeException("Connectivity error while retrieving years for model " + modelCode + ".");
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error while retrieving model years: " + ex.getMessage());
        }
    }

    public FipePriceDTO getPriceByYear(String brandCode, String modelCode, String yearCode) {
        try {
            return webClient
                    .get()
                    .uri("/cars/brands/{brandId}/models/{modelId}/years/{yearId}", brandCode, modelCode, yearCode)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            r -> Mono.error(new RuntimeException("Invalid parameters provided. Brand: '" + brandCode
                                    + "', Model: '" + modelCode
                                    + "', Year: '" + yearCode + "'.")))
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            r -> Mono.error(new RuntimeException("FIPE API server error while retrieving price data.")))
                    .bodyToMono(FipePriceDTO.class)
                    .block();

        } catch (WebClientResponseException ex) {
            throw new RuntimeException("API error " + ex.getStatusCode() + " while retrieving price information.");
        } catch (WebClientRequestException ex) {
            throw new RuntimeException("Connectivity error while retrieving price information.");
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error while retrieving price data: " + ex.getMessage());
        }
    }

    public List<VehicleModelsDTO> getModelsByBrandAndYear(String brandId, String yearCode) {
        try {
            return webClient
                    .get()
                    .uri("/cars/brands/{brandId}/years/{yearId}/models", brandId, yearCode)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, r -> r.bodyToMono(String.class)
                            .flatMap(body -> Mono.error(new RuntimeException(String.format(
                                    "Resource not found or invalid parameters: Brand ID '%s' or Year '%s' "
                                            + "might be incorrect. (HTTP %s)",
                                    brandId, yearCode, r.statusCode())))))
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            r -> Mono.error(new RuntimeException(
                                    "FIPE API server error while fetching models for the given year.")))
                    .bodyToFlux(VehicleModelsDTO.class)
                    .collectList()
                    .block();

        } catch (WebClientResponseException ex) {
            throw new RuntimeException(
                    "API response error: Received " + ex.getStatusCode() + " while fetching models by brand and year.");
        } catch (WebClientRequestException ex) {
            throw new RuntimeException(
                    "Connectivity error: Unable to reach the FIPE service. Please check your internet connection.");
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected internal error while fetching models: " + ex.getMessage());
        }
    }
}
