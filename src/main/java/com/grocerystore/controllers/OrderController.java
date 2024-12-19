package com.grocerystore.controllers;

import com.grocerystore.config.PriceConfig;
import com.grocerystore.models.Order;
import com.grocerystore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/grocery_store")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PriceConfig priceConfig;

    @PostMapping("/generateReceipt")
    public String calculateOrderTotal(@RequestBody Order order) {
        return orderService.generateReceipt(order);
    }

    @GetMapping("/getDiscountsList")
    public Map<String, List<Map<String, Object>>> getDiscountRules() {
        return priceConfig.getDiscounts();
    }

    @GetMapping("/getPricesList")
    public Map<String, Double> getCurrentPrices() {
        return priceConfig.getPrices();
    }
}