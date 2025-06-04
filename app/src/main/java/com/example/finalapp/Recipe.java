package com.example.finalapp;


public class Recipe {
    private String title;
    private String ingredients;

    public Recipe(String title, String ingredients) {
        this.title = title;
        this.ingredients = ingredients;
    }

    public String getTitle() {
        return title;
    }

    public String getIngredients() {
        return ingredients;
    }
}
