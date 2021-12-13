package game.scenes;

import game.characters.Role;
import game.characters.controllers.Controller;
import game.characters.heroes.HeroFactory;
import game.core.*;

import static game.core.Utils.*;

import java.util.*;
import java.util.stream.IntStream;

public class MainMenu extends Scene {

    //-----------------------------------------------------------------------
    //FUNCTIONS:

    //CONSTRUCTOR:
    public MainMenu(){
        this.INTRO_SEQUENCE = new Scanner ( getResource("introSequence.txt") );
        showMenu();
    }

    //----------------
    //MENU SEQUENCES:

    private void showMenu() {

        //LOGO ASCII ART SECTION:
        StringBuffer logo = new StringBuffer();

        drawLine(logo, LineType.DASHED,100);
        Scanner logoScanner = new Scanner( getResource("logo.txt") );

        while(logoScanner.hasNextLine()){ logo.append("|")
                                              .append(format(GColor.YELLOW, logoScanner.nextLine()))
                                              .append("|%n"); }
        drawLine(logo, LineType.EMPTY,100);
        logo.append("|")
            .append(" ".repeat(42)).append(format(GColor.RED, "--Great Battles of Bravery & Valor!"))
            .append(" ".repeat(21)).append("|%n");
        drawLine(logo, LineType.EMPTY,100);
        drawLine(logo, LineType.DASHED,100);
        //ASCII art generated with the help of https://patorjk.com/taag

        //CHOICES SECTION:
        StringBuffer choices = new StringBuffer();
        drawSection( choices, null, 100, 6, 35,
                "\n[NEW GAME "+format(GColor.BLUE,"(n)")+"]    " +
                        "[QUIT "+format(GColor.BLUE,"(q)")+"]");

        //INFORMATION SECTION:
        StringBuffer info = new StringBuffer();
        drawSection( info,
                null,
                100, 6, 35,
                "\n"+format(GColor.GREEN,"Please input your choice..."));

        buffer.append( combineVertical(logo, combineVertical(choices, info, 100), 100 ) );
    }

    public void intro(Party dragon, Party lion){

        String name;
        Role r1;
        Role r2;
        Role r3;
        Team team;

        printNextLineInSequence();
        printNextLineInSequence();
        printNextLineInSequence();

        renderAlert( INTRO_SEQUENCE.nextLine(),format(GColor.GREEN,"Please input you name..."));
        name = GameManager.getInput();

        renderAlert( INTRO_SEQUENCE.nextLine()+name,"");
        System.out.println( format(GColor.BLUE, "Press 'ENTER' to continue...") );
        GameManager.pressEnter();

        printNextLineInSequence();

        r1 = chooseHero();
        r2 = chooseHero();
        r3 = chooseHero();

        printNextLineInSequence();

        printNextLineInSequence();

        renderAlert( INTRO_SEQUENCE.nextLine(),
                format(GColor.GREEN, "Please enter your desired faction { [DRAGON (d)] | [LION (l)] }"));
        printc("CHOICE:\t",GColor.BLUE);
        team = (GameManager.getChoice()=='d')?Team.DRAGON:Team.LION;

        List<Controller.Type> controllers = controllerPrompt();

        initializeParties(dragon, lion, team, controllers.get(0),controllers.get(1), r1, r2, r3);

        printNextLineInSequence();
        printNextLineInSequence();

        renderAlert( INTRO_SEQUENCE.nextLine() +
                format(GColor.RED, team==Team.DRAGON?Team.LION.name():Team.DRAGON.name()+"!!!"), "");
        System.out.println( format(GColor.BLUE, "Press 'ENTER' to continue...") );
        GameManager.pressEnter();

        printNextLineInSequence();
        printNextLineInSequence();

    }

    //-------------------
    //UTILITY FUNCTIONS:

