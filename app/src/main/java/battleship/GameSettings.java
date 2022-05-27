package battleship;

import utils.Triple;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

/**
 * A class representing the settings that a game will be set up with
 */
public class GameSettings {
    public enum OptionType {
        CHOICES,    // read out all the choices as a list of options
        RANGE,      // INTEGERS - take the first two elements of the list and use those as upper and lower bounds
        TEXTENTRY   // Empty list - allow for free-form text entry
    }

    // TODO: put the option names themselves into enum?
    public static List<Triple<String, OptionType, List<String>>> ENUM_OPTIONS = List.of(
            new Triple<>("mode", OptionType.CHOICES, List.of("2player", "cpu")),
            new Triple<>("board size", OptionType.RANGE, List.of("5", "15")),
            new Triple<>("cpu difficulty", OptionType.CHOICES, List.of("easy", "normal", "difficult")),
            new Triple<>("player names", OptionType.TEXTENTRY, List.of()) // empty list represents free-form input
    );

    private final Map<String, String> choices;
//    private final Iterator<SimpleEntry<String, List<String>>> optionSetIter;
    private int curSettingIndex;

    public GameSettings() {
        this.choices = new HashMap<>();
//        this.optionSetIter = ENUM_OPTIONS.iterator();
        this.curSettingIndex = 0;
    }

    /**
     * gets the current choice that has yet to be set in this GameSettings object
     * @return an entry (key, value) where key is the option and value is a list of possibilities
     */
    public Triple<String, OptionType, List<String>> getAvailableChoices() {
        if (!isComplete()) {
            // user no longer prompted for cpu settings if cpu mode not selected
            if (ENUM_OPTIONS.get(this.curSettingIndex).getFirst().equals("cpu difficulty")
                    && !this.choices.get("mode").equals("cpu")){
                this.curSettingIndex++;
            }
            return ENUM_OPTIONS.get(this.curSettingIndex);
        }
        return null;
    }

    /**
     * Sets the current choice that has yet to be set (next choice in list), then updates the "current option"
     * to be the next option. This should be preferred.
     * @param val the string value to set the option to
     */
    public void setCurChoice(String val) {
        this.choices.put(getAvailableChoices().getFirst(), val);
        this.curSettingIndex++;
    }

    /**
     * Sets a specific choice in this GameSettings. This does not advance this object's knowledge of the current
     * option it is on.
     * @param op string name of the option to set
     * @param val string value to set the option to
     */
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

    /**
     * helper method to check if the settings in this GameSettings have all been completed or not
     * @return true if all options have been set, false otherwise
     */
    public boolean isComplete() {
        return this.curSettingIndex >= ENUM_OPTIONS.size();
    }
}
