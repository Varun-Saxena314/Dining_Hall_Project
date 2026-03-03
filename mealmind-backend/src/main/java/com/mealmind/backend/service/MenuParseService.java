package com.mealmind.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.mealmind.backend.dto.MenuDtos;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

@Service
public class MenuParseService {

    private final ObjectMapper objectMapper = new ObjectMapper();

public MenuDtos.MenuResponse parseMenu(String json) {
    try {
        JsonNode root = objectMapper.readTree(json);
        return toMenuResponse(root);
    } catch (Exception e) {
        throw new RuntimeException("Failed to parse CampusDish response", e);
    }
}

    public MenuDtos.MenuResponse toMenuResponse(JsonNode root) {
        String date = text(root, "Date");
        String periodId = text(root, "SelectedPeriodId");

        String periodName = findPeriodName(root, periodId);

        List<MenuDtos.MealItem> items = new ArrayList<>();

        JsonNode products = root.path("Menu").path("MenuProducts");
        if (products.isArray()) {
            for (JsonNode mp : products) {
                JsonNode p = mp.path("Product");
                if (p.isMissingNode() || p.isNull()) continue;

                String id = text(p, "ProductId");
                String name = text(p, "MarketingName");
                if (name == null || name.isBlank()) continue;

                String description = text(p, "ShortDescription");
                String imageUrl = text(p, "ImageUrl");

                String stationId = text(mp, "StationId");

                String category = null;
                JsonNode categories = p.path("Categories");
                if (categories.isArray() && categories.size() > 0) {
                    category = text(categories.get(0), "DisplayName");
                }

                Map<String, String> nutrition = parseNutrition(p);

                List<String> diets = parseDiets(p);

                List<String> allergens = parseAllergens(p);

                items.add(new MenuDtos.MealItem(
                        id, name, description, stationId, category, imageUrl, nutrition, diets, allergens
                ));
            }
        }

        return new MenuDtos.MenuResponse(date, periodId, periodName, items);
    }

    private Map<String, String> parseNutrition(JsonNode product) {
        Map<String, String> out = new LinkedHashMap<>();

        // some fields exist directly
        String calories = text(product, "Calories");
        if (calories != null && !calories.isBlank()) out.put("Calories", calories);

        String servingSize = text(product, "ServingSize");
        String servingUnit = text(product, "ServingUnit");
        if (servingSize != null && servingUnit != null) out.put("Serving Size", servingSize + " " + servingUnit);

        // most of the good stuff is here
        JsonNode tree = product.path("NutritionalTree");
        if (tree.isArray()) {
            for (JsonNode n : tree) {
                String nName = text(n, "Name");   // "Protein"
                String value = text(n, "Value");  // "33"
                String unit = text(n, "Unit");    // "g"

                if (nName == null || value == null) continue;

                String formatted = value;
                if (unit != null && !unit.isBlank()) formatted = value + " " + unit;

                out.put(nName, formatted);

                // also include sub-items like Saturated Fat
                JsonNode sub = n.path("SubList");
                if (sub.isArray()) {
                    for (JsonNode sn : sub) {
                        String snName = text(sn, "Name");
                        String snVal = text(sn, "Value");
                        String snUnit = text(sn, "Unit");
                        if (snName == null || snVal == null) continue;
                        String snFormatted = snVal;
                        if (snUnit != null && !snUnit.isBlank()) snFormatted = snVal + " " + snUnit;
                        out.put(snName, snFormatted);
                    }
                }
            }
        }

        return out;
    }

    private List<String> parseDiets(JsonNode product) {
        List<String> diets = new ArrayList<>();

        // sometimes exists as icons list
        JsonNode di = product.path("DietaryInformation");
        if (di.isArray()) {
            for (JsonNode d : di) {
                String name = text(d, "Name");
                Boolean enabled = bool(d, "IsEnabled");
                if (name != null && (enabled == null || enabled)) diets.add(name);
            }
        }

        // also infer from AvailableFilters if present
        JsonNode filters = product.path("AvailableFilters");
        if (!filters.isMissingNode() && filters.isObject()) {
            addIfTrue(filters, "IsVegan", "Vegan", diets);
            addIfTrue(filters, "IsVegetarian", "Vegetarian", diets);
            addIfTrue(filters, "IsGlutenFree", "Made Without Gluten", diets);
        }

        // remove duplicates
        return dedupeKeepOrder(diets);
    }

    private List<String> parseAllergens(JsonNode product) {
        List<String> allergens = new ArrayList<>();

        JsonNode filters = product.path("AvailableFilters");
        if (!filters.isMissingNode() && filters.isObject()) {
            addIfTrue(filters, "ContainsMilk", "Milk", allergens);
            addIfTrue(filters, "ContainsWheat", "Wheat", allergens);
            addIfTrue(filters, "ContainsPeanuts", "Peanuts", allergens);
            addIfTrue(filters, "ContainsEggs", "Eggs", allergens);
            addIfTrue(filters, "ContainsSoy", "Soy", allergens);
            addIfTrue(filters, "ContainsFish", "Fish", allergens);
            addIfTrue(filters, "ContainsShellfish", "Shellfish", allergens);
            addIfTrue(filters, "ContainsTreeNuts", "Tree Nuts", allergens);
            addIfTrue(filters, "ContainsSesame", "Sesame", allergens);
        }

        return dedupeKeepOrder(allergens);
    }

    private void addIfTrue(JsonNode obj, String key, String label, List<String> out) {
        JsonNode v = obj.get(key);
        if (v != null && v.isBoolean() && v.booleanValue()) out.add(label);
    }

    private List<String> dedupeKeepOrder(List<String> in) {
        return new ArrayList<>(new LinkedHashSet<>(in));
    }

    private String findPeriodName(JsonNode root, String periodId) {
        JsonNode periods = root.path("Menu").path("MenuPeriods");
        if (periods.isArray()) {
            for (JsonNode p : periods) {
                if (Objects.equals(text(p, "PeriodId"), periodId)) {
                    String name = text(p, "Name");
                    if (name != null) return name;
                }
            }
        }
        return null;
    }

    private String text(JsonNode node, String field) {
        JsonNode v = node.get(field);
        if (v == null || v.isNull()) return null;
        return v.asText();
    }

    private Boolean bool(JsonNode node, String field) {
        JsonNode v = node.get(field);
        if (v == null || v.isNull()) return null;
        return v.asBoolean();
    }
}
