package game.characters.heroes.casters;

import game.characters.Hero;
import game.characters.Caster;
import game.characters.Role;
import game.characters.controllers.Controller;
import game.core.GameManager;
import game.core.Party;
import game.core.Utils;

import java.util.HashMap;

public class Healer extends Caster {

    //Constructor:
    public Healer(Controller.Type controllerType, String name, Party party, Party opposition) {
        super(controllerType,
                name, Role.HEALER,
                Healer.ATK, Healer.DEF, Healer.MAX_HP, Healer.MAGIC_DEF,
                Healer.HEAL_AMT, MAX_MP, SPELL_COST,
                Healer.POWER, Healer.CHANCE, Healer.LIMIT_BREAK,
                party, opposition);
        this.actionNames = new HashMap<>();
        this.actionNames.put( "ULTIMATE", "Revival" );
        this.actionNames.put("SPELL", "Healing");
    }

    //-----------------------------------------------------------------------
    //FUNCTIONS:

    //Overrides:
    @Override
    protected void ultimate(Hero target) {
        //[DEFENSIVE]
        //Revival: Revive a fallen enemy.
        //         --revived with 75% Health.
        if(this.party.hasFallen()) {
            if (this.limitBreak >= LIMIT_BREAK) {
                Hero revived = this.party.revive();
                revived.heal((int) (revived.getMaxHP() * 3 / 4f));
                revived.hasFallen(false);
                this.party.addHero(revived);

                //Broadcast Message
                GameManager.appendGameLog(String.format("%s Revived!", revived.getName()));

                //The Ultimate ability can be used only once.
                this.limitBreak = -1;
            }
        } else {
            GameManager.appendGameLog(Utils.format(Utils.GColor.BLUE,
                    "Ultimate ability cannot be used. No fallen heroes.") );
            castSpell(target); }
    }

    @Override
    protected void castSpell(Hero target) {
        //[DEFENSIVE]
        //Healing: Heal everyone in your team by $HEAL_AMT
        if( this.mana>=SPELL_COST ) {
            this.party.getTeam().stream().forEach( hero -> hero.heal( HEAL_AMT ) );
            this.heal( HEAL_AMT );
            this.mana = this.mana - SPELL_COST;
        }
    }

    @Override
    public boolean spellAvailable() { return this.mana > SPELL_COST; }

    @Override
    protected int[] getCharacterState() {
        return new int[]{ this.party.getTotalHealthPercentage(),
                this.limitBreak,
                this.mana,
                this.spellCost,
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
    private static final int HEAL_AMT;
    private static final int MAX_MP;
    private static final int SPELL_COST;

    //Static initializer block.
    static {

        ATK = Integer.parseInt(GameManager.getProperty("ATK.HEALER"));
        DEF = Integer.parseInt(GameManager.getProperty("DEF.HEALER"));
        MAX_HP = Integer.parseInt(GameManager.getProperty("MAX_HP.HEALER"));
        MAGIC_DEF = Integer.parseInt(GameManager.getProperty("MAGIC_DEF.HEALER"));
        POWER = Integer.parseInt(GameManager.getProperty("POWER.HEALER"));
        CHANCE = Double.parseDouble(GameManager.getProperty("CHANCE.HEALER"));
        LIMIT_BREAK = Integer.parseInt(GameManager.getProperty("LIMIT_BREAK.HEALER"));
        HEAL_AMT = Integer.parseInt(GameManager.getProperty("HEAL_AMT.HEALER"));
        MAX_MP = Integer.parseInt(GameManager.getProperty("MAX_MP.HEALER"));
        SPELL_COST = Integer.parseInt(GameManager.getProperty("SPELL_COST.HEALER"));
    }
}
