package org.spring.ingredient_srp.model;

import java.time.Instant;

public class StockMovement {
    private Integer id;
    private Integer idIngredient;
    private Double quantity;
    private MovementType type;
    private Instant creationDatetime;

    public StockMovement() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdIngredient() { return idIngredient; }
    public void setIdIngredient(Integer idIngredient) { this.idIngredient = idIngredient; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public MovementType getType() { return type; }
    public void setType(MovementType type) { this.type = type; }

    public Instant getCreationDatetime() { return creationDatetime; }
    public void setCreationDatetime(Instant creationDatetime) { this.creationDatetime = creationDatetime; }
}
