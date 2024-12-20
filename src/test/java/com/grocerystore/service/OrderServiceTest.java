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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private PriceConfig priceConfig;

    @Mock
    private PriceCalculationService priceCalculationService;

    @InjectMocks
    private OrderService orderService;

    private Order order;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Bread bread = new Bread(3,1);
        List<Bread> breads = List.of(bread);
        Vegetable vegetable = new Vegetable("Tomato", 200);
        List<Vegetable> vegetables = List.of(vegetable);
        Beer beer = new Beer("IPA",7);
        List<Beer> beers = List.of(beer);

        order = new Order(
                breads,
                vegetables,
                beers
        );
    }

    @Test
    public void givenOrder_whenCalculateReceipt_thenReturnFormattedReceipt() {
        String expectedReceipt = "Bread €1,50, Veg €2,00 per 100g, Beer €3,00 per bottle\n\n\n       Total: €6,50";

        Map<String, Double> prices = new LinkedHashMap<>();
        prices.put("bread", 1.5);
        prices.put("vegetable", 2.0);
        prices.put("beer", 3.0);

        when(priceConfig.getPrices()).thenReturn(prices);
        when(priceCalculationService.calculateBreadPrice(any(Order.class), eq(1.5), any(StringBuilder.class), any()))
                .thenReturn(1.5);
        when(priceCalculationService.calculateVegetablePrice(any(Order.class), eq(2.0), any(StringBuilder.class), any()))
                .thenReturn(2.0);
        when(priceCalculationService.calculateBeerPrice(any(Order.class), eq(3.0), any(StringBuilder.class), any()))
                .thenReturn(3.0);

        String receipt = orderService.generateReceipt(order);

        verify(priceConfig, times(1)).getPrices();
        verify(priceCalculationService, times(1))
                .calculateBreadPrice(any(Order.class), eq(1.5), any(StringBuilder.class), any());
        verify(priceCalculationService, times(1))
                .calculateVegetablePrice(any(Order.class), eq(2.0), any(StringBuilder.class), any());
        verify(priceCalculationService, times(1))
                .calculateBeerPrice(any(Order.class), eq(3.0), any(StringBuilder.class), any());
        assertEquals(expectedReceipt, receipt);
    }

}
