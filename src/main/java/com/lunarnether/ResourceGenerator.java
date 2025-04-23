package com.lunarnether;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class to generate blockstate files, block models, and item models for all blocks
 */
public class ResourceGenerator {

    private static final String RESOURCES_PATH = "src/main/resources/assets/lunarnether/";
    private static final String[] BLOCK_NAMES = {
            "lunar_stone",
            "smooth_lunar_stone",
            "polished_lunar_stone",
            "cut_polished_lunar_stone",
            "lunar_dust",
            "astraith",
            "ilmenite_ore",
            "raw_ilmenite_block",
            "titanium_block",
            "chiseled_titanium",
            "titanium_grate",
            "titanium_door_bottom",
            "titanium_door_top",
            "titanium_trapdoor",
            "cut_titanium"
    };

    public static void main(String[] args) {
        generateBlockstates();
        generateBlockModels();
        generateItemModels();
        System.out.println("Resource generation complete!");
    }

    private static void generateBlockstates() {
        File dir = new File(RESOURCES_PATH + "blockstates");
        dir.mkdirs();

        for (String blockName : BLOCK_NAMES) {
            String content = "{\n" +
                    "  \"variants\": {\n" +
                    "    \"normal\": { \"model\": \"lunarnether:" + blockName + "\" }\n" +
                    "  }\n" +
                    "}";

            writeFile(dir, blockName + ".json", content);
        }
    }

    private static void generateBlockModels() {
        File dir = new File(RESOURCES_PATH + "models/block");
        dir.mkdirs();

        for (String blockName : BLOCK_NAMES) {
            String content = "{\n" +
                    "  \"parent\": \"block/cube_all\",\n" +
                    "  \"textures\": {\n" +
                    "    \"all\": \"lunarnether:blocks/" + blockName + "\"\n" +
                    "  }\n" +
                    "}";

            writeFile(dir, blockName + ".json", content);
        }
    }

    private static void generateItemModels() {
        File dir = new File(RESOURCES_PATH + "models/item");
        dir.mkdirs();

        for (String blockName : BLOCK_NAMES) {
            String content = "{\n" +
                    "  \"parent\": \"lunarnether:block/" + blockName + "\"\n" +
                    "}";

            writeFile(dir, blockName + ".json", content);
        }
    }

    private static void writeFile(File dir, String fileName, String content) {
        try (FileWriter writer = new FileWriter(new File(dir, fileName))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}