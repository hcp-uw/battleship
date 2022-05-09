package battleship;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

/**
 * A class representing the settings that a game will be set up with
 */
public class GameSettings {
    public static List<SimpleEntry<String, List<String>>> ENUM_OPTIONS = List.of(
            new SimpleEntry<>("mode", List.of("2player", "cpu")),
            new SimpleEntry<>("boardsize", List.of("5", "6", "7", "8", "9", "10")),
            new SimpleEntry<>("cpudifficulty", List.of("easy", "normal")),
            new SimpleEntry<>("playernames", List.of()) // empty list represents free-form input

//            new SimpleEntry<>()
    );

    private final Map<String, String> choices;
    private final Iterator<SimpleEntry<String, List<String>>> optionSetIter;

    public GameSettings() {
        this.choices = new HashMap<>();
        this.optionSetIter = ENUM_OPTIONS.iterator();
    }

    public SimpleEntry<String, List<String>> getAvailableChoices() {
        return this.optionSetIter.next();
    }

    public void setChoice(String op, String val) {
        this.choices.put(op, val);
    }

    public String getSetting(String setting) {
        String out = this.choices.get(setting);
        if (out == null) {
            return "";
        }
        return out;
    }
}
