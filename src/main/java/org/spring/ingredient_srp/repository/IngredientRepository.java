package org.spring.ingredient_srp.repository;

import org.spring.ingredient_srp.model.CategoryEnum;
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

    public List<Ingredient> findAll() throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT id, name, price, category,id_dish FROM ingredient";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ingredient ing = new Ingredient();
                ing.setId(rs.getInt("id"));
                ing.setName(rs.getString("name"));
                ing.setPrice(rs.getDouble("price"));
                String catStr = rs.getString("category");
                int idDish = rs.getInt("id_dish");
                if (catStr != null) {
                    ing.setCategory(CategoryEnum.valueOf(catStr.trim()));
                }

                if (!rs.wasNull()) { // Vérifie si la valeur n'est pas NULL en SQL
                    ing.setIdDish(idDish);
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