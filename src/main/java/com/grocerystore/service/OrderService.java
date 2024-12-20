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

    private int lineCounter;

    public String generateReceipt(Order order) {
        StringBuilder receipt = new StringBuilder();
        lineCounter = 0;

        Map<String, Double> prices = priceConfig.getPrices();

        prices.forEach((product, price) -> {
            switch (product) {
                case "vegetable" -> receipt.append(String.format("Veg €%.2f per 100g,   ", price));
                case "bread" -> receipt.append(String.format("Bread €%.2f,   ", price));
                case "beer" -> receipt.append(String.format("Beer €%.2f per bottle", price));
            }
        });

        receipt.append("\n\n");

        double total = 0;
        for (String product : prices.keySet()) {
            total += calculateProductPrice(order, product, receipt);
        }

        receipt.append(String.format("\nTotal: €%.2f", total));

        return receipt.toString();
    }

    private double calculateProductPrice(Order order, String product, StringBuilder receipt) {
        double price = priceConfig.getPrices().get(product);

        return switch (product) {
            case "bread" -> priceCalculationService.calculateBreadPrice(order, price, receipt, this::incrementCounter);
            case "vegetable" -> priceCalculationService.calculateVegetablePrice(order, price, receipt, this::incrementCounter);
            case "beer" -> priceCalculationService.calculateBeerPrice(order, price, receipt, this::incrementCounter);
            default -> 0;
        };
    }

    private int incrementCounter() {
        return ++lineCounter;
    }
}