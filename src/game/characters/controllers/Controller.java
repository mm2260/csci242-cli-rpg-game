package game.characters.controllers;

import game.characters.Hero;

import java.util.List;

public interface Controller {

    public enum Type { CPU, PLAYER }
    public enum Action { ATTACK, SPELL, ULTIMATE, FLEE, DO_NOTHING }

    public Action signalAction(Hero hero, int ... characterState );

    @Override
    public String toString();
}
