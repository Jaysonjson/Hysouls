package json.jayson.hysouls.datagen;

import json.jayson.hysouls.essence_attribute.EssenceAttribute;
import json.jayson.hysouls.essence_attribute.EssenceAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EssenceAttributeUIGen {

    public static void main(String[] args) {
        for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
            try {
                Path file = Paths.get("src/main/resources/Common/UI/Custom/" + value.getLevelPageUIPath());
                Path parent = Paths.get("ui_templates/LevelPageEssenceAttribute.ui");

                Files.createDirectories(file.getParent());
                Files.writeString(file, value.asLevelPageUI(Files.readString(parent)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
