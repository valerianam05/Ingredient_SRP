package org.spring.ingredient_srp.controller;

import org.spring.ingredient_srp.exception.BadRequestException;
import org.spring.ingredient_srp.model.Dish;
import org.spring.ingredient_srp.model.Ingredient;
import org.spring.ingredient_srp.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {
    @Autowired

    private DishService service;
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/{id}")
    public Dish getDishById(@PathVariable Integer id) {
        Dish dish = dishService.getDishById(id);
        if (dish == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plat non trouvé");
        }
        return dish;
    }

//    @PostMapping
//    public ResponseEntity<Dish> saveDish(@RequestBody Dish dish) {
//        try {
//            Dish savedDish = dishService.saveDish(dish);
//            return new ResponseEntity<>(savedDish, HttpStatus.CREATED);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }

    @GetMapping("/search")
    public List<Dish> getByIngredient(@RequestParam String ingredientName) {
        return dishService.getDishesByIngredient(ingredientName);
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> updateDishIngredients(
            @PathVariable int id,
            @RequestBody List<Ingredient> ingredients) {

        if (ingredients == null) {
            return ResponseEntity.status(400).body("Le corps de la requête est obligatoire.");
        }

        try {
            dishService.updateIngredients(id, ingredients);
            return ResponseEntity.ok("Association mise à jour.");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(404).body("Dish.id=" + id + " is not found");
            }
            return ResponseEntity.status(500).body(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> getDishes(
            @RequestParam(required = false) Double priceUnder,
            @RequestParam(required = false) Double priceOver,
            @RequestParam(required = false) String name) {
        try {
            return ResponseEntity.ok(dishService.getFiltered(priceUnder, priceOver, name));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> postDishes(@RequestBody List<Dish> list) {
        try {
            return ResponseEntity.status(201).body(service.createAll(list));
        } catch (BadRequestException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}