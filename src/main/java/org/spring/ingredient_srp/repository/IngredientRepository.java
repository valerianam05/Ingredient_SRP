package org.spring.ingredient_srp.repository;

import org.spring.ingredient_srp.config.DBConnection;
import org.spring.ingredient_srp.model.Ingredient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepository {

    public List<Ingredient> findIngredients(int page, int size) throws SQLException {
        String sql = "SELECT id, name, price, category, id_dish FROM ingredient LIMIT ? OFFSET ?";
        List<Ingredient> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, (page - 1) * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public void save(Ingredient ing, Connection conn) throws SQLException {
        String sql = "INSERT INTO ingredient (id, name, price, category, id_dish) VALUES (?, ?, ?, ?::category, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ing.getId());
            ps.setString(2, ing.getName());
            ps.setDouble(3, ing.getPrice());
            ps.setString(4, ing.getCategory());

            if (ing.getIdDish() != null) {
                ps.setInt(5, ing.getIdDish());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            ps.executeUpdate();
        }
    }
    public List<Ingredient> findByCriteria(String name, String cat, String dishName, int page, int size) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT i.id, i.name, i.price, i.category, i.id_dish FROM ingredient i ");
        sql.append("LEFT JOIN dish d ON i.id_dish = d.id WHERE 1=1 ");

        if (name != null) sql.append("AND i.name ILIKE ? ");
        if (cat != null) sql.append("AND i.category = ?::category ");
        if (dishName != null) sql.append("AND d.name ILIKE ? ");
        sql.append("LIMIT ? OFFSET ?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (name != null) ps.setString(idx++, "%" + name + "%");
            if (cat != null) ps.setString(idx++, cat);
            if (dishName != null) ps.setString(idx++, "%" + dishName + "%");
            ps.setInt(idx++, size);
            ps.setInt(idx++, (page - 1) * size);

            List<Ingredient> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
            return list;
        }
    }

    private Ingredient mapRow(ResultSet rs) throws SQLException {
        return new Ingredient(rs.getInt("id"), rs.getString("name"),
                rs.getDouble("price"), rs.getString("category"), rs.getInt("id_dish"));
    }
}