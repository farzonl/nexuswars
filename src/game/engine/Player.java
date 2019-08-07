package game.engine;

import entities.AbstractEntity;
import game.graphics.Color;
import geom.Point;

import java.util.ArrayList;
import java.util.List;

public class Player {

	public static final Player neutral = new Player("Neutral", Color.BLACK);
	
	private final String name;
	private int minerals;
	private List<AbstractEntity> unitList;
	private List<AbstractEntity> buildingList;
	private List<AbstractEntity> entityList;
	private int income;
	private Color color;
	private Point nexusLocation;


	public Player(String name, Color c) {
		this.name = name;
		unitList = new ArrayList<AbstractEntity>();
		buildingList = new ArrayList<AbstractEntity>();
		entityList = new ArrayList<AbstractEntity>();
		income = 1; // TODO: Make more dynamic
		this.color = c;
		this.minerals = 10;
	}
	
	
	public List<AbstractEntity> getEntityList() {
		return entityList;
	}
	
	public int getMinerals() {
		return minerals;
	}

	public void setMinerals(int minerals) {
		this.minerals = minerals;
	}
	
	public void incMinerals() {
		minerals += 1;
	}
	
	public void incMinerals(int num) {
		minerals += num;
	}

	public List<AbstractEntity> getUnitList() {
		return unitList;
	}

	public List<AbstractEntity> getBuildingList() {
		return buildingList;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public String getName() {
		return name;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Player) {
			return name.equals(((Player) obj).name);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		return name;
	}

	public Color getColor() {
		return color;
	}
	
	public Point getNexusLocation() {
		return nexusLocation;
	}

	public void clearList() {
		unitList.clear();
		buildingList.clear();
		entityList.clear();
	}
	public void setNexusLocation(Point nexusLocation) {
		this.nexusLocation = nexusLocation;
	}
	
	
}
