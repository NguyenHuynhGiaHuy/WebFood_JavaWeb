package com.spring.oshaneat.dto;

public class OrderItemDTO {
    private FoodDTO food;
    private int quantity;
    private int price;

    public FoodDTO getFood() {
        return food;
    }

    public void setFood(FoodDTO food) {
        this.food = food;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
