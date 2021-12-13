package game.characters;

import game.characters.controllers.CPU;
import game.characters.controllers.Controller;
import game.characters.controllers.Player;
import game.core.GameManager;
import game.core.Party;
import game.core.Utils;

public abstract class Caster extends Hero {

    //Constructor:
    protected Caster(Controller.Type controllerType,
                     String name, Role role,
                     int atk, int def, int maxHP, int magicDef,
                     int mgAtk, int maxMana , int spellCost,
                     int power, double chance, int limitBreak,
                     Party party, Party opposition )
    {
        super(controllerType,
                name, role,
                atk, def, maxHP, magicDef,
                power, chance, limitBreak,
                party, opposition );

        this.maxMana = this.mana = maxMana;
        this.mgAtk = mgAtk;
        this.spellCost = spellCost;
    }

    //-----------------------------------------------------------------------
    //FUNCTIONS:

    //Protected:
    public void replenishMana(int amount) { this.mana = Math.min(this.mana + amount, this.maxMana); }

    protected abstract void castSpell(Hero target);

    protected void broadcastSpellMsg(){
        GameManager.appendGameLog( String.format("%s %s chose to use spell!",
                this.getRole().name(),
                this.getName() )
        );
    }
    //-----------------------------------
    //Controller utility functions:

    public abstract boolean spellAvailable();

    //-----------------------------------
    //Overrides:
    @Override
    public String toString() {
        return new StringBuffer(super.toString())
                .append( ", MP: ")
                .append( String.format("(%d/%d)", this.mana, this.maxMana))
                .append( createBar(6*this.mana/this.maxMana, 6, Utils.GColor.BLUE) )
                .toString();
    }

    @Override
    protected boolean performAction(Hero target, Controller.Action action, Controller controller) {
        switch ( action ) {
            case SPELL:
                GameManager.appendGameLog( Utils.format(Utils.GColor.CYAN,
                        "%s %s uses their spell: %s",
                        this.getRole().name(), this.getName(), this.actionNames.get("SPELL")) );
                        this.castSpell(target);
                        return false;
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
                        "%s %s's Flee successful!", this.getRole().name(), this.getName()) );
                return this.flee();
            case DO_NOTHING:
                GameManager.appendGameLog( Utils.format(Utils.GColor.CYAN,
                        "%s wasn't able to do anything.", this.getName()) );
                return false;
            default:
                return false;
        }
    }

    //-----------------------------------------------------------------------
    //FIELDS:

    //Hero Attributes:
    protected final int mgAtk;
    protected final int spellCost;

    //Hero stats:
    protected  int maxMana;
    protected int mana;
}
