package br.com.fipe.fipepriceexplorer.dto;

public class FipePriceDTO {
    private String brand;
    private String fuel;
    private String model;
    private Integer modelYear;
    private String price;

    public String getBrand() {
        return brand;
    }

    public String getFuel() {
        return fuel;
    }

    public String getModel() {
        return model;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public String getPrice() {
        return price;
    }
}
