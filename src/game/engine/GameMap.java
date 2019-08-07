
package game.engine;

import entities.AbstractEntity;
import entities.building.Building;
import entities.building.BuildingFactory;
import entities.building.BuildingImpl;
import entities.staticObjects.Ground;
import entities.staticObjects.Wall;
import entities.staticObjects.Water;
import entities.unit.Unit;
import entities.unit.UnitImpl;
import game.graphics.Color;
import game.graphics.Sprite;
import game.parts.Tile;
import game.pathfinding.Diffusion;
import geom.Dimension;
import geom.Point;

import java.util.ArrayList;
import java.util.List;

public class GameMap {

	private Player p1;
	private Player p2;
	private Dimension dim;
	private AbstractEntity[][][] e_map;// this might need to change
	private List<AbstractEntity> terrain;
	private List<AbstractEntity> units;
	private List<AbstractEntity> buildings;
	private List<AbstractEntity> entities;
	private static final GameMap gameInstance = new GameMap();
	private Diffusion pathFinder;// = new Diffusion();
	private int tiles_h;
	private int tiles_w;
	private final int levels = 3;
	private long last;
	private AIPlayer aiPlayer;


	private GameMap() {
		p1 = new Player("Farzon",Color.BLUE);
		p1.setMinerals(100);
		p2 = new Player("Computer",Color.RED);
	}

	public static GameMap getGameMapInstance() {
		return gameInstance;
	}


	public Player getPlayerOne() {
		return p1;
	}

	public Player getPlayerTwo() {
		return p2;
	}

	public AbstractEntity[][][] getEMap() {
		return e_map;
	}

	public boolean isOver() {
		Point nexus1 = p1.getNexusLocation();
		Point nexus2 = p2.getNexusLocation();
		if(e_map[nexus1.x][nexus1.y][GameConstants.BUILDING_LAYER] == null) {
			//lose
			return true;
		}
		if(e_map[nexus2.x][nexus2.y][GameConstants.BUILDING_LAYER] == null) {
			//win
			return true;
		}
		return false;
	}
	
	public boolean isWinner() {
		Point nexus1 = p1.getNexusLocation();
		if(e_map[nexus1.x][nexus1.y][GameConstants.BUILDING_LAYER] == null) {
			//lose
			return false;
		}
		return true;
	}
	
	public void update() {
		if (pathFinder == null) {
			pathFinder = new Diffusion();
		}
		if (aiPlayer == null) {
			aiPlayer = new AIPlayer();
		}
		updateMap();
		clearEMap();
		updateBuildings();
		updateUnits();
		removeDead();
		updatePlayers();
		aiPlayer.update();
	}

	public void updateMap() {
		pathFinder.diffuse();
		for(int i = 0; i < 3; i++)
			pathFinder.diffuse();
	}

	public void updatePlayers() {
		p1.clearList();
		p2.clearList();
		// update minerals by income
		if (Game.getTime() - last >= 20000) {
			last = Game.getTime(); //System.currentTimeMillis();
			p1.incMinerals(p1.getIncome());
			p2.incMinerals(p2.getIncome());
//			System.out.println(p1.getIncome());
		}
		
		for (AbstractEntity ae: units) {
			if (ae.getOwner().equals(p1)) {
				p1.getUnitList().add(ae);
				p1.getEntityList().add(ae);
			}
			else {
				p2.getUnitList().add(ae);
				p2.getEntityList().add(ae);
			}
		}
		
		for (AbstractEntity ae: buildings) {
			if (ae.getOwner().equals(p1)) {
				p1.getBuildingList().add(ae);
				p1.getEntityList().add(ae);
			}
			else {
				p2.getBuildingList().add(ae);
				p2.getEntityList().add(ae);
			}
		}
		
	}

	public void updateBuildings() {
		for (AbstractEntity ae : buildings) {
			BuildingImpl b = (BuildingImpl) ae;
			Point tiles = ae.getTile();
			e_map[tiles.x][tiles.y][GameConstants.BUILDING_LAYER] = ae;
			if (b.canBuild()) {
				int minerals = b.getOwner().getMinerals() + b.getIncome();
				b.getOwner().setMinerals(minerals);
				if(b.getBuildingType() != GameConstants.NEXUS_BLD) {
					AbstractEntity newUnit = (AbstractEntity) b.buildUnit();
				//	newUnit.getOwner().getUnitList().add((Unit) newUnit);
					ae.getOwner().getEntityList().add((AbstractEntity) newUnit);
					entities.add(newUnit);
					units.add(newUnit);
					((UnitImpl) newUnit).setLocation(new Point(ae.getLocation().x,ae.getLocation().y));
					
				}
				b.resetBuildTime();
			}
		}
	}
	
	public void addABuilding(Point pnt, int buildingType)
	{
		addABuilding(pnt,buildingType,p1);
	}
	
