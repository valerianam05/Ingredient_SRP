package org.spring.ingredient_srp.repository;

import org.spring.ingredient_srp.config.DBConnection;
import org.spring.ingredient_srp.model.Dish;
import org.spring.ingredient_srp.model.Ingredient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishRepository {

    // a) Récupérer un plat ET ses ingrédients (Jointure)
    public Dish findDishById(Integer id) throws SQLException {
        String sql = """
            SELECT d.id, d.name as dish_name, d.dish_type, 
                   i.id as ing_id, i.name as ing_name, i.price, i.category
            FROM dish d
            LEFT JOIN ingredient i ON d.id = i.id_dish
            WHERE d.id = ?
        """;
        Dish dish = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (dish == null) {
                        dish = new Dish();
                        dish.setId(rs.getInt("id"));
                        dish.setName(rs.getString("dish_name"));
                        dish.setDishType(rs.getString("dish_type"));
                        dish.setIngredients(new ArrayList<>());
                    }
                    if (rs.getInt("ing_id") != 0) {
                        dish.getIngredients().add(new Ingredient(
                                rs.getInt("ing_id"), rs.getString("ing_name"),
                                rs.getDouble("price"), rs.getString("category"), id));
                    }
                }
            }
        }
        return dish;
    }

    // d) Upsert du plat
    public void upsert(Dish dish, Connection conn) throws SQLException {
        String sql = "INSERT INTO dish (id, name, dish_type) VALUES (?, ?, ?::dish_type) " +
                "ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, dish_type = EXCLUDED.dish_type";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dish.getId());
            ps.setString(2, dish.getName());
            ps.setString(3, dish.getDishType());
            ps.executeUpdate();
        }
    }

    public void dissociateAllIngredients(int dishId, Connection conn) throws SQLException {
        String sql = "UPDATE ingredient SET id_dish = NULL WHERE id_dish = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dishId);
            ps.executeUpdate();
        }
    }

    public void associateIngredients(int dishId, List<Ingredient> ingredients, Connection conn) throws SQLException {
        String sql = "UPDATE ingredient SET id_dish = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Ingredient ing : ingredients) {
                ps.setInt(1, dishId);
                ps.setInt(2, ing.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }


    // e) Recherche de plats par nom d'ingrédient
    public List<Dish> findByIngredientName(String ingName) throws SQLException {
        String sql = "SELECT DISTINCT d.id, d.name, d.dish_type FROM dish d " +
                "JOIN ingredient i ON d.id = i.id_dish WHERE i.name ILIKE ?";
        List<Dish> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + ingName + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Dish d = new Dish();
                    d.setId(rs.getInt("id"));
                    d.setName(rs.getString("name"));
                    d.setDishType(rs.getString("dish_type"));
                    list.add(d);
                }
            }
        }
        return list;
    }
}