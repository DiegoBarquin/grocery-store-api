package com.grocerystore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "grocery-store")
public class PriceConfig {

    private Map<String, Double> prices;
    private Map<String, List<Map<String, Object>>> discounts;

    public Map<String, Double> getPrices() {
        return prices;
    }

    public Map<String, List<Map<String, Object>>> getDiscounts() {
        return discounts;
    }

    public void setPrices(Map<String, Double> prices) {
        this.prices = prices;
    }

    public void setDiscounts(Map<String, List<Map<String, Object>>> discounts) {
        this.discounts = discounts;
    }

}