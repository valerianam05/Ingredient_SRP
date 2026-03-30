package org.spring.ingredient_srp.service;

import org.spring.ingredient_srp.model.Ingredient;
import org.spring.ingredient_srp.repository.IngredientRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.spring.ingredient_srp.config.DBConnection;

public class IngredientService {
    private final IngredientRepository ingredientRepository = new IngredientRepository();

    public List<Ingredient> getIngredients(int page, int size) throws SQLException {
        return ingredientRepository.findIngredients(page, size);
    }

    public void createIngredients(List<Ingredient> ingredients) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                for (Ingredient ing : ingredients) {
                    ingredientRepository.save(ing, conn);
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Échec atomique : Opération annulée.", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion", e);
        }
    }

    public List<Ingredient> findByCriteria(String name, String cat, String dishName, int page, int size) throws SQLException {
        return ingredientRepository.findByCriteria(name, cat, dishName, page, size);
    }
}