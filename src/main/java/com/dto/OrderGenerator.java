package com.dto;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderGenerator {

    public static Order getRandomOrder() {
        Order order = new Order();
        List<String> ingridients = new ArrayList<>();
        ingridients.add("61c0c5a71d1f82001bdaaa6f");
        order.setIngredients(ingridients);
        return order;
    }

}
