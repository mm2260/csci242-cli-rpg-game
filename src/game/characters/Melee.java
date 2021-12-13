package game.characters;

import game.characters.controllers.Controller;
import game.core.GameManager;
import game.core.Party;
import game.core.Utils;

public abstract class Melee extends Hero {

    //Constructor:
    protected Melee(Controller.Type controllerType,
                    String name, Role role,
                    int atk, int def, int maxHP, int magicDef,
                    int power, double chance, int limitBreak,
                    Party party, Party opposition )
    {
        super(controllerType,
                name, role,
                atk, def, maxHP, magicDef,
                power, chance, limitBreak,
                party, opposition );
    }

    //-----------------------------------------------------------------------
    //FUNCTIONS:

    //Overrides:
    @Override
    protected int[] getCharacterState() {
        return new int[0];
    }

    @Override
    protected boolean performAction(Hero target, Controller.Action action, Controller controller) {
        switch ( action ) {
            case ULTIMATE:
                GameManager.appendGameLog( Utils.format(Utils.GColor.CYAN,
                        "%s %s uses their ultimate ability: %s",
                        this.getRole().name(), this.getName(), this.actionNames.get("ULTIMATE")) );
                this.ultimate(target);
                return false;
            case ATTACK:
                this.attack(target);
                return false;
            case FLEE:
                GameManager.appendGameLog( Utils.format(Utils.GColor.CYAN,
                        "%s 's Flee successful!", this.getName()) );
                return this.flee();
            case DO_NOTHING:
                GameManager.appendGameLog( Utils.format(Utils.GColor.CYAN,
                        "%s wasn't able to do anything.", this.getName()) );
                return false;
            default:
                return false;
        }
    }

}
