package br.com.fipe.fipepriceexplorer.runner;

import br.com.fipe.fipepriceexplorer.dto.FipeBrandDTO;
import br.com.fipe.fipepriceexplorer.dto.FipeModelDTO;
import br.com.fipe.fipepriceexplorer.dto.VehicleModelsDTO;
import br.com.fipe.fipepriceexplorer.service.FipeService;
import br.com.fipe.fipepriceexplorer.util.ConsoleTablePrinter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class FipeRunner implements CommandLineRunner {

    private final FipeService fipeService;
    private final Scanner sc = new Scanner(System.in);
    private final ConfigurableApplicationContext context;

    public FipeRunner(FipeService fipeService, ConfigurableApplicationContext context) {
        this.fipeService = fipeService;
        this.context = context;
    }

    @Override
    public void run(String... args) {
        boolean running = true;

        while (running) {
            System.out.println("""

                ===============================
                     FIPE PRICE EXPLORER
                ===============================
                Explore vehicle prices using the
                official FIPE database (API v2)

                1 - List all brands
                2 - Search brand by name
                3 - Search brand by code
                0 - Exit
                """);

            System.out.print("Choose an option and press ENTER: ");

            try {
                int option = Integer.parseInt(sc.nextLine().trim());

                switch (option) {
                    case 1 -> listAndProcessBrands();
                    case 2 -> searchAndProcessBrand();
                    case 3 -> processVehicleSelection();
                    case 0 -> {
                        System.out.println("\n👋 Exiting application. See you next time!");
                        running = false;
                    }
                    default -> System.out.println("\n⚠️ Invalid option. Please choose 0, 1 or 2.");
                }

            } catch (NumberFormatException e) {
                System.out.println("\n⚠️ Invalid input. Please enter a numeric option.");
            } catch (RuntimeException e) {
                System.out.println("\n🚨 " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n❌ Unexpected error. Please try again.");
            }
        }
        sc.close();
        context.close();
    }

    private void searchAndProcessBrand() {
        try {
            System.out.print("\n🔍 Enter brand name (or part of it): ");
            String brandName = sc.nextLine().trim();

            if (brandName.isBlank()) {
                System.out.println("\n⚠️ Brand name cannot be empty.");
                return;
            }

            List<FipeBrandDTO> brands = fipeService.listCarBrands();

            List<FipeBrandDTO> filteredBrands = brands.stream()
                    .filter(b -> b.getName().toUpperCase().contains(brandName.toUpperCase()))
                    .toList();

            if (filteredBrands.isEmpty()) {
                System.out.println("\n⚠️ No brands found matching your search.");
                return;
            }

            System.out.println("\n✅ Brands found:");
            filteredBrands.forEach(
                    b -> System.out.println(b.getCode() + " - " + b.getName().toUpperCase()));

            processVehicleSelection();

        } catch (RuntimeException e) {
            System.out.println("\n🚨 " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ Unexpected error while searching brands.");
        }
    }

    private void listAndProcessBrands() {
        try {
            List<FipeBrandDTO> brands = fipeService.listCarBrands();

            if (brands.isEmpty()) {
                System.out.println("\n⚠️ No brands available at the moment.");
                return;
            }

            System.out.println("\n📋 Available brands:\n");

            brands.stream()
                    .sorted(Comparator.comparing(FipeBrandDTO::getName))
                    .map(b -> b.getCode() + " - " + b.getName().toUpperCase())
                    .forEach(System.out::println);

            processVehicleSelection();

        } catch (RuntimeException e) {
            System.out.println("\n🚨 " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ Unexpected error while listing brands.");
        }
    }

    private void processVehicleSelection() {
        try {
            System.out.print("\n➡️ Enter the brand code: ");
            String brandCode = sc.nextLine().trim();

            if (brandCode.isBlank()) {
                System.out.println("\n⚠️ Brand code cannot be empty.");
                return;
            }

            System.out.print("\n➡️ Enter vehicle year (YYYY) or press ENTER to list all models: ");
            String yearInput = sc.nextLine().trim();

            if (yearInput.isBlank()) {
                displayAllModels(brandCode);
            } else {
                displayModelsByYear(brandCode, yearInput);
            }

            System.out.print("\n➡️ Enter the model code: ");
            String modelCode = sc.nextLine().trim();

            if (modelCode.isBlank()) {
                System.out.println("\n⚠️ Model code cannot be empty.");
                return;
            }

            displayPrices(brandCode, modelCode, yearInput);

        } catch (RuntimeException e) {
            System.out.println("\n🚨 " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ Unexpected error during vehicle selection.");
        }
    }

    private void displayPrices(String brandCode, String modelCode, String yearInput) {
        try {
            Optional<String> yearOpt = yearInput.isBlank() ? Optional.empty() : Optional.of(yearInput);

            var prices = fipeService.listPricesForAllYears(brandCode, modelCode, yearOpt);

            if (prices.isEmpty()) {
                System.out.println("\n⚠️ No price data found for this selection.");
                return;
            }

            ConsoleTablePrinter.printPriceTable(prices);

        } catch (RuntimeException e) {
            System.out.println("\n🚨 " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ Unexpected error while fetching prices.");
        }
    }

    private void displayModelsByYear(String brandCode, String yearInput) {
        try {
            System.out.println("\n📌 Models available for year " + yearInput + ":\n");

            List<VehicleModelsDTO> models = fipeService.listModelsByBrandAndYear(brandCode, yearInput);

            if (models.isEmpty()) {
                System.out.println("\n⚠️ No models found for this year.");
                return;
            }

            models.stream()
                    .sorted(Comparator.comparing(VehicleModelsDTO::modelCode))
                    .map(m -> m.modelCode() + " - " + m.modelDetails().toUpperCase())
                    .forEach(System.out::println);

        } catch (RuntimeException e) {
            System.out.println("\n🚨 " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ Unexpected error while listing models by year.");
        }
    }

    private void displayAllModels(String brandCode) {
        try {
            System.out.println("\n📌 All available models:\n");

            List<FipeModelDTO> models = fipeService.listModelsByBrand(brandCode);

            if (models.isEmpty()) {
                System.out.println("\n⚠️ No models found for this brand.");
                return;
            }

            models.stream()
                    .sorted(Comparator.comparing(FipeModelDTO::getName))
                    .map(m -> m.getCode() + " - " + m.getName().toUpperCase())
                    .forEach(System.out::println);

        } catch (RuntimeException e) {
            System.out.println("\n🚨 " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ Unexpected error while listing models.");
        }
    }
}
