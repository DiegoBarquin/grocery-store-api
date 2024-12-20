package com.grocerystore.service;

import com.grocerystore.config.PriceConfig;
import com.grocerystore.model.Beer;
import com.grocerystore.model.Bread;
import com.grocerystore.model.Order;
import com.grocerystore.model.Vegetable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class PriceCalculationServiceTest {

    @Mock
    private PriceConfig priceConfig;

    @Mock
    private Supplier<Integer> lineCounter;

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

        Map<String, Object> discountRuleBread3DaysOld = new HashMap<>();
        discountRuleBread3DaysOld.put("ageInDays", 3);
        discountRuleBread3DaysOld.put("discount", 2);
        discountRuleBread3DaysOld.put("description", "three days old");
        Map<String, Object> discountRuleBread6DaysOld = new HashMap<>();
        discountRuleBread6DaysOld.put("ageInDays", 6);
        discountRuleBread6DaysOld.put("discount", 3);
        discountRuleBread6DaysOld.put("description", "six days old");

        Map<String, Object> discountRuleBelgiumBeer = new HashMap<>();
        discountRuleBelgiumBeer.put("type", "Belgium");
        discountRuleBelgiumBeer.put("packPrice", 3.0);
        Map<String, Object> discountRuleDutchBeer = new HashMap<>();
        discountRuleDutchBeer.put("type", "Dutch");
        discountRuleDutchBeer.put("packPrice", 2.0);
        Map<String, Object> discountRuleGermanBeer = new HashMap<>();
        discountRuleGermanBeer.put("type", "German");
        discountRuleGermanBeer.put("packPrice", 4.0);

        Map<String, List<Map<String, Object>>> discounts = new HashMap<>();
        discounts.put("vegetable",
                Arrays.asList(
                        discountRuleVegetablesOver500,
                        discountRuleVegetablesOver300,
                        discountRuleVegetablesOver100));
        discounts.put("bread",
                Arrays.asList(
                        discountRuleBread3DaysOld,
                        discountRuleBread6DaysOld));
        discounts.put("beer",
                Arrays.asList(
                        discountRuleBelgiumBeer,
                        discountRuleDutchBeer,
                        discountRuleGermanBeer));

        when(priceConfig.getDiscounts()).thenReturn(discounts);
        when(lineCounter.get()).thenReturn(1).thenReturn(2);
    }

    @Test
    void givenOrderWithVegetables_whenCalculateVegetablePrice_thenReturnCorrectPrice() {
        StringBuilder receipt = new StringBuilder();
        String expectedReceipt = " 1.    400g Vegetable                 €3,80\n";
        double vegetablePricePer100g = 1.0;
        double expectedFinalPrice = 3.8;

        Vegetable vegetable1 = new Vegetable("Carrot",200);
        Vegetable vegetable2 = new Vegetable("Tomato", 200);
        List<Vegetable> vegetables = Arrays.asList(vegetable1, vegetable2);

        Order order = new Order(
                List.of(),
                vegetables,
                List.of());

        double finalPrice = priceCalculationService.calculateVegetablePrice(order, vegetablePricePer100g, receipt, lineCounter);

        assertEquals(expectedFinalPrice, finalPrice);
        assertEquals(expectedReceipt, receipt.toString());
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

        double finalPrice = priceCalculationService.calculateVegetablePrice(order, vegetablePricePer100g, receipt, lineCounter);

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

        double finalPrice = priceCalculationService.calculateBeerPrice(order, beerPricePerBottle, receipt, lineCounter);

        assertEquals(0.0, finalPrice);
        assertTrue(receipt.toString().isEmpty());
    }

    @Test
    void givenOrderWithBeerWithDiscounts_whenCalculateBeerPrice_thenReturnCorrectPrice() {
        StringBuilder receipt = new StringBuilder();
        String expectedReceipt = " 1.    7 x German Beers               €4,50\n";

        double beerPricePerBottle = 0.5;
        double expectedFinalPrice = 4.5;

        Beer beer = new Beer("German",7);
        List<Beer> beers = List.of(beer);

        Order order = new Order(
                List.of(),
                List.of(),
                beers);

        double finalPrice = priceCalculationService.calculateBeerPrice(order, beerPricePerBottle, receipt, lineCounter);

        assertEquals(expectedFinalPrice, finalPrice);
        assertEquals(expectedReceipt, receipt.toString());
    }

    @Test
    void givenOrderWithBeerWithNoDiscounts_whenCalculateBeerPrice_thenReturnCorrectPrice() {
        StringBuilder receipt = new StringBuilder();
        String expectedReceipt = " 1.    7 x IPA Beers                  €3,50\n";

        double beerPricePerBottle = 0.5;
        double expectedFinalPrice = 3.5;

        Beer beer = new Beer("IPA",7);
        List<Beer> beers = List.of(beer);

        Order order = new Order(
                List.of(),
                List.of(),
                beers);

        double finalPrice = priceCalculationService.calculateBeerPrice(order, beerPricePerBottle, receipt, lineCounter);

        assertEquals(expectedFinalPrice, finalPrice);
        assertEquals(expectedReceipt, receipt.toString());
    }

    @Test
    void givenOrderWithMultipleEntriesOfDifferentBeers_whenCalculateBeerPrice_thenReturnCorrectFinalPrice() {
        StringBuilder receipt = new StringBuilder();
        String expectedReceipt = " 1.    7 x Belgium Beers              €3,50\n 2.    9 x Dutch Beers                €3,50\n";

        double beerPricePerBottle = 0.5;
        double expectedPrice = 7;

        Beer belgiumBeer = new Beer("Belgium",7);
        Beer duchtBeer = new Beer("Dutch",9);
        List<Beer> beers = List.of(belgiumBeer, duchtBeer);

        Order order = new Order(
                List.of(),
                List.of(),
                beers);

        double finalPrice = priceCalculationService.calculateBeerPrice(order, beerPricePerBottle, receipt, lineCounter);

        assertEquals(expectedPrice, finalPrice);
        assertEquals(expectedReceipt, receipt.toString());
    }

    @Test
    void givenOrderWithMultipleEntriesOfSameBeer_whenCalculateBeerPrice_thenReturnCorrectPrice() {
        StringBuilder receipt = new StringBuilder();
        String expectedReceipt = " 1.    6 x German Beers               €4,00\n";

        double beerPricePerBottle = 0.5;
        double expectedPrice = 4;

        Beer firstBeerEntry = new Beer("German",3);
        Beer secondBeerEntry = new Beer("German",3);
        List<Beer> beers = List.of(firstBeerEntry, secondBeerEntry);

        Order order = new Order(
                List.of(),
                List.of(),
                beers);

        double finalPrice = priceCalculationService.calculateBeerPrice(order, beerPricePerBottle, receipt, lineCounter);

        assertEquals(expectedPrice, finalPrice);
        assertEquals(expectedReceipt, receipt.toString());
    }

    @Test
    void givenOrderWithBreadOfOneDay_whenCalculateBreadPrice_thenReturnCorrectPrice() {
        StringBuilder receipt = new StringBuilder();
        String expectedReceipt = " 1.    3 x Bread                      €3,00\n";

        double breadPrice = 1.0;
        double expectedFinalPrice = 3;

        Bread bread = new Bread(3,1);
        List<Bread> breads = List.of(bread);

        Order order = new Order(
                breads,
                List.of(),
                List.of());

        double finalPrice = priceCalculationService.calculateBreadPrice(order, breadPrice, receipt, lineCounter);

        assertEquals(expectedFinalPrice, finalPrice);
        assertEquals(expectedReceipt, receipt.toString());
    }

    @Test
    void givenOrderWithMultipleEntriesOfSameBreads_whenCalculateBreadPrice_thenReturnCorrectPrice() {
        StringBuilder receipt = new StringBuilder();
        String expectedReceipt = " 1.    4 x Bread (three days old)     €2,00\n";

        double breadPrice = 1.0;
        double expectedFinalPrice = 2;

        Bread firstBreadEntry = new Bread(3,3);
        Bread secondBreadEntry = new Bread(1,3);
        List<Bread> breads = List.of(firstBreadEntry, secondBreadEntry);

        Order order = new Order(
                breads,
                List.of(),
                List.of());

        double finalPrice = priceCalculationService.calculateBreadPrice(order, breadPrice, receipt, lineCounter);

        assertEquals(expectedFinalPrice, finalPrice);
        assertEquals(expectedReceipt, receipt.toString());
    }

    @Test
    void givenOrderWithMultipleEntriesOfDifferentBreads_whenCalculateBreadPrice_thenReturnCorrectPrice() {
        StringBuilder receipt = new StringBuilder();
        String expectedReceipt = " 1.    3 x Bread (three days old)     €2,00\n 2.    5 x Bread (six days old)       €3,00\n";

        double breadPrice = 1.0;
        double expectedFinalPrice = 5;

        Bread firstBreadEntry = new Bread(3,3);
        Bread secondBreadEntry = new Bread(5,6);
        List<Bread> breads = List.of(firstBreadEntry, secondBreadEntry);

        Order order = new Order(
                breads,
                List.of(),
                List.of());

        double finalPrice = priceCalculationService.calculateBreadPrice(order, breadPrice, receipt, lineCounter);

        assertEquals(expectedFinalPrice, finalPrice);
        assertEquals(expectedReceipt, receipt.toString());
    }

    @Test
    void givenOrderWithBreadOfTreeDays_whenCalculateBreadPrice_thenReturnCorrectPrice() {
        StringBuilder receipt = new StringBuilder();
        String expectedReceipt = " 1.    3 x Bread (three days old)     €2,00\n";

        double breadPrice = 1.0;
        double expectedFinalPrice = 2;

        Bread bread = new Bread(3,3);
        List<Bread> breads = List.of(bread);

        Order order = new Order(
                breads,
                List.of(),
                List.of());

        double finalPrice = priceCalculationService.calculateBreadPrice(order, breadPrice, receipt, lineCounter);

        assertEquals(expectedFinalPrice, finalPrice);
        assertEquals(expectedReceipt, receipt.toString());
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

        double finalPrice = priceCalculationService.calculateBreadPrice(order, breadPrice, receipt, lineCounter);

        assertEquals(0.0, finalPrice);
        assertTrue(receipt.toString().isEmpty());
    }

}
