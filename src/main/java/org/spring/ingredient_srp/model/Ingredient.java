package org.spring.ingredient_srp.model;

public class Ingredient {
    // On utilise les Wrappers (Majuscules) partout pour autoriser le null
    private Integer id;
    private String name;
    private Double price;
    private String category;
    private Integer idDish;

    // Constructeur par défaut (Indispensable pour Jackson/Spring)
    public Ingredient() {
    }

    // Constructeur complet : ATTENTION aux types ici (Integer et Double)
    public Ingredient(Integer id, String name, Double price, String category, Integer idDish) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.idDish = idDish;
    }

    // Getters et Setters mis à jour
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getIdDish() {
        return idDish;
    }

    public void setIdDish(Integer idDish) {
        this.idDish = idDish;
    }
}