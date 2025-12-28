package br.com.fipe.fipepriceexplorer.util;

import br.com.fipe.fipepriceexplorer.dto.FipePriceDTO;

import java.util.List;

public class ConsoleTablePrinter {

    public static void printPriceTable(List<FipePriceDTO> prices) {

        int wBrand = Math.max(
                "BRAND".length(),
                prices.stream().mapToInt(p -> p.getBrand().length()).max().orElse(0));
        int wModel = Math.max(
                "MODEL".length(),
                prices.stream().mapToInt(p -> p.getModel().length()).max().orElse(0));
        int wYear = Math.max(
                "YEAR".length(),
                prices.stream()
                        .mapToInt(p -> String.valueOf(p.getModelYear()).length())
                        .max()
                        .orElse(0));
        int wFuel = Math.max(
                "FUEL".length(),
                prices.stream().mapToInt(p -> p.getFuel().length()).max().orElse(0));
        int wPrice = Math.max(
                "PRICE".length(),
                prices.stream().mapToInt(p -> p.getPrice().length()).max().orElse(0));

        String rowFormat =
                "| %-" + wBrand + "s | %-" + wModel + "s | %-" + wYear + "s | %-" + wFuel + "s | %-" + wPrice + "s |%n";

        String lineSeparator = "─".repeat(wBrand + wModel + wYear + wFuel + wPrice + 16);

        System.out.println("\n📊 FIPE PRICE HISTORY");

        System.out.println(lineSeparator);
        System.out.printf(rowFormat, "BRAND", "MODEL", "YEAR", "FUEL", "PRICE");
        System.out.println(lineSeparator);

        prices.forEach(p -> System.out.printf(
                        rowFormat, p.getBrand(), p.getModel(), p.getModelYear(), p.getFuel(), p.getPrice()));

        System.out.println(lineSeparator);
    }
}
