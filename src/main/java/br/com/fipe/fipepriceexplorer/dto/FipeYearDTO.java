package br.com.fipe.fipepriceexplorer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FipeYearDTO {
    @JsonProperty("code")
    private String year;

    @JsonProperty("name")
    private String typeModel;

    public String getYear() {
        return year;
    }

    public String getTypeModel() {
        return typeModel;
    }
}
