package school.hei.spring_srp_project.entity;

import java.util.List;

public record Dish(Integer id, String name, Double price, List<Ingredient> ingredients) {}