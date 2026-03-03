package com.mealmind.backend.dto;

import java.util.List;
import java.util.Map;

public class MenuDtos {

    // what your frontend will consume
    public static class MenuResponse {
        public String date;        // test "03/03/2026"
        public String periodId;    // test "1424"
        public String periodName;  // test "Dinner"
        public List<MealItem> items;

        public MenuResponse(String date, String periodId, String periodName, List<MealItem> items) {
            this.date = date;
            this.periodId = periodId;
            this.periodName = periodName;
            this.items = items;
        }
    }

    public static class MealItem {
        public String id;                 // ProductId
        public String name;               // MarketingName
        public String description;
        public String stationId;
        public String category;           // like "Entrées"
        public String imageUrl;

        public Map<String, String> nutrition; // e.g. {"Calories":"230", "Protein":"33 g", ...}
        public List<String> diets;            // e.g. ["Vegan","Vegetarian","Made Without Gluten"]
        public List<String> allergens;        // e.g. ["Milk","Wheat","Peanuts"]

        public MealItem(
                String id,
                String name,
                String description,
                String stationId,
                String category,
                String imageUrl,
                Map<String, String> nutrition,
                List<String> diets,
                List<String> allergens
        ) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.stationId = stationId;
            this.category = category;
            this.imageUrl = imageUrl;
            this.nutrition = nutrition;
            this.diets = diets;
            this.allergens = allergens;
        }
    }
}
