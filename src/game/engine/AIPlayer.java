package game.engine;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import entities.AbstractEntity;
import entities.building.BuildingImpl;
import entities.staticObjects.Wall;
import geom.Point;

public class AIPlayer {
	
	private Player computer;
	private Player player;
	private GameMap gameInstance;
	private Set<AbstractEntity> enemyTopList;
	private Set<AbstractEntity> enemyBottomList;
	private Set<AbstractEntity> topList;
	private Set<AbstractEntity> bottomList;
	private int points[] = {0,1,2,3,4,4};
	private int price[] = {0,5,8,10,15,15};
	private int buildingType[];
	private int top;
	private int bottom;
	private int build;
	private long last;
	private long startExpensive = 50000;
	
	public AIPlayer() {
		gameInstance = GameMap.getGameMapInstance();
		computer = gameInstance.getPlayerTwo();
		player = gameInstance.getPlayerOne();
		enemyTopList = new HashSet<AbstractEntity>();
		enemyBottomList = new HashSet<AbstractEntity>();
		topList = new HashSet<AbstractEntity>();
		bottomList = new HashSet<AbstractEntity>();
		top = 0;
		bottom = 0;
		buildingType = new int[6];
		build = 0;
		startExpensive += Game.getTime();
		last = 0;
	}
	
	public void update() {
		updateLists();
		whatToBuild();
	}
	
	private void whatToBuild() {
		Point placement = whereToBuild();
		if(placement == null) {
			return;
		}
		
//		System.out.println(computer.getMinerals());
		if(Game.getTime()-last > 10000) {
			build = 0;
			last = Game.getTime();
		}
		if(computer.getMinerals() >= price[build] && build != 0) {
			gameInstance.addABuilding(placement, build, computer);
			build = 0;
		}
		if(build == 0) { // nothing trying to build
			int total = 0;
			if(Game.getTime()<=startExpensive) { // || top > 0 || bottom > 0) {
				for(int i = 1; i < buildingType.length; i++) {
					total += buildingType[i];
				}
			}
			else {
				for(int i = 1; i < buildingType.length; i++) {
					total += buildingType[i]*price[i];
				}
			}
			Random r = new Random(System.currentTimeMillis());
			int choice = r.nextInt(total);
			if(Game.getTime()>startExpensive)
				choice-= buildingType[GameConstants.Barrack_BLD]*price[GameConstants.Barrack_BLD]; //DOJO_BLD];
			else
				choice-= buildingType[GameConstants.Barrack_BLD];
			if(choice < 0 && build == 0) {
				build = GameConstants.DOJO_BLD;
			}
			
			if(Game.getTime()>startExpensive)
				choice-= buildingType[GameConstants.DOJO_BLD]*price[GameConstants.ACAD_BLD];
			else
				choice-= buildingType[GameConstants.DOJO_BLD];
			if(choice < 0 && build == 0) {
				build = GameConstants.ACAD_BLD;
			}
			
			if(Game.getTime()>startExpensive)
				choice-= buildingType[GameConstants.ACAD_BLD]*price[GameConstants.Barrack_BLD];
			else
				choice-= buildingType[GameConstants.ACAD_BLD];
			if(choice < 0 && build == 0) {
				build = GameConstants.Barrack_BLD;
			}
			
			if(Game.getTime()>startExpensive)
				choice-= buildingType[GameConstants.SHIELD_BLD]*price[GameConstants.RPG_BLD];
			else
				choice-= buildingType[GameConstants.SHIELD_BLD];
			if(choice < 0 && build == 0) {
				build = GameConstants.RPG_BLD;
			}
			
			if(Game.getTime()>startExpensive)
				choice-= buildingType[GameConstants.RPG_BLD]*price[GameConstants.SHIELD_BLD];
			else
				choice-= buildingType[GameConstants.RPG_BLD];
			if(choice < 0 && build == 0) {
				build = GameConstants.SHIELD_BLD;
			}
			last = Game.getTime();
		}
	}
	
	private Point whereToBuild() {
		Point placement = null;
		int ylocation = computer.getNexusLocation().y;
		int xlocation = computer.getNexusLocation().x;
		AbstractEntity[][][] eMap = gameInstance.getEMap();

		while(!(eMap[--xlocation][ylocation][GameConstants.TERRAIN_LAYER] instanceof Wall));
		xlocation++;

		if(top<=bottom) {
			ylocation = 2;
		}
		else {
			ylocation = 20;
		}
		
				
		boolean emptySpot = false;
		
		for(int i = ylocation; i<eMap[0].length; i++) {
			for(int k = xlocation; k < eMap.length; k++) {
				if(eMap[k][i][GameConstants.BUILDING_LAYER] == null) {
					placement = new Point(k,i);
					emptySpot = true;
					break;
				}
			}
			if(emptySpot == true)
				break;
		}
		return placement;
	}
	
	private void updateLists() {
		List<AbstractEntity> enemyBuildings = player.getBuildingList();
		
		
		enemyTopList.clear();
		enemyBottomList.clear();
		topList.clear();
		bottomList.clear();
		for(int i = 0; i < enemyBuildings.size(); i++) {
			if(enemyBuildings.get(i).getLocation().y/32 <= player.getNexusLocation().y) {
				if(!enemyTopList.contains(enemyBuildings.get(i))) {
					enemyTopList.add(enemyBuildings.get(i));
				}
			}
			else {
				if(!enemyBottomList.contains(enemyBuildings.get(i))) {
					enemyBottomList.add(enemyBuildings.get(i));
				}
			}
		}
				
		List<AbstractEntity> buildings = computer.getBuildingList();
		
		for(int i = 0; i < buildings.size(); i++) {
			if(buildings.get(i).getLocation().y/32 <= computer.getNexusLocation().y) {
				if(!topList.contains(buildings.get(i))) {
					topList.add(buildings.get(i));
				}
			}
			else {
				if(!bottomList.contains(buildings.get(i))) {
					bottomList.add(buildings.get(i));
				}
			}
		}
		
		top = 0;
		for(int i = 0; i < buildingType.length; i++) {
			buildingType[i]=0;
		}
		buildingType[1]=1;
		buildingType[3]=1;
		if(Game.getTime()>startExpensive) {
			buildingType[2] = 1;
			buildingType[4] = 1;
			buildingType[5] = 1;
		}
		
		Iterator<AbstractEntity> iter = topList.iterator();
		while(iter.hasNext()) {
			AbstractEntity ae = iter.next();
			top+=points[((BuildingImpl)ae).getBuildingType()];
			if(buildingType[((BuildingImpl)ae).getBuildingType()]>1)
				buildingType[((BuildingImpl)ae).getBuildingType()]--;
		}
		
		iter = enemyTopList.iterator();
		while(iter.hasNext()) {
			AbstractEntity ae = iter.next();
			top-=points[((BuildingImpl)ae).getBuildingType()];
			buildingType[((BuildingImpl)ae).getBuildingType()]++;
		}
		
		bottom = 0;
		iter = bottomList.iterator();
		while(iter.hasNext()) {
			AbstractEntity ae = iter.next();
			bottom+=points[((BuildingImpl)ae).getBuildingType()];
			if(buildingType[((BuildingImpl)ae).getBuildingType()]>1)
				buildingType[((BuildingImpl)ae).getBuildingType()]--;
		}
		
		iter = enemyBottomList.iterator();
		while(iter.hasNext()) {
			AbstractEntity ae = iter.next();
			bottom-=points[((BuildingImpl)ae).getBuildingType()];
			buildingType[((BuildingImpl)ae).getBuildingType()]++;
		}
	}
}