	public void addABuilding(Point pnt, int buildingType, Player p)
	{
		if(e_map[pnt.x][pnt.y][GameConstants.TERRAIN_LAYER].getPassable() && e_map[pnt.x][pnt.y][GameConstants.BUILDING_LAYER] == null) {
			switch(buildingType) {
			case 0: 
				e_map[pnt.x][pnt.y][GameConstants.BUILDING_LAYER] = (AbstractEntity) BuildingFactory.nexus(p, Tile.size*pnt.x, Tile.size*pnt.y);
				break;
			case 1: 
				e_map[pnt.x][pnt.y][GameConstants.BUILDING_LAYER] = (AbstractEntity) BuildingFactory.barracks(p, Tile.size*pnt.x, Tile.size*pnt.y);
				break;
			case 2: 
				e_map[pnt.x][pnt.y][GameConstants.BUILDING_LAYER] = (AbstractEntity) BuildingFactory.dojo(p, Tile.size*pnt.x, Tile.size*pnt.y);
				break;
			case 3: 
				e_map[pnt.x][pnt.y][GameConstants.BUILDING_LAYER] = (AbstractEntity) BuildingFactory.academy(p, Tile.size*pnt.x, Tile.size*pnt.y);
				break;
			case 4: 
				e_map[pnt.x][pnt.y][GameConstants.BUILDING_LAYER] = (AbstractEntity) BuildingFactory.rpgfactroy(p, Tile.size*pnt.x, Tile.size*pnt.y);
				break;
			case 5: 
				e_map[pnt.x][pnt.y][GameConstants.BUILDING_LAYER] = (AbstractEntity) BuildingFactory.tankfactory(p, Tile.size*pnt.x, Tile.size*pnt.y);
				break;
			}
			
			int temp = p.getMinerals() - ((BuildingImpl)e_map[pnt.x][pnt.y][GameConstants.BUILDING_LAYER]).getCost();
			if(temp >= 0) {
				p.getEntityList().add(e_map[pnt.x][pnt.y][GameConstants.BUILDING_LAYER]);
				entities.add(e_map[pnt.x][pnt.y][GameConstants.BUILDING_LAYER]);
				buildings.add(e_map[pnt.x][pnt.y][GameConstants.BUILDING_LAYER]);
				p.setMinerals(temp);
			}
			else {
				e_map[pnt.x][pnt.y][GameConstants.BUILDING_LAYER] = null;
			}
				
		}
	}
	
	public void clearEMap() {
		for(int i = 0; i < e_map[0].length; i++){
			for(int k = 0; k < e_map.length; k++){
				e_map[k][i][GameConstants.UNIT_LAYER]=null;
			}
		}
	}
	
	public void updateUnits() {
		
		for (AbstractEntity ae : units) {
			Point tiles = ae.getTile();
			e_map[tiles.x][tiles.y][GameConstants.UNIT_LAYER] = ae;
			
			if(ae.getOwner().equals(p1)){
				if (!((Unit) ae).attack(p2.getEntityList(), ae)) {//p2.getEntityList(), ae)) {
					ae.move(((UnitImpl) ae).getMoveSpeed(), pathFinder.getBestMove(p1, tiles));
				}
			}
			else if(ae.getOwner().equals(p2)){
				if (!((Unit) ae).attack(p1.getEntityList(), ae)) {//p1.getEntityList(), ae)) {
					ae.move(((UnitImpl) ae).getMoveSpeed(), pathFinder.getBestMove(p2, tiles));
				}
			}
			else{
				System.out.println("Error finding owner");
			}
		}
	}

	public void removeDead() {
		List<AbstractEntity> entitiesToRemove = new ArrayList<AbstractEntity>();
		for (AbstractEntity ae : entities) {
			if (ae.getCurrentHP() <= 0) {
				Point tiles = ae.getTile();
				if (ae instanceof Unit) {
					units.remove(ae);
//					ae.getOwner().getUnitList().remove(ae);
					e_map[tiles.x][tiles.y][GameConstants.UNIT_LAYER] = null;
				}
				if (ae instanceof Building) {
					buildings.remove(ae);
//					ae.getOwner().getBuildingList().remove(ae);
					e_map[tiles.x][tiles.y][GameConstants.BUILDING_LAYER] = null;
				}
				
				if (ae.getOwner().equals(p1)) {
					p2.incMinerals();
				}
				else {
					p1.incMinerals();
				}
				
//				ae.getOwner().getEntityList().remove(ae);
				entitiesToRemove.add(ae);
			}
		}
		for (AbstractEntity u : entitiesToRemove) {
			entities.remove(u);
		}
	}

	public void initDem(Dimension d) {
		this.dim = d;
		this.tiles_h = d.height/Tile.size;
		this.tiles_w = d.width/Tile.size;
		this.e_map = new AbstractEntity[this.tiles_w][this.tiles_h][levels];
		terrain    = new ArrayList<AbstractEntity>();
		units      = new ArrayList<AbstractEntity>();
		entities   = new ArrayList<AbstractEntity>();
		Sprite gnd = new Sprite(new Dimension(Tile.size,Tile.size),"../res/grass.png");
		buildings  = new ArrayList<AbstractEntity>();
		initWater();
		initWalls();
		//Ground
		for(int i = 0; i < tiles_h; i++)
		{
			for(int j = 0; j < tiles_w; j++)
			{
				if(e_map[j][i][GameConstants.TERRAIN_LAYER] == null)
				{
					Ground g = new Ground(gnd,
							new Point(j*Tile.size,i*Tile.size));
					e_map[j][i][GameConstants.TERRAIN_LAYER] = g;
				}
				terrain.add(e_map[j][i][GameConstants.TERRAIN_LAYER]);
				entities.add(e_map[j][i][GameConstants.TERRAIN_LAYER]);
			}
		}
		initBuildings();
	}

