package game.characters;

import game.characters.controllers.CPU;
import game.characters.controllers.Controller;
import game.characters.controllers.Player;
import game.core.GameManager;
import game.core.Party;

import java.util.HashMap;

import static game.core.Utils.*;

public abstract class Hero {

    //Constructor:
    protected Hero( Controller.Type controllerType,
                    String name, Role role,
                    int atk, int def, int maxHP, int magicDef,
                    int power, double chance, int limitBreak,
                    Party party, Party opposition )
    {
        this.controller = controllerType== Controller.Type.CPU?( new CPU() ):( new Player() );

        this.name = name;
        this.role = role;

        this.atk = atk;
        this.def = def;
        this.maxHP = this.hitPoints = maxHP;
        this.magicDef = magicDef;

        this.power = power;
        this.chance = chance;
        this.limitBreak = limitBreak;

        this.party = party;
        this.opposition = opposition;
    }

    //-----------------------------------------------------------------------
    //FUNCTIONS:

    //Public:

    public int getCurrentHP() { return this.hitPoints; }

    public boolean action(Hero target) {
        if (this.controller instanceof CPU){
            CPU cpu = (CPU) this.controller;
            return performAction(target, cpu.signalAction(this, getCharacterState()), cpu);
        }
        else {
            Player player = (Player) this.controller;
            return performAction(target, player.signalAction(this, getCharacterState()), player);
        }
    }

    public void heal(int amount){
        this.hitPoints = Math.min( this.hitPoints + amount, this.maxHP );

        // send message to log.
        GameManager.appendGameLog( String.format( "%s heals by %s%d points%s.",
                this.name, GColor.YELLOW, amount, GColor.RESET ) );
    }

    public int getMagicDefence(){ return this.magicDef; }

    //Basic hero information getters:
    public String getName() { return this.name; }

    public Role getRole() { return this.role; }

    //Stat functions:
    public void hasFallen(boolean bool) { this.hasFallen = bool; }

    public boolean hasFallen() { return this.hasFallen; }

    public int getDefence() { return this.def; }

    public int getMaxHP() { return this.maxHP; }

    public void setAttackBuff(int buff){
        GameManager.appendGameLog( format(GColor.GREEN, "Attack Buff for %s %s set to %d",
                                this.getRole().name(), this.getName(), buff) );
        this.atkBuff = buff;
    }

    public void buffMaxHP(int buff) {
        GameManager.appendGameLog( format(GColor.GREEN,"Max HP for %s %s increased by %d",
                                        this.getRole().name(), this.getName(), buff) );
        this.maxHP = this.maxHP + buff;
    }

    public void takeDamage(int amount){

        if (this.hitPoints - amount<=0) {
            this.hitPoints = 0;
            this.hasFallen = true;
        } else{
            this.hitPoints = this.hitPoints -amount;
        }

        // change party total.

        // send message to log.
        GameManager.appendGameLog( String.format( "%s takes %s%d damage%s.",
                this.name, GColor.RED, amount, GColor.RESET ) );
    }

    //-----------------------------------
    //Protected:

    protected void attack(Hero enemy) {

        // an attack has a chance to miss.
        if ( Math.random()>missChance && attackAvailable() ){

            boolean powerMove = Math.random() < chance;
            if ( powerMove ) { GameManager.appendGameLog(
                    format(GColor.CYAN, "%s %s USES POWER MOVE!", this.role.name(), this.name) ); }
            // Damage formula:
            // Damage taken = ( Hero-Base-Attack + (probability=chance) { power } ) *
            //                (enemy defence multiplier).
            int baseDamage = this.atk + this.atkBuff + ( powerMove ? power : 0);
            double multiplier = enemy.getMaxHP() / (double) (enemy.getMaxHP() + enemy.getDefence());
            // Regardless of how high the enemy's defence stat, they will take at least some damage.

            // Log is not updated here. It is updated in the takeDamage call.
            enemy.takeDamage((int) (baseDamage * multiplier));
            this.stamina = this.stamina - 15;

            //Ultimate ability can be used only once.
            //If it has not been used before, then increment the hero's limit breaker.
            if(this.limitBreak!=-1) { this.limitBreak = this.limitBreak + 1; }
        }

        // send message to log.
        else { GameManager.appendGameLog( format(GColor.GREEN, "%s misses the attack!", this.name ) ); }

    }

    protected abstract boolean performAction(Hero target, Controller.Action action, Controller controller);

    protected boolean flee() {
        double flee = Math.random();
        if (flee > fleeChance) {
            GameManager.appendGameLog( format(GColor.PURPLE, "%s %s fled successfully!") );
        } else {
            GameManager.appendGameLog( format(GColor.PURPLE, "%s %s was unable to flee!!!") );
        }
        return flee > fleeChance;
    }

    protected abstract void ultimate(Hero target);

    protected abstract int[] getCharacterState();

    protected String createBar(int points, int total, GColor color){
        return new StringBuffer()
                .append("║")
                .append( format( color, "▒".repeat( Math.abs( points+1 )) ) )
                .append( "-".repeat( total-points ))
                .append("║")
                .toString();
    }

    //-----------------------------------
    //Controller utility functions:

    public boolean attackAvailable() { return ( this.stamina >= 5 ); }

    public abstract boolean ultimateAvailable();

    //-----------------------------------
    //Overrides:

    @Override
    public String toString() {
        StringBuffer temp = new StringBuffer( String.format("(*%s) %s, %s ~ ",
                this.controller.toString(), this.role.name(), this.name ) );

        temp.append( " ".repeat( 40-temp.length() ) );

        temp.append( String.format("HP: (%d/%d)%s%s, St: (%d/100) %s%s",
                this.hitPoints, this.maxHP,
                " ".repeat( (this.hitPoints<100)? ( this.hitPoints<10 )?2:1 : 0  ),
                createBar((int)(10* this.hitPoints / (this.maxHP*1f) ), 10, GColor.GREEN),
                this.stamina,
                " ".repeat( (this.stamina<100)? ( this.stamina<10 )?2:1 : 0  ),
                createBar( (int)(6* (this.stamina /100f)), 6, GColor.RED)) );

        return temp.toString();
    }

    //-----------------------------------------------------------------------
    //FIELDS:

    //Party & Controller references:
    protected final Controller controller;
    protected final Party party;
    protected final Party opposition;

    //Basic hero information:
    private final String name;
    private final Role role;
    protected HashMap<String, String> actionNames;

    //Hero Attributes:
    private int maxHP;
    protected final int atk;
    protected final int def;
    protected final int magicDef;
    protected final int power;
    protected final double chance;
    protected static final double missChance;
    protected static final double fleeChance;
    private int atkBuff = 10;

    //Hero stats
    private int hitPoints;
    private boolean hasFallen = false;
    protected int limitBreak;
    protected int stamina=100;

    //-----------------------------------
    //Static variable initialization.
    static {
        missChance = 0.05;
        fleeChance = 0.05;
    }
}