package game.core;

import game.characters.Hero;

import java.util.List;

public interface Party {

    public int getTotalHealthPercentage();

    public List<Hero> getTeam();

    public List<Hero> getFallen();

    public void addHero( Hero hero );

    public Hero removeHero();

    public boolean hasFallen();

    public Hero revive();

    public int numHeroes();

    public String getTeamName();
}
