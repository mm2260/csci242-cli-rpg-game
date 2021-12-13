package game.characters.heroes.casters;

import game.characters.Caster;
import game.characters.Hero;
import game.characters.Role;
import game.characters.controllers.Controller;
import game.core.GameManager;
import game.core.Party;
import game.core.Utils;

import java.util.HashMap;

public class Necromancer extends Caster {

    //Constructor:
    public Necromancer(Controller.Type controllerType, String name, Party party, Party opposition) {
        super(controllerType,
                name, Role.NECROMANCER,
                Necromancer.ATK, Necromancer.DEF, Necromancer.MAX_HP, Necromancer.MAGIC_DEF,
                Necromancer.STEAL_AMT, MAX_MP, SPELL_COST,
                Necromancer.POWER, Necromancer.CHANCE, Necromancer.LIMIT_BREAK,
                party, opposition);
        this.actionNames = new HashMap<>();
        this.actionNames.put( "ULTIMATE", Utils.format(Utils.GColor.RED ,"Dark Resurrection")  );
        this.actionNames.put( "SPELL", Utils.format(Utils.GColor.RED ,"Life Steal")  );
    }

    //-----------------------------------------------------------------------
    //FUNCTIONS:

    //Overrides:
    @Override
    protected void ultimate(Hero target) {
        //[OFFENSIVE]
        //Dark Resurrection: Resurrect a fallen enemy and compel it to fight its own.
        //                   --resurrected with 25% health.
        if(this.opposition.hasFallen()) {
            if (this.limitBreak >= LIMIT_BREAK) {
                Hero pawn = this.opposition.revive();
                pawn.heal(pawn.getMaxHP() / 4);
                pawn.hasFallen(false);
                this.party.addHero(pawn);

                //Broadcast Message
                GameManager.appendGameLog(String.format("%s Resurrected with black magic!", pawn.getName()));

                //The Ultimate ability can be used only once.
                this.limitBreak = -1;
            }
        } else {
            GameManager.appendGameLog(Utils.format(Utils.GColor.BLUE,
                    "Ultimate ability cannot be used. No fallen heroes.") );
            this.castSpell(target);
        }
    }

    @Override
    protected void castSpell(Hero target) {
        //[OFFENSIVE]
        //Steal Life: The necromancer heals itself by draining its opponents life.
        if( this.mana>=SPELL_COST ) {

            //Mitigate the damage with the target's magic defence stat.
            double multiplier = target.getMaxHP() / (double) (target.getMaxHP() + target.getMagicDefence());
            int lifeSteal = (int) (Necromancer.STEAL_AMT * multiplier);

            //Inflict the damage, then heal.
            target.takeDamage( lifeSteal );
            this.heal( lifeSteal );
            this.mana = this.mana - SPELL_COST;
        }
    }

    @Override
    public boolean spellAvailable() { return this.mana > SPELL_COST; }

    @Override
    protected int[] getCharacterState() {
        return new int[]{ this.opposition.getTotalHealthPercentage(),
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
    private static final int STEAL_AMT;
    private static final int MAX_MP;
    private static final int SPELL_COST;

    //Static initializer block.
    static {

        ATK = Integer.parseInt(GameManager.getProperty("ATK.NECROMANCER"));
        DEF = Integer.parseInt(GameManager.getProperty("DEF.NECROMANCER"));
        MAX_HP = Integer.parseInt(GameManager.getProperty("MAX_HP.NECROMANCER"));
        MAGIC_DEF = Integer.parseInt(GameManager.getProperty("MAGIC_DEF.NECROMANCER"));
        POWER = Integer.parseInt(GameManager.getProperty("POWER.NECROMANCER"));
        CHANCE = Double.parseDouble(GameManager.getProperty("CHANCE.NECROMANCER"));
        LIMIT_BREAK = Integer.parseInt(GameManager.getProperty("LIMIT_BREAK.NECROMANCER"));
        STEAL_AMT = Integer.parseInt(GameManager.getProperty("STEAL_AMT.NECROMANCER"));
        MAX_MP = Integer.parseInt(GameManager.getProperty("MAX_MP.NECROMANCER"));
        SPELL_COST = Integer.parseInt(GameManager.getProperty("SPELL_COST.NECROMANCER"));
    }
}
