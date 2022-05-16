package battleship;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

/**
 * A class representing the settings that a game will be set up with
 */
public class GameSettings {
    public static List<SimpleEntry<String, List<String>>> ENUM_OPTIONS = List.of(
            new SimpleEntry<>("mode", List.of("2player", "cpu")),
            new SimpleEntry<>("boardsize", List.of("5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15")),
            new SimpleEntry<>("cpudifficulty", List.of("easy", "normal")),
            new SimpleEntry<>("playernames", List.of()) // empty list represents free-form input

//            new SimpleEntry<>()
    );

    private final Map<String, String> choices;
    private final Iterator<SimpleEntry<String, List<String>>> optionSetIter;
    private int curSettingIndex;

    public GameSettings() {
        this.choices = new HashMap<>();
        this.optionSetIter = ENUM_OPTIONS.iterator();
        this.curSettingIndex = 0;
    }

    /**
     * gets the current choice that has yet to be set in this GameSettings object
     * @return an entry (key, value) where key is the option and value is a list of possibilities
     */
    public SimpleEntry<String, List<String>> getAvailableChoices() {
        if (this.curSettingIndex < ENUM_OPTIONS.size())
        return ENUM_OPTIONS.get(this.curSettingIndex);
        return null;
    }

    /**
     * sets the current choice that has yet to be set (next choice in list), then updates the "current option"
     * to be the next optioin
     * @param val the string value to set the option to
     */
    public void setCurChoice(String val) {
        this.choices.put(getAvailableChoices().getKey(), val);
        this.curSettingIndex++;
    }

    public void setChoice(String op, String val) {
        assert !val.equals("") : "cannot set an option to empty";
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
