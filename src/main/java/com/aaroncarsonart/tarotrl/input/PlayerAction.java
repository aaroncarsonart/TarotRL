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
    ASCEND("Navigate the player one tile of higher elevation"),
    DESCEND("Navigate the player one tile of lower elevation"),

    WAIT("Player stands still, and allows the game clock to advance one step"),
    REST("Player stands still, and allows the game clock to advance one step"),

    CONFIRM("Confirm current action. (Context-sensitive)"),
    CANCEL("Cancel current action. (Context-sensitive)"),

    DOOR("Open or close a door."),
    AUTO_PICKUP_ITEMS("Automatically add items to inventory when walked over"),

    INSPECT("Inspect the focused origin, reporting any relevant info.")
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

    public boolean isMovementAction() {
        switch (this) {
            case MOVE_LEFT:
            case MOVE_RIGHT:
            case MOVE_UP:
            case MOVE_DOWN:
            case ASCEND:
            case DESCEND:
                return true;
            default:
                return false;
        }
    }
}
