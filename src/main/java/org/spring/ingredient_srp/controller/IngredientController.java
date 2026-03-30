package org.spring.ingredient_srp.controller;

import org.spring.ingredient_srp.model.Ingredient;
import org.spring.ingredient_srp.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientService ingredientService = new IngredientService();

    @GetMapping
    public List<Ingredient> getIngredients(@RequestParam int page, @RequestParam int size) {
        try {
            return ingredientService.getIngredients(page, size);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la récupération");
        }
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public void createIngredients(@RequestBody List<Ingredient> ingredients) {
        ingredientService.createIngredients(ingredients);
    }

    @GetMapping("/search")
    public List<Ingredient> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String dishName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            return ingredientService.findByCriteria(name, category, dishName, page, size);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}