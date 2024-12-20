package com.grocerystore.controller;

import com.grocerystore.config.PriceConfig;
import com.grocerystore.model.Order;
import com.grocerystore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/grocery_store")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PriceConfig priceConfig;

    @Operation(
            summary = "Generate receipt for the order.",
            description = "Calculates the total of the order and generates a receipt with the order details."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Receipt generated successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "string"))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error in calculating the order.",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/generateReceipt")
    public String calculateOrderTotal(@RequestBody @Parameter(description = "Order object containing the order details.",
            required = true) @Valid Order order) {

        return orderService.generateReceipt(order);
    }

    @Operation(summary = "Get discounts list",
            description = "Returns the applicable discount rules for the store.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Discount list retrieved successfully.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error in retrieving the discounts list.",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/getDiscountsList")
    public Map<String, List<Map<String, Object>>> getDiscountRules() {
        return priceConfig.getDiscounts();
    }

    @Operation(summary = "Get current prices.",
            description = "Returns the current prices of the products available in the store.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Price list retrieved successfully.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error in retrieving the price list.",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/getPricesList")
    public Map<String, Double> getCurrentPrices() {
        return priceConfig.getPrices();
    }
}