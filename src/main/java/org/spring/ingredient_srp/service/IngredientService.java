package org.spring.ingredient_srp.service;

import org.spring.ingredient_srp.model.Ingredient;
import org.spring.ingredient_srp.model.StockValue;
import org.spring.ingredient_srp.model.Unit;
import org.spring.ingredient_srp.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> findAll() {
        try {
            return ingredientRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ingrédients", e);
        }
    }

    public Ingredient getIngredientById(Integer id) {
        try {
            Ingredient ingredient = ingredientRepository.findById(id);
            if (ingredient == null) {
                throw new RuntimeException("Ingredient.id=" + id + " is not found");
            }
            return ingredient;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public StockValue getStockValue(Integer id, String atStr, String unitStr) {
        getIngredientById(id);
        Instant at = Instant.parse(atStr);

        try {
            Double quantity = ingredientRepository.getStockQuantityAt(id, at);
            Unit unit = Unit.valueOf(unitStr.toUpperCase());

            return new StockValue(quantity, unit, at);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}