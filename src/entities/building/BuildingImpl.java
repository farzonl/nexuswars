package entities.building;

import entities.AbstractEntity;
import entities.Description;
import entities.Producer;
import entities.unit.Armor;
import entities.unit.Unit;
import game.engine.Game;
import game.engine.Player;
import game.graphics.Sprite;
import geom.Dimension;
import geom.Point;

public class BuildingImpl extends AbstractEntity implements Building {
	
	private final Player owner;
	private final Dimension dimensions;
	private final Producer<Unit> unitProducer;
	private final int cost;
	private final int buildTime;
	private final int maxHP;
	private final Description description;
	private long lastTimeSpawned;
	private int currentHP;
	private Armor armor;
	private int income;
	private int buildingType;

	private BuildingImpl(Player owner, Description descript, int maxHP, Point location, Dimension dimension, Producer<Unit> unitProducer,
			int cost, int buildTime, int currentHP, long lastTimeSpawned,int income,int btype, Sprite s) {
		super(s, location);
		this.owner = owner;
		this.p = location;
		this.dimensions = dimension;
		this.unitProducer = unitProducer;
		this.cost = cost;
		this.buildTime = buildTime;
		this.maxHP = maxHP;
		this.description = descript;
		this.currentHP = currentHP;
		this.lastTimeSpawned = lastTimeSpawned;
		this.income = income;
		this.buildingType = btype;
	}
	
	@Override
	public Player getOwner() {
		return owner;
	}
	public int getBuildingType()
	{
		return buildingType;
	}
	
	@Override
	public boolean getPassable() {
		return true;
	}

	@Override
	public Point getLocation() {
		return p;
	}

	@Override
	public Dimension getDimensions() {
		return dimensions;
	}
	
	public void move(long delta) {}

	@Override
	public Description getDescription() {
		return description;
	}

	@Override
	public Unit buildUnit() {
		if (canBuild()) {
			resetBuildTime();
			return unitProducer.produce();
		}
		else {
			return null; // TODO: Gotta be a better solution
		}
		
	}
	@Override
	public boolean canBuild() {
		if (getCurrentBuildTime() >= getMaxBuildTime()) {
			return true;
		}
		return false;
	}

	@Override
	// TODO : Add timer implementation
	public int getCurrentBuildTime() {
		return (int) ((int) Game.getTime() - lastTimeSpawned);
	}

	@Override
	public int getMaxBuildTime() {
		return buildTime;
	}

	@Override
	// TODO : Add timer implementation
	public void resetBuildTime() {
		lastTimeSpawned = Game.getTime();
	}
	
	@Override
	public Sprite getSprite() {
		return super.sprite;
	}

	@Override
	public int getCurrentHP() {
		return currentHP;
	}

	@Override
	public int getMaxHP() {
		return maxHP;
	}
	
	public int getCost() {
		return cost;
	}
	
	public int getIncome()
	{
		return income;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	@Override
	public Armor getArmor() {
		return armor;
	}

	public static class Builder {
		
		private Player owner;
		private Point location;
		private Dimension dimensions;
		private Producer<Unit> unitProducer;
		private int cost;
		private int buildTime;
		private int maxHP;
		private Description description;
		private long lastTimeSpawned;
		private int income;
		private int buildingType;
		private Sprite sprite;
		
		private Builder() {
			location = null;
			dimensions = new Dimension(32,32);
			unitProducer = null;
			cost = 5;
			buildTime = 1000;
			maxHP = 5000;
			lastTimeSpawned = Game.getTime();
			description = new Description("Farzon", "Produces weak units");
		}
		
		public BuildingImpl build() {
			//sprite.setColor(owner.getColor());
			return new BuildingImpl(owner, description, maxHP, location, dimensions, 
					unitProducer, cost, buildTime, maxHP, lastTimeSpawned,income, buildingType, sprite);
		}
		
		public Builder owner(Player owner) {
			this.owner = owner;
			return this;
		}
		
		
		public Builder addIncome(int income) {
			this.income = income;
			return this;
		}
		
		public Builder setBuildingType(int btype)
		{
			this.buildingType = btype;
			return this;
		}
		
		public Builder location(Point location) {
			this.location = location;
			return this;
		}
		
		public Builder Dimension(Dimension dimensions) {
			this.dimensions = dimensions;
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
		
		public Builder maxHP(int maxHP) {
			this.maxHP = maxHP;
			return this;
		}
		
		public Builder cost(int cost) {
			this.cost = cost;
			return this;
		}
		
		public Builder buildTime(int buildTime) {
			this.buildTime = buildTime;
			return this;
		}
		
		public Builder unitProducer(Producer<Unit> unitProducer) {
			this.unitProducer = unitProducer;
			return this;
		}

		public Builder sprite(Sprite sprite) {
			this.sprite = sprite;
			return this;
		}
		
	}

	@Override
	public void damage(Unit attacker) {
		this.currentHP -= attacker.getAttackPower(getArmor());
	}

}
