package game.characters.heroes.melee;

import game.characters.Hero;
import game.characters.Melee;
import game.characters.Role;
import game.characters.controllers.Controller;
import game.core.GameManager;
import game.core.Party;

import java.util.HashMap;

public class Tank extends Melee {

    //Constructor:
    public Tank(Controller.Type controllerType, String name, Party party, Party opposition) {
        super(controllerType,
                name, Role.TANK,
                Tank.ATK, Tank.DEF, Tank.MAX_HP, Tank.MAGIC_DEF,
                Tank.POWER, Tank.CHANCE, Tank.LIMIT_BREAK,
                party, opposition);
        this.actionNames = new HashMap<>();
        this.actionNames.put( "ULTIMATE", "Stampede" );
    }

    //-----------------------------------------------------------------------
    //FUNCTIONS:

    //Overrides:
    @Override
    protected void ultimate(Hero target) {
        if( this.limitBreak>=LIMIT_BREAK) {
            //[OFFENSIVE]
            //Stampede: defeat target in one shot.
            target.takeDamage( target.getMaxHP() );

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
    private static final int LIMIT_BREAK;

    //Static initializer block.
    static {

        ATK = Integer.parseInt(GameManager.getProperty("ATK.TANK"));
        DEF = Integer.parseInt(GameManager.getProperty("DEF.TANK"));
        MAX_HP = Integer.parseInt(GameManager.getProperty("MAX_HP.TANK"));
        MAGIC_DEF = Integer.parseInt(GameManager.getProperty("MAGIC_DEF.TANK"));
        POWER = Integer.parseInt(GameManager.getProperty("POWER.TANK"));
        CHANCE = Double.parseDouble(GameManager.getProperty("CHANCE.TANK"));
        LIMIT_BREAK = Integer.parseInt(GameManager.getProperty("LIMIT_BREAK.TANK"));
    }
}
