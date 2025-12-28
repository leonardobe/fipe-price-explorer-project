package br.com.fipe.fipepriceexplorer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VehicleFuelDTO(
        @JsonProperty("code") String yearCode,
        @JsonProperty("name") String fuelType) {}
