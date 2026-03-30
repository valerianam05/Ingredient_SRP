package org.spring.ingredient_srp.service;

import org.spring.ingredient_srp.model.Dish;
import org.spring.ingredient_srp.repository.DishRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.spring.ingredient_srp.config.DBConnection;

public class DishService {
    private final DishRepository dishRepository = new DishRepository();

    public Dish getDishById(Integer id) throws SQLException {
        return dishRepository.findDishById(id);
    }

    public Dish saveDish(Dish dish) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                dishRepository.upsert(dish, conn);

                dishRepository.dissociateAllIngredients(dish.getId(), conn);

                if (dish.getIngredients() != null) {
                    dishRepository.associateIngredients(dish.getId(), dish.getIngredients(), conn);
                }

                conn.commit();
                return dish;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la sauvegarde du plat", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Dish> getDishesByIngredient(String ingredientName) throws SQLException {
        return dishRepository.findByIngredientName(ingredientName);
    }
}