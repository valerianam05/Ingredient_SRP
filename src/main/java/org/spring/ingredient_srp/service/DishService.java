package org.spring.ingredient_srp.service;

import org.spring.ingredient_srp.exception.BadRequestException;
import org.spring.ingredient_srp.model.Dish;
import org.spring.ingredient_srp.model.Ingredient;
import org.spring.ingredient_srp.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishService {
    @Autowired

    private final DishRepository dishRepository;
    private final DataSource dataSource;

    public DishService(DishRepository dishRepository, DataSource dataSource) {
        this.dishRepository = dishRepository;
        this.dataSource = dataSource;
    }

    public Dish getDishById(Integer id) {
        try {
            return dishRepository.findDishById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du plat", e);
        }
    }

    public Dish saveDish(Dish dish) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                dishRepository.upsert(dish, conn);

                dishRepository.dissociateAllIngredients(dish.getId(), conn);
                if (dish.getIngredients() != null && !dish.getIngredients().isEmpty()) {
                    dishRepository.associateIngredients(dish.getId(), dish.getIngredients(), conn);
                }

                conn.commit();
                return dish;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erreur lors de la sauvegarde transactionnelle du plat", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données", e);
        }
    }

    public List<Dish> getDishesByIngredient(String ingredientName) {
        try {
            return dishRepository.findByIngredientName(ingredientName);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par ingrédient", e);
        }
    }

    public void updateIngredients(int id, List<Ingredient> ingredients) throws SQLException {
        Dish dish = dishRepository.findDishById(id);
        if (dish == null) {
            throw new RuntimeException("Dish.id=" + id + " is not found");
        }

        try {
            dishRepository.updateDishIngredients(id, ingredients);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL : " + e.getMessage());
        }
    }


    public List<Dish> createAll(List<Dish> dishes) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            List<Dish> saved = new ArrayList<>();

            for (Dish d : dishes) {
                if (dishRepository.existsByName(d.getName(), conn)) {
                    throw new BadRequestException("Dish.name=" + d.getName() + " already exists");
                }

                dishRepository.upsert(d, conn);

                saved.add(d);
            }
            return saved;
        }
    }

    public List<Dish> getFiltered(Double pMax, Double pMin, String name) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return dishRepository.findFiltered(pMax, pMin, name, conn);
        }
    }
}