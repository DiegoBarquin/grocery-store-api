package com.grocerystore.services;

import com.grocerystore.config.PriceConfig;
import com.grocerystore.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private PriceConfig priceConfig;

    @Autowired
    private PriceCalculationService priceCalculationService;

    public String generateReceipt(Order order) {
        StringBuilder receipt = new StringBuilder();

        Map<String, Double> prices = priceConfig.getPrices();

        prices.forEach((product, price) -> {
            switch (product) {
                case "vegetable" -> receipt.append(String.format("Veg €%.2f per 100g,   ", price));
                case "bread" -> receipt.append(String.format("Bread €%.2f,   ", price));
                case "beer" -> receipt.append(String.format("Beer €%.2f per bottle\n\n", price));
            }
        });

        double total = 0;
        for (String product : prices.keySet()) {
            total += calculateProductPrice(order, product, receipt);
        }

        receipt.append(String.format("Total: €%.2f", total));

        return receipt.toString();
    }

    private double calculateProductPrice(Order order, String product, StringBuilder receipt) {
        double price = priceConfig.getPrices().get(product);

        return switch (product) {
            case "bread" -> priceCalculationService.calculateBreadPrice(order, price, receipt);
            case "vegetable" -> priceCalculationService.calculateVegetablePrice(order, price, receipt);
            case "beer" -> priceCalculationService.calculateBeerPrice(order, price, receipt);
            default -> 0;
        };
    }

}