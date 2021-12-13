package game.core;

import game.characters.Role;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GameManager {

    public enum StateProperty { PARTY_A, PARTY_B, CHOICE, LOG;}
    private static Properties properties = new Properties();

    static{
        gameState = new GameState();

        try { properties.load( Utils.getResource("config.properties") ); }
        catch (Exception e){ System.out.println("Failed to initialize: " + e.getMessage() ); }

    }

    private static class GameState {

        String partyA="party-A uninitialized";
        String partyB="party-B uninitialized";

        String choices="choices uninitialized";

        StringBuffer log= new StringBuffer().append("log uninitialized.");
    }

    private static GameState gameState;

    public void setParty(String partyA, String partyB) {
        GameManager.gameState.partyA=partyA;
        GameManager.gameState.partyB=partyB;
    }

    public static void setChoices(String choices) { GameManager.gameState.choices=choices; }

    public void resetGameLog() { GameManager.gameState.log= new StringBuffer(); }

    public static void appendGameLog(String message){
        GameManager.gameState.log.append(message).append("\n");
    }

    public static void appendGameLogWithoutLineBreak(String message) {
        GameManager.gameState.log.append(message);
    }

    public static String getState(StateProperty property){
        switch (property) {
            case PARTY_A:
                return GameManager.gameState.partyA;
            case PARTY_B:
                return GameManager.gameState.partyB;
            case CHOICE:
                return GameManager.gameState.choices;
            case LOG:
                return GameManager.gameState.log.toString();
            default:
                // Cannot be reached.
        }
        return "Oops! Something happened!";
    }

    public static String getProperty(String key) {
        return GameManager.properties.getProperty(key);
    }

    public static void printProperties(){
        GameManager.properties.keySet().stream()
                .map(key -> key + ": "+ properties.getProperty(key.toString())).forEach(System.out::println);
    }

    public static Scanner userInput = new Scanner(System.in);

    public static char getChoice(){ return userInput.next().charAt(0); }

    public static String getInput(){ return userInput.nextLine();}

    public static String getName(Team team, Role role) {
        return null;
    }

    public static void pressEnter(){ userInput.nextLine(); }
}
