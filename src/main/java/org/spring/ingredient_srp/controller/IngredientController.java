package org.spring.ingredient_srp.controller;

import org.spring.ingredient_srp.model.Ingredient;
import org.spring.ingredient_srp.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAll() throws SQLException {
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ingredientService.getIngredientById(id));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<?> getStock(
            @PathVariable Integer id,
            @RequestParam(required = false) String at,
            @RequestParam(required = false) String unit) {

        if (at == null || unit == null) {
            return new ResponseEntity<>(
                    "Either mandatory query parameter 'at' or 'unit' is not provided.",
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            return ResponseEntity.ok(ingredientService.getStockValue(id, at, unit));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}