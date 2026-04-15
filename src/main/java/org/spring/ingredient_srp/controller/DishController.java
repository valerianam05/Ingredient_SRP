package org.spring.ingredient_srp.controller;

import org.spring.ingredient_srp.exception.BadRequestException;
import org.spring.ingredient_srp.model.Dish;
import org.spring.ingredient_srp.model.Ingredient;
import org.spring.ingredient_srp.service.DishService;
import org.spring.ingredient_srp.service.IngredientService;
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

    private final IngredientService ingredientService;
    private DishService service;
    private final DishService dishService;

    public DishController(DishService dishService, IngredientService ingredientService) {
        this.dishService = dishService;
        this.ingredientService = ingredientService;
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
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDishIngredients(
            @PathVariable int id,
            @RequestBody List<Ingredient> ingredients) { // Tu reçois une liste

        if (ingredients == null) {
            return ResponseEntity.status(400).body("Le corps de la requête est obligatoire.");
        }

        try {
            Dish tempDish = new Dish();

            tempDish.setIngredients(ingredients);

            dishService.updateComplete(id, tempDish);

            return ResponseEntity.ok("Association mise à jour.");
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                return ResponseEntity.status(404).body("Dish.id=" + id + " is not found");
            }
            return ResponseEntity.status(500).body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Erreur SQL : " + e.getMessage());
        }
    }



    @GetMapping
    public ResponseEntity<?> getDishes() {
        try {
            return ResponseEntity.ok(ingredientService.getAllDishes());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
        }
    }

//    @PostMapping
//    public ResponseEntity<?> postDishes(@RequestBody List<Dish> list) {
//        try {
//            return ResponseEntity.status(201).body(ingredientService.list);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(e.getMessage());
//        }
//    }
}