package game.scenes;

import game.core.GameManager;
import game.core.GameManager.StateProperty;
import game.core.Scene;
import game.core.Utils;

import java.util.Scanner;

import static game.core.Utils.format;

public class Battle extends Scene {

    public Battle() {
        initialize();
    }

    //-----------------------------------------------------------------------
    //HELPER FUNCTIONS:

    private void initialize() {

        StringBuffer logSection = new StringBuffer();
        drawSection(logSection, format(Utils.GColor.CYAN ,"BATTLE LOG:"), 122, 10,
                GameManager.getState(StateProperty.LOG) );

        StringBuffer choiceSection = new StringBuffer();
        drawSection(choiceSection, format(Utils.GColor.GREEN, "CHOICES:"), 122, 4,
                GameManager.getState(StateProperty.CHOICE) );



        StringBuffer partyInfoSection = new StringBuffer();
        drawSection(partyInfoSection, format(Utils.GColor.PURPLE ,"PARTY:"), 122, 10, 4,
                mergeParties(GameManager.getState(StateProperty.PARTY_A),GameManager.getState(StateProperty.PARTY_B)) );


        this.buffer = combineVertical( combineVertical(logSection,
                                                partyInfoSection, 122),
                                       choiceSection, 122);
    }

    private String mergeParties(String partA, String partyB){
        return new StringBuffer().append(partA).append('\n').append(partyB).toString();
    }

    private GameManager gameManager;
}
