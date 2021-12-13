package game.characters.heroes.melee;

import game.characters.Hero;
import game.characters.Melee;
import game.characters.Role;
import game.characters.controllers.Controller;
import game.core.GameManager;
import game.core.Party;

import java.util.HashMap;

public class Berserker extends Melee {

    //Constructor:
    public Berserker(Controller.Type controllerType, String name, Party party, Party opposition) {
        super(controllerType,
                name, Role.BERSERKER,
                Berserker.ATK, Berserker.DEF, Berserker.MAX_HP, Berserker.MAGIC_DEF,
                Berserker.POWER, Berserker.CHANCE, Berserker.LIMIT_BREAK,
                party, opposition);
        this.actionNames = new HashMap<>();
        this.actionNames.put( "ULTIMATE", "Storm Hammer" );
    }

    //-----------------------------------------------------------------------
    //FUNCTIONS:

    //Overrides:
    @Override
    protected void ultimate(Hero target) {
        //[OFFENSIVE]
        //Storm Hammer: Apply massive damage to everyone in the enemy team.
        if( this.limitBreak>=LIMIT_BREAK) {
            this.opposition.getTeam().stream().forEach( enemy -> { enemy.takeDamage( this.power/2 ); } );
            target.takeDamage( this.power/2 );
            //The Ultimate ability can be used only once.
            this.limitBreak = -1;
        }

    }

    @Override
    protected int[] getCharacterState() {
        return new int[]{ this.party.getTotalHealthPercentage(),
                this.limitBreak,
                -1,
                9999,
                this.stamina,
                this.getCurrentHP(),
                this.getMaxHP(),
                this.LIMIT_BREAK
        };
    }

    @Override
    public boolean ultimateAvailable() { return this.limitBreak > LIMIT_BREAK; }

    //-----------------------------------------------------------------------
    //FIELDS:

    private static final int ATK;
    private static final int DEF;
    private static final int MAX_HP;
    private static final int MAGIC_DEF;
    private static final int POWER;
    private static final double CHANCE;
    protected static int LIMIT_BREAK;

    //Static variable initialization.
    static {

        ATK = Integer.parseInt(GameManager.getProperty("ATK.BERSERKER"));
        DEF = Integer.parseInt(GameManager.getProperty("DEF.BERSERKER"));
        MAX_HP = Integer.parseInt(GameManager.getProperty("MAX_HP.BERSERKER"));
        MAGIC_DEF = Integer.parseInt(GameManager.getProperty("MAGIC_DEF.BERSERKER"));
        POWER = Integer.parseInt(GameManager.getProperty("POWER.BERSERKER"));
        CHANCE = Double.parseDouble(GameManager.getProperty("CHANCE.BERSERKER"));
        LIMIT_BREAK = Integer.parseInt(GameManager.getProperty("LIMIT_BREAK.BERSERKER"));
    }
}
