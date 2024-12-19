package com.grocerystore.services;

import com.grocerystore.config.PriceConfig;
import com.grocerystore.models.Beer;
import com.grocerystore.models.Bread;
import com.grocerystore.models.Order;
import com.grocerystore.models.Vegetable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PriceCalculationService {

    @Autowired
    private PriceConfig priceConfig;

    public double calculateVegetablePrice(Order order, double vegetablePricePer100g, StringBuilder receipt) {
        double vegetableFinalPrice = 0;
        int totalWeight = 0;

        for (Vegetable veg : order.getVegetables()) {
            totalWeight += (int) veg.weightInGrams();
        }
        
        if(totalWeight > 0){
            double discount = 0;
            for (Map<String, Object> rule : priceConfig.getDiscounts().get("vegetable")) {
                if (totalWeight > (int) rule.get("weightOver")){
                    discount = (double) rule.get("discount");
                }
            }

            double vegetableBasePrice  =  vegetablePricePer100g * ((double) totalWeight /100);
            vegetableFinalPrice = vegetableBasePrice - ( vegetableBasePrice  * discount);

            receipt.append(String.format("%sg Vegetable €%.2f\n", totalWeight, vegetableFinalPrice));
        }
        
        return vegetableFinalPrice;
    }

    public double calculateBeerPrice(Order order, double beerPricePerBottle, StringBuilder receipt) {
        double beerFinalPrice = 0;
        double beerTotalPrice = 0;

        for (Beer beer : order.getBeers()) {
            int quantity = beer.quantity();
            String type = beer.type();

            if (type == null || type.isEmpty()) {
                throw new IllegalArgumentException("Beer name cannot be null or empty.");
            }

            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity must be non-negative.");
            }

            Double packPrice = null;

            for (Map<String, Object> rule : priceConfig.getDiscounts().get("beer")) {
                if (rule.get("type").equals(type)){
                    packPrice = (double) rule.get("packPrice");
                    break;
                }
            }

            if (quantity >= 6 && packPrice != null) {
                int packs = quantity / 6;
                int extraBeer = quantity % 6;
                beerFinalPrice = (extraBeer * beerPricePerBottle) + (packs * packPrice);
            } else {
                beerFinalPrice = quantity * beerPricePerBottle;
            }

            beerTotalPrice += beerFinalPrice;

            receipt.append(String.format("%s x %s Beers %.2f€\n", quantity, type, beerFinalPrice));
        }

        return beerTotalPrice;
    }

    public double calculateBreadPrice(Order order, double breadPrice, StringBuilder receipt) {
        double breadFinalPrice = 0;
        double breadsTotalPrice = 0;

        for (Bread bread : order.getBreads()) {
            int quantity = bread.quantity();
            int ageInDays = bread.ageInDays();

            int discount = 1;
            String description = "";

            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity must be non-negative.");
            }

            if(ageInDays > 6){
                throw new IllegalArgumentException("Bread too old to sell");
            }else if(ageInDays <= 1){
                breadFinalPrice = breadPrice * quantity;
                breadsTotalPrice += breadFinalPrice;
            }else{
                for (Map<String, Object> rule : priceConfig.getDiscounts().get("bread")) {
                    if ( rule.get("ageInDays").equals(ageInDays)) {
                        discount = (int) rule.get("discount");
                        description = " ("+ rule.get("description") +")";
                    }
                }

                breadFinalPrice = (breadPrice * (quantity / discount)) + (breadPrice * (quantity % discount));
                breadsTotalPrice += breadFinalPrice;
            }
            receipt.append(String.format("%s x Bread%s %.2f€\n", quantity, description, breadFinalPrice));
        }

        return breadsTotalPrice;
    }

}
