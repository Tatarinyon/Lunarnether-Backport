package com.lunarnether;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class RecipeGenerator {

    private static final String MODID = "lunarnether";
    private static final String OUTPUT_FOLDER = "src/main/resources/assets/lunarnether/recipes/";

    public static void main(String[] args) {
        generateStairsRecipe("lunar_stone", "lunar_stone_stairs");
        generateStairsRecipe("smooth_lunar_stone", "smooth_lunar_stone_stairs");
        generateStairsRecipe("polished_lunar_stone", "polished_lunar_stone_stairs");
        generateStairsRecipe("cut_polished_lunar_stone", "cut_polished_lunar_stone_stairs");
        generateStairsRecipe("titanium_block", "titanium_stairs");
        generateStairsRecipe("chiseled_titanium", "chiseled_titanium_stairs");
        generateStairsRecipe("cut_titanium", "cut_titanium_stairs");

        generateWallRecipe("lunar_stone", "lunar_stone_wall");
        generateWallRecipe("smooth_lunar_stone", "smooth_lunar_stone_wall");
        generateWallRecipe("polished_lunar_stone", "polished_lunar_stone_wall");
        generateWallRecipe("titanium_block", "titanium_wall");
        generateWallRecipe("cut_titanium", "cut_titanium_wall");

        generateSlabRecipe("lunar_stone", "lunar_stone_slab");
        generateSlabRecipe("smooth_lunar_stone", "smooth_lunar_stone_slab");
        generateSlabRecipe("polished_lunar_stone", "polished_lunar_stone_slab");
        generateSlabRecipe("titanium_block", "titanium_slab");
        generateSlabRecipe("cut_titanium", "cut_titanium_slab");

        System.out.println("Tarif dosyaları oluşturuldu!");
    }

    private static void generateStairsRecipe(String input, String output) {
        String json = "{\n" +
                "  \"type\": \"minecraft:crafting_shaped\",\n" +
                "  \"pattern\": [\n" +
                "    \"#  \",\n" +
                "    \"## \",\n" +
                "    \"###\"\n" +
                "  ],\n" +
                "  \"key\": {\n" +
                "    \"#\": { \"item\": \"" + MODID + ":" + input + "\" }\n" +
                "  },\n" +
                "  \"result\": {\n" +
                "    \"item\": \"" + MODID + ":" + output + "\",\n" +
                "    \"count\": 4\n" +
                "  }\n" +
                "}";

        writeToFile(output + ".json", json);
    }

    private static void generateWallRecipe(String input, String output) {
        String json = "{\n" +
                "  \"type\": \"minecraft:crafting_shaped\",\n" +
                "  \"pattern\": [\n" +
                "    \"###\",\n" +
                "    \"###\"\n" +
                "  ],\n" +
                "  \"key\": {\n" +
                "    \"#\": { \"item\": \"" + MODID + ":" + input + "\" }\n" +
                "  },\n" +
                "  \"result\": {\n" +
                "    \"item\": \"" + MODID + ":" + output + "\",\n" +
                "    \"count\": 6\n" +
                "  }\n" +
                "}";

        writeToFile(output + ".json", json);
    }

    private static void generateSlabRecipe(String input, String output) {
        String json = "{\n" +
                "  \"type\": \"minecraft:crafting_shaped\",\n" +
                "  \"pattern\": [\n" +
                "    \"###\"\n" +
                "  ],\n" +
                "  \"key\": {\n" +
                "    \"#\": { \"item\": \"" + MODID + ":" + input + "\" }\n" +
                "  },\n" +
                "  \"result\": {\n" +
                "    \"item\": \"" + MODID + ":" + output + "\",\n" +
                "    \"count\": 6\n" +
                "  }\n" +
                "}";

        writeToFile(output + ".json", json);
    }

    private static void writeToFile(String fileName, String content) {
        try {
            File folder = new File(OUTPUT_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            FileWriter writer = new FileWriter(new File(folder, fileName));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
