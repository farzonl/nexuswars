package entities.unit;

import java.util.List;

import entities.AbstractEntity;
import entities.Entity;

public interface Unit extends Entity {

	void damage(Unit attacker);

	int getMaxHP();
	int getCurrentHP();
	
	int getAttackPower(Armor armor);
	int getAttackSpeed();
	int getAttackRange();
	int getAttackRadius();
	boolean canAttack();
	long getCurrentAttackTime();
	void resetAttackTime();
	Armor getArmor();
	boolean attack(List<AbstractEntity> enemyEntityList, AbstractEntity unit);
	
	int getMoveSpeed();
	
}
