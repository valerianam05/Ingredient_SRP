package org.spring.ingredient_srp.service;

import org.spring.ingredient_srp.model.Dish;
import org.spring.ingredient_srp.model.Ingredient;
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



    public List<Dish> getAllDishes() throws SQLException {
        return ingredientRepository.findAllDishes();
    }

    public List<Ingredient> getAllIngredients() throws SQLException {
        return ingredientRepository.findAllIngredients();
    }

    public Ingredient getIngredientById(Integer id) throws SQLException {
        return ingredientRepository.findById(id);
    }

    public Object getStockValue(Integer id, String at, String unit) throws SQLException {
        Instant instantAt = Instant.parse(at);

        Double quantity = ingredientRepository.getStockQuantityAt(id, instantAt);

        return new Object() {
            public final Double valeur = quantity;
            public final String unite = unit;
            public final String date = at;
        };
    }
}