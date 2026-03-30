package org.spring.ingredient_srp.controller;

import org.spring.ingredient_srp.model.Dish;
import org.spring.ingredient_srp.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishService dishService = new DishService();

    @GetMapping("/{id}")
    public Dish getDishById(@PathVariable Integer id) {
        try {
            Dish dish = dishService.getDishById(id);
            if (dish == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plat non trouvé");
            }
            return dish;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping
    public Dish saveDish(@RequestBody Dish dish) {
        return dishService.saveDish(dish);
    }

    @GetMapping("/search-by-ingredient")
    public List<Dish> getByIngredient(@RequestParam String name) {
        try {
            return dishService.getDishesByIngredient(name);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
