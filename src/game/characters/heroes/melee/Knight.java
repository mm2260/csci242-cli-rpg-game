package game.characters.heroes.melee;

import game.characters.Hero;
import game.characters.Melee;
import game.characters.Role;
import game.characters.controllers.Controller;
import game.core.GameManager;
import game.core.Party;

import java.util.HashMap;

public class Knight extends Melee {

    //Constructor:
    public Knight(Controller.Type controllerType, String name, Party party, Party opposition) {
        super(controllerType,
                name, Role.KNIGHT,
                Knight.ATK, Knight.DEF, Knight.MAX_HP, Knight.MAGIC_DEF,
                Knight.POWER, Knight.CHANCE, Knight.LIMIT_BREAK,
                party, opposition);
        this.actionNames = new HashMap<>();
        this.actionNames.put( "ULTIMATE", "Armor Reflect" );
    }

    //-----------------------------------------------------------------------
    //FUNCTIONS:

    //Overrides:
    @Override
    protected void ultimate(Hero target) {
        if( this.limitBreak>=LIMIT_BREAK) {
            //[OFFENSIVE]
            //Armor Reflect: Once activated, all subsequent attacks are reflected.
            //               -- 5% of damage received is inflicted upon everyone in the opposite party.
            this.ultimateActive = true;

            //The Ultimate ability can be used only once.
            this.limitBreak = -1;
        }
    }

    @Override
    public void takeDamage(int amount){
        super.takeDamage(amount);
        if (ultimateActive) {
            this.opposition.getTeam().stream().forEach(enemy -> enemy.takeDamage((int) (amount * 0.05)));
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

    private boolean ultimateActive = false;

    private static final int ATK;
    private static final int DEF;
    private static final int MAX_HP;
    private static final int MAGIC_DEF;
    private static final int POWER;
    private static final double CHANCE;
    private static final int LIMIT_BREAK;

    //Static initializer block.
    static {

        ATK = Integer.parseInt(GameManager.getProperty("ATK.KNIGHT"));
        DEF = Integer.parseInt(GameManager.getProperty("DEF.KNIGHT"));
        MAX_HP = Integer.parseInt(GameManager.getProperty("MAX_HP.KNIGHT"));
        MAGIC_DEF = Integer.parseInt(GameManager.getProperty("MAGIC_DEF.KNIGHT"));
        POWER = Integer.parseInt(GameManager.getProperty("POWER.KNIGHT"));
        CHANCE = Double.parseDouble(GameManager.getProperty("CHANCE.KNIGHT"));
        LIMIT_BREAK = Integer.parseInt(GameManager.getProperty("LIMIT_BREAK.KNIGHT"));
    }
}
