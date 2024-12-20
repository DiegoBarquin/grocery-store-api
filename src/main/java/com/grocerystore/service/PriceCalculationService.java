package com.grocerystore.service;

import com.grocerystore.config.PriceConfig;
import com.grocerystore.model.Beer;
import com.grocerystore.model.Bread;
import com.grocerystore.model.Order;
import com.grocerystore.model.Vegetable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class PriceCalculationService {

    @Autowired
    private PriceConfig priceConfig;

    public double calculateVegetablePrice(Order order, double vegetablePricePer100g, StringBuilder receipt, Supplier<Integer> lineCounter) {
        double vegetableFinalPrice = 0;
        int totalWeight = 0;

        for (Vegetable veg : order.getVegetables()) {
            totalWeight += (int) veg.weightInGrams();
        }
        
        if(totalWeight > 0) {
            double discount = 0;
            for (Map<String, Object> rule : priceConfig.getDiscounts().get("vegetable")) {
                if (totalWeight > (int) rule.get("weightOver")) {
                    discount = (double) rule.get("discount");
                }
            }

            double vegetableBasePrice = vegetablePricePer100g * ((double) totalWeight / 100);
            vegetableFinalPrice = vegetableBasePrice - (vegetableBasePrice * discount);

            addLineToReceipt(receipt, lineCounter, String.format("%sg Vegetable", totalWeight), vegetableFinalPrice);

        }

        return vegetableFinalPrice;
    }

    public double calculateBeerPrice(Order order, double beerPricePerBottle, StringBuilder receipt, Supplier<Integer> lineCounter) {
        double beerTotalPrice = 0;

        Map<String, Integer> beerQuantityByType = new HashMap<>();
        for (Beer beer : order.getBeers()) {
            beerQuantityByType.merge(beer.type(), beer.quantity(), Integer::sum);
        }

        for (Map.Entry<String, Integer> entry : beerQuantityByType.entrySet()) {
            String type = entry.getKey();
            int quantity = entry.getValue();

            Double packPrice = null;

            for (Map<String, Object> rule : priceConfig.getDiscounts().get("beer")) {
                if (rule.get("type").equals(type)){
                    packPrice = (double) rule.get("packPrice");
                    break;
                }
            }
            double beerFinalPrice;
            if (quantity >= 6 && packPrice != null) {
                int packs = quantity / 6;
                int extraBeer = quantity % 6;
                beerFinalPrice = (extraBeer * beerPricePerBottle) + (packs * packPrice);
            } else {
                beerFinalPrice = quantity * beerPricePerBottle;
            }

            beerTotalPrice += beerFinalPrice;

            addLineToReceipt(receipt, lineCounter, String.format("%s x %s Beers", quantity, type), beerFinalPrice);
        }

        return beerTotalPrice;
    }

    public double calculateBreadPrice(Order order, double breadPrice, StringBuilder receipt, Supplier<Integer> lineCounter) {
        double breadsTotalPrice = 0;

        Map<Integer, Integer> breadQuantityByAge = new HashMap<>();
        for (Bread bread : order.getBreads()) {
            breadQuantityByAge.merge(bread.ageInDays(), bread.quantity(), Integer::sum);
        }

        for (Map.Entry<Integer, Integer> entry : breadQuantityByAge.entrySet()) {
            int ageInDays = entry.getKey();
            int quantity = entry.getValue();

            int discount = 1;
            String description = "";

            double breadFinalPrice;

            if(ageInDays <= 1){
                breadFinalPrice = breadPrice * quantity;
            }else{
                for (Map<String, Object> rule : priceConfig.getDiscounts().get("bread")) {
                    if ( rule.get("ageInDays").equals(ageInDays)) {
                        discount = (int) rule.get("discount");
                        description = " ("+ rule.get("description") +")";
                    }
                }

                breadFinalPrice = (breadPrice * (quantity / discount)) + (breadPrice * (quantity % discount));
            }
            breadsTotalPrice += breadFinalPrice;

            addLineToReceipt(receipt, lineCounter, String.format("%s x Bread%s", quantity, description), breadFinalPrice);
        }

        return breadsTotalPrice;
    }

    private void addLineToReceipt(StringBuilder receipt, Supplier<Integer> lineCounter, String description, double price){
        receipt.append(String.format("%2d.%4s%-30s €%3.2f\n",
                lineCounter.get(),
                "",
                description,
                price));
    }

}
