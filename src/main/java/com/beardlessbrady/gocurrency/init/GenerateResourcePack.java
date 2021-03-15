package com.beardlessbrady.gocurrency.init;

import com.beardlessbrady.gocurrency.ConfigHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

/**
 * Created by BeardlessBrady on 2021-02-28 for Currency-Mod
 * All Rights Reserved
 * https://github.com/Beardlessbrady/Currency-Mod
 */
public class GenerateResourcePack {

    /**
     * Auto generates resource pack for mod
     * @throws IOException
     */
    public static void init() throws IOException {
        createDir("resourcepacks/GOC Resources/assets/gocurrency/models/item");
        createDir("resourcepacks/GOC Resources/assets/gocurrency/textures/items");

        // pack.mcmeta
        writeFile("resourcepacks/GOC Resources/pack.mcmeta", "{\n" +
                "  \"pack\": {\n" +
                "    \"pack_format\": 6,\n" +
                "    \"description\": \"Good Ol' Currency Resource Pack\"\n" +
                "  }\n" +
                "}", false);

        // Generate Resource Pack Icon
        File file = new File("resourcepacks/GOC Resources/pack.png");
        if (!file.exists()) {
            URL resource = GenerateResourcePack.class.getClassLoader().getResource("assets/gocurrency/textures/items/currency_0.png");
            BufferedImage img = ImageIO.read(resource);
            ImageIO.write(img, "png", file);
        }

        // Generate base MODEL json file content
        StringBuilder modelJson = new StringBuilder("{\n" +
                "  \"parent\": \"builtin/generated\",\n" +
                "  \"textures\": {\n" +
                "    \"layer0\": \"gocurrency:items/currency\",\n" +
                "    \"layer1\": \"gocurrency:items/currency\"\n" +
                "  },\n" +
                "  \"__comment\": \"Currency ID's\",\n" +
                "  \"overrides\": [\n");

        for (int i = 0; i < ConfigHandler.configCurrencyName.get().size(); i++) {
            modelJson.append("    { \"predicate\": { \"currency\": ").append(i).append(" }, \"model\": \"gocurrency:item/currency_").append(i).append("\" }");

            if (i != ConfigHandler.configCurrencyName.get().size() - 1) {
                modelJson.append(",\n");
            } else {
                modelJson.append("\n");
            }
        }
        modelJson.append("  ]\n" + "}");


        // Write currency.json MODEL for currency item
        writeFile("resourcepacks/GOC Resources/assets/gocurrency/models/item/currency.json", modelJson.toString(), true);

        // Create custom currency MODELS and TEXTURES
        currencyPredicateModels(ConfigHandler.configCurrencyName.get().size());
        currencyPredicateTexture(ConfigHandler.configCurrencyName.get().size());
    }

    private static void createDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            try {
                if (file.mkdirs()) {
                    System.out.println("Good Ol' Currency: Create directory " + dir);
                } else {
                    System.out.println("Good Ol' Currency: Unable to create directory " + dir);
                }
            } catch (Exception e) {
                System.out.println("Good Ol' Currency: An error occurred.");
                e.printStackTrace();
            }
        }
    }

    private static void writeFile(String filename, String content, boolean overwrite) {
        if (!new File(filename).exists() || overwrite) {
            try {
                FileWriter writePackMCMeta = new FileWriter(filename);
                writePackMCMeta.write(content);
                writePackMCMeta.close();
            } catch (IOException e) {
                System.out.println("Good Ol' Currency: An error occurred.");
                e.printStackTrace();
            }
        }
    }

    private static void currencyPredicateModels(int amount) {
        if (amount < 100) {
            for (int i = 0; i < amount; i++) {
                writeFile("resourcepacks/GOC Resources/assets/gocurrency/models/item/currency_" + i + ".json",
                        "{\n" +
                                "  \"parent\": \"item/generated\",\n" +
                                "  \"textures\": {\n" +
                                "    \"layer0\": \"gocurrency:items/currency_" + i + "\"\n" +
                                "  }\n" +
                                "}", false);
            }
        } else {
            System.out.println("Good Ol' Currency: Error - over 100 custom bills...");
        }
    }

    private static void currencyPredicateTexture(int amount) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        if (amount < 100) {
            File file;
            URL resource;
            BufferedImage img;

            for (int i = 0; i < amount; i++) {
                file = new File("resourcepacks/GOC Resources/assets/gocurrency/textures/items/currency_" + i + ".png");
                if (!file.exists()) {
                    if (i <= 5) { // Default textures
                        resource = GenerateResourcePack.class.getClassLoader().getResource("assets/gocurrency/textures/items/currency_" + i + ".png");
                    } else { // Otherwise use 'no texture' currency
                        resource = GenerateResourcePack.class.getClassLoader().getResource("assets/gocurrency/textures/items/currency.png");
                    }
                    img = ImageIO.read(resource);
                    ImageIO.write(img, "png", file);
                }
            }
        } else {
            System.out.println("Good Ol' Currency: Error - over 100 custom bills...");
        }
    }
}

