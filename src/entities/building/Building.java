package entities.building;

import entities.Entity;
import entities.unit.Armor;
import entities.unit.Unit;
import game.engine.Player;
import game.graphics.Sprite;

public interface Building extends Entity {

	Unit buildUnit();
	
	int getCurrentBuildTime();
	int getMaxBuildTime();
	void resetBuildTime();
	boolean canBuild();
	Player getOwner();
	int getCurrentHP();
	int getMaxHP();
	void damage(Unit attacker);
	Armor getArmor();
	
	int getCost();
	Sprite getSprite();
}
