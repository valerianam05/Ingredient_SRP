package org.spring.ingredient_srp.model;

public class Ingredient {
    private Integer id;
    private String name;
    private Double price;
    private Integer idDish;
    private CategoryEnum category;

    public Ingredient() {
    }

    public Ingredient(Integer id, String name, Double price, Integer idDish, CategoryEnum category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.idDish = idDish;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getIdDish() {
        return idDish;
    }

    public void setIdDish(Integer idDish) {
        this.idDish = idDish;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }
}

