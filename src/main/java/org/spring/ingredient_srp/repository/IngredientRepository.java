package org.spring.ingredient_srp.repository;

import org.spring.ingredient_srp.model.CategoryEnum;
import org.spring.ingredient_srp.model.Dish;
import org.spring.ingredient_srp.model.Ingredient;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IngredientRepository {
    private final DataSource dataSource;

    public IngredientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void updateDishIngredients(int dishId, List<Ingredient> ingredients) throws SQLException {
        String detachSql = "UPDATE ingredient SET id_dish = NULL WHERE id_dish = ?";
        String attachSql = "UPDATE ingredient SET id_dish = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false); // Mode transaction pour la sécurité
            try {
                try (PreparedStatement ps1 = conn.prepareStatement(detachSql)) {
                    ps1.setInt(1, dishId);
                    ps1.executeUpdate();
                }

                if (ingredients != null && !ingredients.isEmpty()) {
                    try (PreparedStatement ps2 = conn.prepareStatement(attachSql)) {
                        for (Ingredient ing : ingredients) {
                            ps2.setInt(1, dishId);
                            ps2.setInt(2, ing.getId());
                            ps2.executeUpdate();
                        }
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }


    public List<Dish> findAllDishes() throws SQLException {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT id, name, dish_type, price FROM dish";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt("id"));
                dish.setName(rs.getString("name"));
                dish.setDishType(rs.getString("dish_type"));
                dish.setPrice(rs.getDouble("price"));

                // Appel de la méthode qui était manquante
                dish.setIngredients(this.findIngredientsByDishId(dish.getId()));

                dishes.add(dish);
            }
        }
        return dishes;
    }

    public List<Ingredient> findIngredientsByDishId(Integer dishId) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT id, name, price, category FROM ingredient WHERE id_dish = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dishId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ingredient ing = new Ingredient();
                    ing.setId(rs.getInt("id"));
                    ing.setName(rs.getString("name"));
                    ing.setPrice(rs.getDouble("price"));
                    String catStr = rs.getString("category");
                    if (catStr != null) {
                        ing.setCategory(CategoryEnum.valueOf(catStr.trim()));
                    }
                    ingredients.add(ing);
                }
            }
        }
        return ingredients;
    }

    public List<Ingredient> findAllIngredients() throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT id, name, price, category, id_dish FROM ingredient";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ingredient ing = new Ingredient();
                ing.setId(rs.getInt("id"));
                ing.setName(rs.getString("name"));
                ing.setPrice(rs.getDouble("price"));
                ing.setIdDish(rs.getInt("id_dish"));
                String catStr = rs.getString("category");
                if (catStr != null) {
                    ing.setCategory(CategoryEnum.valueOf(catStr.trim()));
                }
                ingredients.add(ing);
            }
        }
        return ingredients;
    }

    public Ingredient findById(Integer id) throws SQLException {
        String sql = "SELECT id, name, price, category FROM ingredient WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ingredient ing = new Ingredient();
                    ing.setId(rs.getInt("id"));
                    ing.setName(rs.getString("name"));
                    ing.setPrice(rs.getDouble("price"));
                    String catStr = rs.getString("category");
                    if (catStr != null) {
                        ing.setCategory(CategoryEnum.valueOf(catStr.trim()));
                    }
                    return ing;
                }
            }
        }
        return null;
    }

    public Double getStockQuantityAt(Integer id, Instant at) throws SQLException {
        String sql = """
            SELECT SUM(CASE WHEN type = 'OUT' THEN -quantity ELSE quantity END) as actual_quantity 
            FROM stock_movement 
            WHERE id_ingredient = ? AND creation_datetime <= ?
        """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setTimestamp(2, Timestamp.from(at));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("actual_quantity");
                }
            }
        }
        return 0.0;
    }
}