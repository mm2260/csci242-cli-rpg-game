package game.characters.heroes.casters;

import game.characters.Caster;
import game.characters.Hero;
import game.characters.Role;
import game.characters.controllers.Controller;
import game.core.GameManager;
import game.core.Party;

import java.util.HashMap;

public class Enchanter extends Caster {

    //Constructor:
    public Enchanter(Controller.Type controllerType, String name, Party party, Party opposition) {
        super( controllerType,
                name, Role.ENCHANTER,
                Enchanter.ATK, Enchanter.DEF, Enchanter.MAX_HP, Enchanter.MAGIC_DEF,
                Enchanter.MAGIC_ATK, MAX_MP, SPELL_COST,
                Enchanter.POWER, Enchanter.CHANCE, Enchanter.LIMIT_BREAK,
                party, opposition);
        this.actionNames = new HashMap<>();
        this.actionNames.put( "ULTIMATE", "Warcry" );
        this.actionNames.put("SPELL", "Enrich");
    }

    //-----------------------------------------------------------------------
    //FUNCTIONS:

    //Overrides:
    @Override
    protected void ultimate(Hero target) {
        //[DEFENSIVE]
        //Warcry: Buff your team's base attack damage by $ATK_BUFF
        if( this.limitBreak>=LIMIT_BREAK) {
            this.party.getTeam().stream().forEach( hero -> hero.setAttackBuff( Enchanter.ATK_BUFF ) );

            //The Ultimate ability can be used only once.
            this.limitBreak = -1;
        }
    }

    @Override
    protected void castSpell(Hero target) {
        //[DEFENSIVE]
        //Enrich: Buff HP by $HP_BUFF for everyone in your team.
        if( this.mana>=SPELL_COST ) {
            this.party.getTeam().stream().forEach( hero -> hero.buffMaxHP( Enchanter.HP_BUFF ) );
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
    private static final int MAGIC_ATK;
    private static final int MAX_MP;
    private static final int SPELL_COST;
    private static final int ATK_BUFF;
    private static final int HP_BUFF;

    //Static initializer block.
    static {

        ATK = Integer.parseInt(GameManager.getProperty("ATK.ENCHANTER"));
        DEF = Integer.parseInt(GameManager.getProperty("DEF.ENCHANTER"));
        MAX_HP = Integer.parseInt(GameManager.getProperty("MAX_HP.ENCHANTER"));
        MAGIC_DEF = Integer.parseInt(GameManager.getProperty("MAGIC_DEF.ENCHANTER"));
        POWER = Integer.parseInt(GameManager.getProperty("POWER.ENCHANTER"));
        CHANCE = Double.parseDouble(GameManager.getProperty("CHANCE.ENCHANTER"));
        LIMIT_BREAK = Integer.parseInt(GameManager.getProperty("LIMIT_BREAK.ENCHANTER"));
        MAGIC_ATK = Integer.parseInt(GameManager.getProperty("MAGIC_ATK.ENCHANTER"));
        MAX_MP = Integer.parseInt(GameManager.getProperty("MAX_MP.ENCHANTER"));
        SPELL_COST = Integer.parseInt(GameManager.getProperty("SPELL_COST.ENCHANTER"));
        ATK_BUFF = Integer.parseInt(GameManager.getProperty("ATK_BUFF.ENCHANTER"));
        HP_BUFF = Integer.parseInt(GameManager.getProperty("HP_BUFF.ENCHANTER"));
    }
}
