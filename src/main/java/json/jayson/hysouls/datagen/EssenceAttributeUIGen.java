package json.jayson.hysouls.datagen;

import json.jayson.hysouls.essence_attribute.EssenceAttribute;
import json.jayson.hysouls.essence_attribute.EssenceAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class EssenceAttributeUIGen {

    public static void main(String[] args) throws IOException {
        for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
            Path file = Paths.get("src/main/resources/Common/UI/Custom/" + value.getLevelPageUIPath());
            Path parent = Paths.get("ui_templates/LevelPageEssenceAttribute.ui");

            Files.createDirectories(file.getParent());
            Files.writeString(file, value.asLevelPageUI(Files.readString(parent)));
        }

        Path file = Path.of("src/main/resources/Common/UI/Custom/Pages/LevelUpPage.ui");
        List<String> lines = Files.readAllLines(file);
        int heightLine = 7;
        lines.set(heightLine - 1, "Anchor: (Width: 665, Height: " + (250 + 60 * EssenceAttributes.getAttributeMap().size()) + ");");
        Files.write(file, lines);
    }

}