    private void renderAlert(String msg, String subtext){
        StringBuffer temp = new StringBuffer().append(msg)
                .append("\n\n")
                .append( format(GColor.GREEN,subtext) );

        Renderer.render(
                new Scene().drawSection( "INTRO", 120, 26,
                        String.format( Alert.createAlertBox(80, 20,
                                temp.toString()).toString()) )
        );
    }

    private void printNextLineInSequence() { printNextLineInSequence("Press 'ENTER' to continue...",""); }

    private void printNextLineInSequence(String prompt, String subtext ) {
        renderAlert( INTRO_SEQUENCE.nextLine(),subtext);
        System.out.println( format(GColor.BLUE, prompt) );
        GameManager.pressEnter();
    }

    private void initializeParties(Party dragon, Party lion, Team team, Controller.Type controller1, Controller.Type controller2, Role ... roles) {
        Party party = team==Team.DRAGON?dragon:lion;
        Party opposition = team==Team.DRAGON?lion:dragon;

        IntStream.range(0,3).forEach( i ->
                party.addHero(HeroFactory.create(roles[i], controller1, party, opposition)));

        HashSet<Role> availableRoles = new HashSet<>(Arrays.asList(Role.BERSERKER, Role.ENCHANTER, Role.HEALER,
                Role.KNIGHT, Role.NECROMANCER, Role.TANK));
        availableRoles.removeAll(Arrays.asList( roles[0], roles[1], roles[2]));

        availableRoles.forEach( role -> opposition.addHero(
                HeroFactory.create(role, controller2, opposition, party) ) );
    }

    private List<Controller.Type> controllerPrompt() {
        renderAlert( format(GColor.GREEN,"Select '1' for Player vs CPU\n" +
                "Select '2' for Player vs Player\n" +
                "Select '3' for CPU vs CPU") ,"");
        System.out.println( format(GColor.CYAN, "CHOICE:" ) );

        switch ( GameManager.getChoice() ){
            case '1':   //Player vs CPU
                return Arrays.asList( Controller.Type.PLAYER, Controller.Type.CPU );
            case '2': //Player vs Player
                return Arrays.asList( Controller.Type.PLAYER, Controller.Type.PLAYER );
            case '3': // CPU vs CPU
                return Arrays.asList( Controller.Type.CPU, Controller.Type.CPU );
            default:
                return Arrays.asList( Controller.Type.CPU, Controller.Type.CPU );
        }
    }

    private Role chooseHero() {
        renderAlert( INTRO_SEQUENCE.nextLine(),"Please select a choice:");
        System.out.println("Melee Heroes:"+format(GColor.CYAN, this.MELEE.toString()) );
        System.out.println("Caster Type Heroes:"+format(GColor.YELLOW, this.CASTER.toString()) );
        printc("CHOICE:\t",GColor.BLUE);
        return handleChoice(GameManager.getChoice(), this.CASTER, this.MELEE) ;
    }

    private Role handleChoice(char choice, List<String> caster, List<String> melee){
        switch (choice){
            case 'h':
                caster.remove("[Healer (h)]");
                return Role.HEALER;
            case 'e':
                caster.remove("[Enchanter (e)]");
                return Role.ENCHANTER;
            case 'n':
                caster.remove("[Necromancer (n)]");
                return Role.NECROMANCER;
            case 'b':
                melee.remove("[Berserker (b)]");
                return Role.BERSERKER;
            case 't':
                melee.remove("[Tank (t)]");
                return Role.TANK;
            case 'k':
                melee.remove("[Knight (k)]");
                return Role.KNIGHT;
        }
        return null;
    }

    //-----------------------------------------------------------------------
    //FIELDS:

    // Constants:
    private final Scanner INTRO_SEQUENCE;
    private final ArrayList<String> CASTER = new ArrayList<>(Arrays.asList("[Healer (h)]", "[Enchanter (e)]", "[Necromancer (n)]"));
    private final ArrayList<String> MELEE = new ArrayList<>(Arrays.asList("[Berserker (b)]", "[Tank (t)]", "[Knight (k)]"));

}
