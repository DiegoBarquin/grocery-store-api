package com.grocerystore.service;

import com.grocerystore.config.PriceConfig;
import com.grocerystore.model.Beer;
import com.grocerystore.model.Bread;
import com.grocerystore.model.Order;
import com.grocerystore.model.Vegetable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

            receipt.append(String.format("%d. %-30s €%3.2f\n",
                    lineCounter.get(),
                    String.format("%sg Vegetable", totalWeight),
                    vegetableFinalPrice));

        }

        return vegetableFinalPrice;
    }

    public double calculateBeerPrice(Order order, double beerPricePerBottle, StringBuilder receipt, Supplier<Integer> lineCounter) {
        double beerTotalPrice = 0;

        for (Beer beer : order.getBeers()) {
            int quantity = beer.quantity();
            String type = beer.type();

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

            receipt.append(String.format("%d. %-30s €%3.2f\n",
                    lineCounter.get(),
                    String.format("%s x %s x Beers", quantity, type),
                    beerFinalPrice));
        }

        return beerTotalPrice;
    }

    public double calculateBreadPrice(Order order, double breadPrice, StringBuilder receipt, Supplier<Integer> lineCounter) {
        double breadsTotalPrice = 0;

        for (Bread bread : order.getBreads()) {
            int quantity = bread.quantity();
            int ageInDays = bread.ageInDays();

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

            receipt.append(String.format("%d. %-30s €%3.2f\n",
                    lineCounter.get(),
                    String.format("%s x Bread%s", quantity, description),
                    breadFinalPrice));
        }

        return breadsTotalPrice;
    }

}
