package br.com.fipe.fipepriceexplorer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VehicleModelsDTO(
        @JsonProperty("code") String modelCode,
        @JsonProperty("name") String modelDetails) {}
