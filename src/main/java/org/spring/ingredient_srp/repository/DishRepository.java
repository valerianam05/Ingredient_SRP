package org.spring.ingredient_srp.repository;

import org.spring.ingredient_srp.model.CategoryEnum;
import org.spring.ingredient_srp.model.Dish;
import org.spring.ingredient_srp.model.Ingredient;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class DishRepository {

    private final DataSource dataSource;

    public DishRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Dish findDishById(Integer id) throws SQLException {
        String sql = """
            SELECT d.id, d.name as dish_name, d.dish_type, 
                   i.id as ing_id, i.name as ing_name, i.price, i.category
            FROM dish d
            LEFT JOIN ingredient i ON d.id = i.id_dish
            WHERE d.id = ?
        """;
        Dish dish = null;
        try (Connection conn = dataSource.getConnection();
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
                        Ingredient ing = new Ingredient();
                        ing.setId(rs.getInt("ing_id"));
                        ing.setName(rs.getString("ing_name"));
                        ing.setPrice(rs.getDouble("price"));

                        String catStr = rs.getString("category");
                        if (catStr != null) {
                            ing.setCategory(CategoryEnum.valueOf(catStr.trim()));
                        }

                        dish.getIngredients().add(ing);
                    }
                }
            }
        }
        return dish;
    }

    public void upsert(Dish dish, Connection conn) throws SQLException {
        String sql = "INSERT INTO dish (id, name, dish_type) VALUES (COALESCE(?, nextval('dish_id_seq')), ?, ?::dish_type) " +
                "ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, dish_type = EXCLUDED.dish_type";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (dish.getId() == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, dish.getId());
            }

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

    public List<Dish> findByIngredientName(String ingName) throws SQLException {
        String sql = "SELECT DISTINCT d.id, d.name, d.dish_type FROM dish d " +
                "JOIN ingredient i ON d.id = i.id_dish WHERE i.name ILIKE ?";
        List<Dish> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
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

    public void updateDishIngredients(int dishId, List<Ingredient> ingredients) throws SQLException {
        String detachSql = "UPDATE ingredient SET id_dish = NULL WHERE id_dish = ?";
        String attachSql = "UPDATE ingredient SET id_dish = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false); // Transaction pour éviter les erreurs partielles
            try {
                // Détacher les anciens
                try (PreparedStatement ps1 = conn.prepareStatement(detachSql)) {
                    ps1.setInt(1, dishId);
                    ps1.executeUpdate();
                }
                // Attacher les nouveaux fournis dans le JSON
                try (PreparedStatement ps2 = conn.prepareStatement(attachSql)) {
                    for (Ingredient ing : ingredients) {
                        ps2.setInt(1, dishId);
                        ps2.setInt(2, ing.getId());
                        ps2.executeUpdate();
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public List<Dish> findFiltered(Double pMax, Double pMin, String n, Connection conn) throws SQLException {
        String sql = "SELECT id, name, dish_type, price FROM dish WHERE 1=1";

        if (pMax != null) { sql += " AND price < ?"; }
        if (pMin != null) { sql += " AND price > ?"; }
        if (n != null)    { sql += " AND name ILIKE ?"; }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int index = 1;

            if (pMax != null) { pstmt.setDouble(index++, pMax); }
            if (pMin != null) { pstmt.setDouble(index++, pMin); }
            if (n != null)    { pstmt.setString(index++, "%" + n + "%"); }

            List<Dish> results = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Dish(rs.getInt("id"), rs.getString("name"), rs.getDouble("price")));
                }
            }
            return results;
        }

    }
    public boolean existsByName(String name, Connection conn) throws SQLException {
        String sql = "SELECT COUNT(id) FROM dish WHERE name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }



}