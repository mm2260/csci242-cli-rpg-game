package game.characters.heroes;

import game.characters.Hero;
import game.characters.Role;
import game.characters.controllers.Controller;
import game.characters.heroes.casters.Enchanter;
import game.characters.heroes.casters.Healer;
import game.characters.heroes.casters.Necromancer;
import game.characters.heroes.melee.Berserker;
import game.characters.heroes.melee.Knight;
import game.characters.heroes.melee.Tank;
import game.core.GameManager;
import game.core.Party;

public class HeroFactory {

    public static Hero create(Role role, Controller.Type controllerType, Party party, Party opposition){

        String name = GameManager.getProperty( party.getTeamName()+"."+role.name() );

        switch (role) {
            case BERSERKER:
                return new Berserker(controllerType, name, party, opposition);
            case HEALER:
                return new Healer(controllerType, name, party, opposition);
            case ENCHANTER:
                return new Enchanter(controllerType, name, party, opposition);
            case KNIGHT:
                return new Knight(controllerType, name, party, opposition);
            case NECROMANCER:
                return new Necromancer(controllerType, name, party, opposition);
            case TANK:
                return new Tank(controllerType, name, party, opposition);
            default:
                return null;
        }
    }
}
