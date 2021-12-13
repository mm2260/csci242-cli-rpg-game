package game.characters.controllers;

import game.characters.Caster;
import game.characters.Hero;
import game.characters.Melee;
import game.characters.heroes.casters.Enchanter;
import game.characters.heroes.casters.Healer;
import game.characters.heroes.melee.Berserker;
import game.characters.heroes.melee.Knight;

public class CPU implements Controller {

    @Override
    public Action signalAction(Hero caller, int... characterState) {
        if ( caller instanceof Caster) {
            if (caller instanceof Enchanter) { return enchanterTree(characterState); }
            else if (caller instanceof Healer) { return healerTree(characterState); }
            else { return necromancerTree(characterState); }
        }
        else {
            if (caller instanceof Berserker) { return berserkerTree(characterState); }
            else if (caller instanceof Knight) { return knightTree(characterState); }
            else { return tankTree(characterState); }
        }
    }

    //-----------------------------------------------------------------------
    //Hero Behavior Trees:

    //Casters:
    private Action enchanterTree(int ... characterState ) {
        if (characterState[0] < 65) {
            if (characterState[0] < 40 ) {
                return ultimate(characterState);
            }
            else { return spell(characterState); }
        }
        else { return attack(characterState); }
    }

    private Action healerTree(int ... characterState ) {
        if (characterState[0] < 65) {
            if (characterState[0] < 40 ) {
                return ultimate(characterState);
            }
            else { return spell(characterState); }
        }
        else { return attack(characterState); }
    }

    private Action necromancerTree(int ... characterState ) {
        if (characterState[0] < 65) {
            if (characterState[0] < 30 ) {
                return ultimate(characterState);
            }
            else { return spell(characterState); }
        }
        else { return attack(characterState); }
    }

    //Melee:
    private Action berserkerTree(int ... characterState) {
        if (characterState[5]<characterState[6]/2) {
            return ultimate(characterState);
        }
        else { return attack(characterState); }
    }

    private Action knightTree(int ... characterState) {
        if (characterState[5]<characterState[6]/2) {
            return ultimate(characterState);
        }
        else { return attack(characterState); }
    }

    private Action tankTree(int ... characterState) {
        if (characterState[5]<characterState[6]/2) {
            return ultimate(characterState);
        }
        else { return attack(characterState); }
    }

    //-----------------------------------------------------------------------
    //Helper Functions:

    private Action ultimate(int ... characterState ) {
        if (characterState[1]!=-1 && characterState[1]>characterState[7]) {
            return Action.ULTIMATE;
        }
        else { return spell(characterState); }
    }

    private Action spell(int ... characterState) {
        if (characterState[2] > characterState[3]) {
            return Action.SPELL;
        }
        else { return attack(characterState); }
    }

    private Action attack(int ... characterState) {
        if (characterState[4]!=0) {
            return Action.ATTACK;
        }
        else { return flee(characterState); }
    }

    private Action flee( int ... characterState){
        if ( Math.random() > 0.5 ){
            return Action.FLEE;
        }
        else { return Action.DO_NOTHING; }
    }

    //-----------------------------------------------------------------------
    //Overrides:

    @Override
    public String toString() {
        return "C";
    }

}
