package game.characters.controllers;

import game.characters.Caster;
import game.characters.Hero;
import game.characters.heroes.casters.Enchanter;
import game.characters.heroes.casters.Healer;
import game.characters.heroes.melee.Berserker;
import game.characters.heroes.melee.Knight;
import game.core.GameManager;
import game.core.Renderer;
import game.core.Utils;
import game.scenes.Battle;

import java.util.HashSet;
import java.util.stream.Collectors;

public class Player implements Controller {

    HashSet<String> availableChoices;
    boolean doNothing = false;

    @Override
    public Action signalAction(Hero caller, int... characterState) {

        availableChoices = new HashSet<>();

        attack(caller);
        ultimate(caller);
        if(caller instanceof Caster) { spell( (Caster)(caller) ); }

        availableChoices.add( Utils.format( Utils.GColor.PURPLE, "f" ) );

        GameManager.setChoices( String.format("Available choices for %s: %s %n%n%s %n%s",
                                        Utils.format(Utils.GColor.CYAN,"%s %s",
                                                caller.getRole().name(),
                                                caller.getName() ),
                                        availableChoices.stream()
                                                        .map(i -> "("+i+")")
                                                        .collect(Collectors.toList())
                                                        .toString() ,
                                        Utils.format(Utils.GColor.GREEN,"Please select your choice..."),
                                        Utils.format(Utils.GColor.GREEN,
                                                "[(a)-> attack (s)-> spell (f)-> flee (u)-> ultimate]") )
                              );
        Renderer.render( new Battle() );
        Utils.printc("CHOICE: ", Utils.GColor.BLUE);
        char choice = GameManager.getChoice();

         availableChoices = (HashSet<String>) availableChoices.stream()
                                            .map(this::stringFormatting)
                                            .collect(Collectors.toSet());

        System.out.println(availableChoices);

        switch (choice) {
            case 'a':
                if ( availableChoices.contains("a") ) {
                    GameManager.appendGameLog( String.format("%s %s chose to attack!",
                                                            caller.getRole().name(),
                                                            caller.getName() )
                                             );

                    return Action.ATTACK;
                }
                else {
                    Utils.printc("Action <attack> unavailable. Need more STAMINA. %n", Utils.GColor.RED);
                    return Action.DO_NOTHING;
                }
            case 'u':
                if ( availableChoices.contains("u") ) {
                    GameManager.appendGameLog( String.format("%s %s chose their ultimate!",
                                                caller.getRole().name(),
                                                caller.getName() )
                                             );

                    return Action.ULTIMATE;
                }
                else {
                    Utils.printc("Action <ultimate> unavailable.%n", Utils.GColor.RED);
                    return Action.DO_NOTHING;
                }
            case 's':
                if ( availableChoices.contains("s") ) { return Action.SPELL; }
                else {
                    Utils.printc("Action <spell> unavailable. Need more MANA%n", Utils.GColor.RED);
                    return Action.DO_NOTHING;
                }
            case 'f':
                return flee();
            default:
                return Action.DO_NOTHING;
        }
    }

    protected String stringFormatting(String string){
        //The ANSI escape sequences messed with the calculation of string length.
        //Regex referred from StackOverflow 14652538/remove-ascii-color-codes.
        return string.replaceAll("\u001B\\[[;\\d]*[ -/]*[@-~]","");
    }

    //-----------------------------------------------------------------------
    //Available Behaviors:

    private void ultimate(Hero hero) {
        if ( hero.ultimateAvailable() ) {
            availableChoices.add( Utils.format( Utils.GColor.CYAN, "u" ) );
        }
    }

    private void spell(Caster hero) {
        if ( hero.spellAvailable() ) {
            availableChoices.add( Utils.format( Utils.GColor.YELLOW, "s" ) );
        }
    }

    private void attack(Hero hero) {
        if(hero.attackAvailable() ){
            availableChoices.add( Utils.format( Utils.GColor.RED, "a" ) );
        }
    }

    private Action flee(){
        if ( Math.random() > 0.5 ){
            return Action.FLEE;
        }
        else {
            this.doNothing = true;
            return Action.DO_NOTHING;
        }
    }

    //-----------------------------------------------------------------------
    //Overrides:

    @Override
    public String toString() {
        return "P";
    }
}
