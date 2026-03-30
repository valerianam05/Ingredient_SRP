package org.spring.ingredient_srp.model;

import java.time.Instant;

public class StockValue {
    private Double valeur;
    private Unit unite;
    private Instant date;

    public StockValue() {}

    public StockValue(Double valeur, Unit unite, Instant date) {
        this.valeur = valeur;
        this.unite = unite;
        this.date = date;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Unit getUnite() {
        return unite;
    }

    public void setUnite(Unit unite) {
        this.unite = unite;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}