	private void initBuildings() {
		
		Point p1Nexus = new Point(1, 11);
		Point p2Nexus = new Point((dim.width-2*Tile.size)/Tile.size, 11);
		addABuilding(p1Nexus,GameConstants.NEXUS_BLD, p1);	
		addABuilding(p2Nexus,GameConstants.NEXUS_BLD, p2);
		p1.setNexusLocation(p1Nexus);
		p2.setNexusLocation(p2Nexus);
//		addABuilding(new Point(1, 1),GameConstants.Barrack_BLD, p1);
//		addABuilding(new Point(1, 2),GameConstants.Barrack_BLD, p1);
//		addABuilding(new Point((dim.width-2*Tile.size)/Tile.size, 21),GameConstants.RPG_BLD, p2);
//		addABuilding(new Point((dim.width-2*Tile.size)/Tile.size, 1),GameConstants.Barrack_BLD, p2);
//		addABuilding(new Point((dim.width-2*Tile.size)/Tile.size, 2),GameConstants.Barrack_BLD, p2);
//		addABuilding(new Point((dim.width-2*Tile.size)/Tile.size, 3),GameConstants.Barrack_BLD, p2);
//		addABuilding(new Point(1, 20),GameConstants.Barrack_BLD, p1);
		//addABuilding(new Point(0, 0),GameConstants.Barrack_BLD, p1);
		//p1.setMinerals(100);
		//p2.setMinerals(50);

		//entities.addAll(buildings);
	}

	private void  initWater() {
		Sprite watsp = new Sprite(new Dimension(Tile.size,Tile.size), "../res/water.png");
		//Water
		for(int i = tiles_h/4; i < 3*tiles_h/4;i++)
		{
			for(int j = tiles_w/4-1; j < 3*tiles_w/4+1;j++)
			{
				Water w = new Water(watsp,
						new Point(j*Tile.size,i*Tile.size));
				e_map[j][i][GameConstants.TERRAIN_LAYER] = w;

			}
		}
	}
	private void  initWalls() {
		Sprite wall = new Sprite(new Dimension(Tile.size,Tile.size),"../res/wall.png");
		//Wall top
		int j_w = 6;
		int j_w1 = 3*tiles_w/4 +1;
		//WallTop
		Wall wt1 = new Wall(wall,
				new Point(j_w*Tile.size,0));
		Wall wt2 = new Wall(wall,
				new Point(j_w1*Tile.size,0));
		e_map[j_w][0][GameConstants.TERRAIN_LAYER]  = wt1;
		e_map[j_w1][0][GameConstants.TERRAIN_LAYER] = wt2;

		//Wall Middle
		for(int i = 4; i < 2+3*tiles_h/4;i++) {
			Wall w = new Wall(wall,
					new Point(j_w*Tile.size,i*Tile.size));
			e_map[j_w][i][GameConstants.TERRAIN_LAYER] = w;

			Wall w1 = new Wall(wall,
					new Point(j_w1*Tile.size,i*Tile.size));
			e_map[j_w1][i][GameConstants.TERRAIN_LAYER] = w1;
		}

		//Wall Btm
		Wall wb1 = new Wall(wall,
				new Point(j_w*Tile.size,(tiles_h-1)*Tile.size));
		Wall wb2 = new Wall(wall,
				new Point(j_w1*Tile.size,(tiles_h-1)*Tile.size));
		e_map[j_w][tiles_h-1][GameConstants.TERRAIN_LAYER]  = wb1;
		e_map[j_w1][tiles_h-1][GameConstants.TERRAIN_LAYER] = wb2;
	}

	public List<AbstractEntity> getDrawList() {
		return entities;
	}

	public void rmABuilding(Point p, int curr_building) {
		// TODO Auto-generated method stub
		if(!(p.x == p1.getNexusLocation().x && p.y == p1.getNexusLocation().y))
		{
			entities.remove(e_map[p.x][p.y][GameConstants.BUILDING_LAYER]);
			buildings.remove(e_map[p.x][p.y][GameConstants.BUILDING_LAYER]);
			if(e_map[p.x][p.y][GameConstants.BUILDING_LAYER] != null)
			{
				int temp = p1.getMinerals() +((BuildingImpl)e_map[p.x][p.y][GameConstants.BUILDING_LAYER]).getCost()/2;
				p1.setMinerals(temp);
			}
			e_map[p.x][p.y][GameConstants.BUILDING_LAYER] = null;
			
		}
	}

}
