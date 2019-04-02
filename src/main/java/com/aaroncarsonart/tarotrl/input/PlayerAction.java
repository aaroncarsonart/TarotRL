package com.aaroncarsonart.tarotrl.input;

/**
 * Enum class to encapsulate all player executable behaviors.
 */
public enum PlayerAction {
    // default
    UNKNOWN(""),

    HELP("Display the help menu"),
    QUIT("Quit the game"),

    // movement actions
    MOVE_UP("Navigate the player up one tile on the game map"),
    MOVE_DOWN("Navigate the player down one tile on the game map"),
    MOVE_LEFT("Navigate the player left one tile on the game map"),
    MOVE_RIGHT("Navigate the player right one tile on the game map"),
    REST("Player stands still, and allows the game clock to advance one step"),

    OK("Confirm current action. (Context-sensitive)"),
    CANCEL("Cancel current action. (Context-sensitive)"),
    ;

    /**
     * The human-readable text explaining the behavior of this PlayerAction.
     */
    private String description;

    /**
     * Create a new PlayerAction with the given description.
     * @param description The human readable text explaining the behavior of this PlayerAction.
     */
    PlayerAction(String description) {
        this.description = description;
    }

    /**
     * @return The human-readable text explaining the behavior of this PlayerAction.
     */
    public String getDescription() {
        return description;
    }
}
