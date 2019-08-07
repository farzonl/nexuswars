package entities.unit;

import entities.AbstractEntity;
import entities.Description;
import game.engine.Game;
import game.engine.Player;
import game.graphics.Color;
import game.graphics.Sprite;
import geom.Dimension;
import geom.Point;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class UnitImpl extends AbstractEntity implements Unit {

	/*
	 * Immutable
	 */

	private final Player owner;
	private final Description description;
	private final int maxHP;
	private final int attackPower;
	private final int attackSpeed;
	private final int attackRange;
	private final int attackRadius;
	private final Armor armor;
	private final Map<Armor, Double> armorMultiplier;
	private final int moveSpeed;
	private final Sprite s;
	/*
	 * Mutable
	 */

	private int currentHP;
	private long lastAttackTime;

	private UnitImpl(Player owner, Description description, int maxHP, int attackPower,
			int attackSpeed, int attackRange, int attackRadius, Armor armor,
			Map<Armor, Double> armorMultiplier, int moveSpeed, Point location,
			Sprite s) {
		super(s, location);
		this.owner = owner;
		this.description = description;
		this.maxHP = maxHP;
		this.currentHP = maxHP;
		this.attackPower = attackPower;
		this.attackSpeed = attackSpeed;
		this.attackRange = attackRange;
		this.attackRadius = attackRadius;
		this.armor = armor;
		this.armorMultiplier = armorMultiplier;
		this.moveSpeed = moveSpeed;
		this.p = location;
		this.s = s;
		lastAttackTime = 0;
		this.owner.getUnitList().add(this);
		this.owner.getEntityList().add(this);
	}

	@Override
	public Player getOwner() {
		return owner;
	}
	
	@Override
	public boolean getPassable() {
		return false;
	}

	@Override
	public Point getLocation() {
		return p;
	}

	public Sprite getSprite() {
		return s;
	}

	@Override
	public void damage(Unit attacker) {
		this.currentHP -= attacker.getAttackPower(getArmor());
	}

	@Override
	public int getMaxHP() {
		return maxHP;
	}

	@Override
	public int getCurrentHP() {
		return currentHP;
	}

	@Override
	public int getAttackPower(Armor armor) {
		double multiplier = 1.0;
		if (armorMultiplier.containsKey(armor)) {
			multiplier = armorMultiplier.get(armor);
		}
		return (int) Math.max(1, Math.round(multiplier * attackPower));
	}

	@Override
	public int getAttackSpeed() {
		return attackSpeed;
	}

	@Override
	public int getAttackRange() {
		return attackRange;
	}

	@Override
	public int getAttackRadius() {
		return attackRadius;
	}

	@Override
	public Armor getArmor() {
		return armor;
	}

	@Override
	public int getMoveSpeed() {
		return moveSpeed;
	}

	@Override
	public Description getDescription() {
		return description;
	}

	public void setLocation(Point location) {
		this.p = location;
	}
	
	@Override
	public boolean canAttack() {
		if (getCurrentAttackTime() >= getAttackSpeed()) {
			return true;
		}
		return false;
	}

	@Override
	public long getCurrentAttackTime() {
		return (Game.getTime() - lastAttackTime);
	}


	@Override
	public void resetAttackTime() {
		lastAttackTime = Game.getTime();
	}
	
	@Override
	public boolean attack(List<AbstractEntity> enemyEntityList, AbstractEntity unit) {
		for (AbstractEntity ae : enemyEntityList) {
			if(!ae.getOwner().equals(unit.getOwner())) {
			if (inRange(unit, ae)) { 
				if (((Unit) unit).canAttack()) {
					ae.damage((Unit) unit);
					((Unit) unit).resetAttackTime();
				}
				return true;
			}
			}
		}
		return false;
	}
	
	/*
	 * Builder Shit
	 */

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private Player owner;
		private Description description;
		private int maxHP;
		private int attackPower;
		private int attackSpeed;
		private int attackRange;
		private int attackRadius;
		private Armor armor;
		private ImmutableMap.Builder<Armor, Double> armorMultiplier;
		private int moveSpeed;
		private Point location;
		private Dimension dimensions;
		private int currentHP;
		private long lastAttackTime;
		private Sprite s;

		private Builder() {
			this.description = new Description("Name", "Description");
			this.maxHP = 50;
			this.currentHP = maxHP;
			this.attackPower = 30;
			this.attackSpeed = 100;
			this.attackRange = 10;
			this.attackRadius = 1;
			this.armor = Armor.NONE;
			this.armorMultiplier = ImmutableMap.builder();
			this.moveSpeed = 1;
			this.location = new Point(100, 100);
			this.lastAttackTime = 0;
		}

		public UnitImpl build() {
			s.setColor(owner.getColor());
			return new UnitImpl(owner, description, maxHP, attackPower, attackSpeed,
					attackRange, attackRadius, armor, armorMultiplier.build(),
					moveSpeed, location, s);
		}

		public Builder owner(Player owner) {
			this.owner = owner;
			return this;
		}
		
		public Builder maxHP(int maxHP) {
			this.maxHP = maxHP;
			return this;
		}
		
		public Builder dimensions(Dimension dimensions) {
			this.dimensions = dimensions;
			return this;
		}

		public Builder attackPower(int attackPower) {
			this.attackPower = attackPower;
			return this;
		}

		public Builder attackSpeed(int attackSpeed) {
			this.attackSpeed = attackSpeed;
			return this;
		}

		public Builder attackRange(int attackRange) {
			this.attackRange = attackRange;
			return this;
		}

		public Builder attackRadius(int attackRadius) {
			this.attackRadius = attackRadius;
			return this;
		}

		public Builder armor(Armor armor) {
			this.armor = armor;
			return this;
		}

		public Builder multiplier(Armor armor, double factor) {
			armorMultiplier.put(armor, factor);
			return this;
		}
		
		public Builder moveSpeed(int moveSpeed) {
			this.moveSpeed = moveSpeed;
			return this;
		}

		public Builder location(Point location) {
			this.location = location;
			return this;
		}
		
		public Builder location(int x, int y) {
			this.location = new Point(x, y);
			return this;
		}

		public Builder description(Description description) {
			this.description = description;
			return this;
		}

		public Builder description(String name, String description) {
			this.description = new Description(name, description);
			return this;
		}
		
		public Builder sprite(Sprite s) {
			this.s = s;
			return this;
		}
		
	}
}
