package com.grocerystore.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Order {

    private List<Bread> breads;
    private List<Vegetable> vegetables;
    private List<Beer> beers;

    @JsonCreator
    public Order(
            @JsonProperty("breads") List<Bread> breads,
            @JsonProperty("vegetables") List<Vegetable> vegetables,
            @JsonProperty("beers") List<Beer> beers
    ) {
        this.breads = breads;
        this.vegetables = vegetables;
        this.beers = beers;
    }

    public List<Bread> getBreads() {
        return breads;
    }

    public void setBreads(List<Bread> breads) {
        this.breads = breads;
    }

    public List<Vegetable> getVegetables() {
        return vegetables;
    }

    public void setVegetables(List<Vegetable> vegetables) {
        this.vegetables = vegetables;
    }

    public List<Beer> getBeers() {
        return beers;
    }

    public void setBeers(List<Beer> beers) {
        this.beers = beers;
    }

}
