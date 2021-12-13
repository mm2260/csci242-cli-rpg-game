package game.core;

import game.characters.Hero;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class HeroParty implements Party {

    private final String name;
    private final Queue<Hero> heroQueue = new LinkedList<>();
    private final Stack<Hero> fallenHeroes = new Stack<>();
    private int totalHealth = 0;

    public HeroParty( String name ){
        this.name = name;
    }

    @Override
    public int getTotalHealthPercentage() {
        int currentHealth = this.getTeam().stream().mapToInt( Hero::getCurrentHP ).sum();
        return (int) (100*(currentHealth / (float)(this.totalHealth)) );
    }

    @Override
    public List<Hero> getTeam() {
        return (List<Hero>) this.heroQueue;
    }

    @Override
    public List<Hero> getFallen() { return this.fallenHeroes; }

    @Override
    public void addHero(Hero hero) {
        if ( !hero.hasFallen() ){
            this.totalHealth = this.totalHealth + hero.getMaxHP();
            this.heroQueue.add(hero);
        } else { this.fallenHeroes.push(hero); }
    }

    @Override
    public Hero removeHero() {
        Hero hero = this.heroQueue.poll();
        this.totalHealth = this.totalHealth - hero.getMaxHP();
        return hero;
    }

    @Override
    public boolean hasFallen() {
        return !this.fallenHeroes.empty();
    }

    @Override
    public Hero revive() {
        return this.fallenHeroes.pop();
    }

    @Override
    public int numHeroes() {
        return this.heroQueue.size();
    }

    @Override
    public String getTeamName() {
        return this.name;
    }

    @Override
    public String toString() {
        StringBuffer temp = new StringBuffer( this.name+":\n" );
        this.getTeam().stream().map(Hero::toString).sorted().forEach( hero -> temp.append(hero).append('\n') );
        return temp.toString();
    }
}
