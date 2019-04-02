package com.aaroncarsonart.tarotrl.game.console;

import com.aaroncarsonart.tarotrl.input.PlayerAction;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleInputHandler {
    private Scanner scanner = new Scanner(System.in);
    private static final Map<String, PlayerAction> inputActionsMap = initInputActionsMap();

    private static Map<String, PlayerAction> initInputActionsMap() {
        Map<String, PlayerAction> inputActionsMap = new LinkedHashMap<>();
        inputActionsMap.put("?", PlayerAction.HELP);
        inputActionsMap.put("h", PlayerAction.HELP);
        inputActionsMap.put("help", PlayerAction.HELP);

        inputActionsMap.put("q", PlayerAction.QUIT);
        inputActionsMap.put("quit", PlayerAction.QUIT);
        inputActionsMap.put("exit", PlayerAction.QUIT);

        inputActionsMap.put("w", PlayerAction.MOVE_UP);
        inputActionsMap.put("s", PlayerAction.MOVE_DOWN);
        inputActionsMap.put("a", PlayerAction.MOVE_LEFT);
        inputActionsMap.put("d", PlayerAction.MOVE_RIGHT);

        inputActionsMap.put("up", PlayerAction.MOVE_UP);
        inputActionsMap.put("down", PlayerAction.MOVE_DOWN);
        inputActionsMap.put("left", PlayerAction.MOVE_LEFT);
        inputActionsMap.put("right", PlayerAction.MOVE_RIGHT);

        return inputActionsMap;
    }

    public static final String getConsoleHelpString() {
        StringBuilder sb = new StringBuilder();

        int spacing = 16;
        String entryFormat = "%-" + spacing + "s %-" + spacing + "s %s";
        final char NEWLINE = '\n';

        sb.append("---------------------------------------------------------------------").append(NEWLINE);
        sb.append("Console input commands: (type command + enter to processPlayerAction game action)").append(NEWLINE);
        sb.append("---------------------------------------------------------------------").append(NEWLINE);
        sb.append(NEWLINE);
        sb.append(String.format(entryFormat, "typed command", "game action", "description of command")).append(NEWLINE);
        sb.append(String.format(entryFormat, "-------------", "-----------", "----------------------")).append(NEWLINE);

        for(String typedCommand : inputActionsMap.keySet()) {
            PlayerAction gameAction = inputActionsMap.get(typedCommand);
            String gameActionName = gameAction.name();
            String gameActionDefinition = gameAction.getDescription();
            String formattedEntry = String.format(entryFormat, typedCommand, gameActionName, gameActionDefinition);
            sb.append(formattedEntry).append(NEWLINE);
        }
        return sb.toString();
    }

    public PlayerAction nextAction() {
        String nextLine = scanner.nextLine().toLowerCase();
        PlayerAction nextAction = inputActionsMap.getOrDefault(nextLine, PlayerAction.UNKNOWN);
        return nextAction;
    }
}
