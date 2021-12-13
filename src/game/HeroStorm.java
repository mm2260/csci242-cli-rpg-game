package game;

import static game.core.Utils.*;

import game.characters.Hero;
import game.characters.Role;
import game.characters.controllers.Controller;
import game.characters.heroes.HeroFactory;

import game.scenes.Alert;
import game.scenes.Battle;
import game.core.*;
import game.scenes.MainMenu;
import javafx.concurrent.Worker;

import java.awt.*;

public class HeroStorm {

//Not Constant
    private int round = 0;

    public static void main(String[] args) {

//        System.setProperty("java.awt.headless","true");

//        SoundHandler soundHandler = new SoundHandler();
//        soundHandler.setMusic("music.wav");

//        new Thread(soundHandler::loopMusic).start();
        new Thread(HeroStorm::start).start();
    }

    private static void start() {

        HeroParty dragon = new HeroParty("Dragon");
        HeroParty lion = new HeroParty("Lion");

        MainMenu mainMenu = new MainMenu();

        Renderer.render(mainMenu);
        printc("CHOICE: ", GColor.BLUE);

        switch(GameManager.getChoice()) {
            case 'n':
                mainMenu.intro(dragon, lion);
                break;
            case 'q':
                printc("It is sad to see you go, hero... :(", GColor.BLUE);
                HeroStorm.quit();
        }

        play(dragon, lion);
    }

    private static void play(Party dragon, Party lion) {
        GameManager gm = new GameManager();
        gm.setParty(dragon.toString(), lion.toString());

        HeroStorm game = new HeroStorm();

        game.gameplayLoop(gm, dragon, lion);

        Renderer.render( new Scene().appendToBuffer(
                Alert.createAlertBox(122, 24,
                        format(GColor.CYAN, "TEAM %s WON!",
                                dragon.getTeam().size()==0?"LION":"DRAGON" )
                ).toString() ));
        quit();
    }

    private void gameplayLoop(GameManager gameManager, Party A, Party B) {

        while( A.numHeroes() !=0 && B.numHeroes() != 0) {

            gameManager.resetGameLog();

            if (this.round++ % 2 == 0) {
                faceOff(A, B);
            } else {
                faceOff(B, A);
            }

            gameManager.setParty(A.toString(), B.toString());

            gameManager.setChoices( format(GColor.BLUE, "Press 'ENTER' to move onto the next round.") );
            Renderer.render(new Battle());
            GameManager.pressEnter();
        }
    }

    private void faceOff( Party A, Party B ) {
        Hero heroA = A.removeHero();
        Hero heroB = B.removeHero();

        GameManager.appendGameLog( String.format("Round #%d", this.round) );
        GameManager.appendGameLog( format(GColor.PURPLE, "**%s %s vs %s %s:",
                                   heroA.getRole().name(), heroA.getName(),
                                   heroB.getRole().name(), heroB.getName() )
                                 );

        boolean fled = false;

        fled = heroA.action( heroB );
        if (!heroB.hasFallen() && !fled) {
            GameManager.appendGameLog("-turn switch-");
            heroB.action( heroA );
        } else {
            GameManager.appendGameLog( format(GColor.RED,
                                    "!! %s %s has fallen !!%n", heroB.getRole().name(), heroB.getName()) );
        }
        if (heroA.hasFallen()) {
            GameManager.appendGameLog( format(GColor.RED,
                                "!! %s %s has fallen !!%n", heroA.getRole().name(), heroA.getName()) );
        }

        A.addHero(heroA);
        B.addHero(heroB);

        GameManager.setChoices( format(GColor.BLUE, "Press 'ENTER' to update the log.") );
        Renderer.render(new Battle());
        GameManager.pressEnter();
    }

    public static void quit(){ System.exit(0); }
}
