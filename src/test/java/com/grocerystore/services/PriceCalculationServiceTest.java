package com.grocerystore.services;

import com.grocerystore.models.Bread;
import com.grocerystore.models.Order;
import com.grocerystore.models.Vegetable;
import com.grocerystore.services.PriceCalculationService;
import com.grocerystore.config.PriceConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.aot.hint.TypeReference.listOf;

class PriceCalculationServiceTest {

    @Mock
    private PriceConfig priceConfig;

    @InjectMocks
    private PriceCalculationService priceCalculationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Map<String, Object> discountRuleVegetablesOver500 = new HashMap<>();
        discountRuleVegetablesOver500.put("weightOver", 500);
        discountRuleVegetablesOver500.put("discount", 0.1);
        Map<String, Object> discountRuleVegetablesOver300 = new HashMap<>();
        discountRuleVegetablesOver300.put("weightOver", 300);
        discountRuleVegetablesOver300.put("discount", 0.07);
        Map<String, Object> discountRuleVegetablesOver100 = new HashMap<>();
        discountRuleVegetablesOver100.put("weightOver", 100);
        discountRuleVegetablesOver100.put("discount", 0.05);

        Map<String, List<Map<String, Object>>> discounts = new HashMap<>();
        discounts.put("vegetable",
                Arrays.asList(
                        discountRuleVegetablesOver500,
                        discountRuleVegetablesOver300,
                        discountRuleVegetablesOver100));


        when(priceConfig.getDiscounts()).thenReturn(discounts);
    }

    @Test
    void givenOrderWithVegetables_whenCalculateVegetablePrice_thenReturnCorrectFinalPrice() {
        StringBuilder receipt = new StringBuilder();

        double vegetablePricePer100g = 1.0;
        double expectedFinalPrice = 3.8;

        Vegetable vegetable1 = new Vegetable("Carrot",200);
        Vegetable vegetable2 = new Vegetable("Tomato", 200);
        List<Vegetable> vegetables = Arrays.asList(vegetable1, vegetable2);

        Order order = new Order(
                List.of(),
                vegetables,
                List.of());

        double finalPrice = priceCalculationService.calculateVegetablePrice(order, vegetablePricePer100g, receipt);

        assertEquals(expectedFinalPrice, finalPrice);
        assertTrue(receipt.toString().contains("400g Vegetable €3,80"));
    }

    @Test
    void givenOrderWithNoVegetables_whenCalculateVegetablePrice_thenReturnZero() {
        StringBuilder receipt = new StringBuilder();

        double vegetablePricePer100g = 2.0;

        Order order = new Order(
                List.of(),
                List.of(),
                List.of()
        );

        double finalPrice = priceCalculationService.calculateVegetablePrice(order, vegetablePricePer100g, receipt);

        assertEquals(0.0, finalPrice);
        assertTrue(receipt.toString().isEmpty());
    }

    @Test
    void givenOrderWithNoBeer_whenCalculateBeerPrice_thenReturnZero() {
        StringBuilder receipt = new StringBuilder();

        double beerPricePerBottle = 0.5;

        Order order = new Order(
                List.of(),
                List.of(),
                List.of()
        );

        double finalPrice = priceCalculationService.calculateBeerPrice(order, beerPricePerBottle, receipt);

        assertEquals(0.0, finalPrice);
        assertTrue(receipt.toString().isEmpty());
    }

    @Test
    void givenOrderWithBread_whenCalculateBreadPrice_thenReturnCorrectFinalPrice() {
        StringBuilder receipt = new StringBuilder();

        double vegetablePricePer100g = 1.0;
        double expectedFinalPrice = 3.8;

        Bread bread = new Bread(3,3);
        List<Bread> breads = Arrays.asList(bread);

        Order order = new Order(
                breads,
                List.of(),
                List.of());

        double finalPrice = priceCalculationService.calculateVegetablePrice(order, vegetablePricePer100g, receipt);

        assertEquals(expectedFinalPrice, finalPrice);
        assertTrue(receipt.toString().contains("400g Vegetable €3,80"));
    }

    @Test
    void givenOrderWithNoBread_whenCalculateBreadPrice_thenReturnZero() {
        StringBuilder receipt = new StringBuilder();

        double breadPrice = 1;

        Order order = new Order(
                List.of(),
                List.of(),
                List.of()
        );

        double finalPrice = priceCalculationService.calculateBreadPrice(order, breadPrice, receipt);

        assertEquals(0.0, finalPrice);
        assertTrue(receipt.toString().isEmpty());
    }

}
