package game.pathfinding;

import entities.AbstractEntity;
import entities.building.Building;
import entities.staticObjects.Wall;
import entities.staticObjects.Water;
import entities.unit.Unit;
import game.engine.GameConstants;
import game.engine.GameMap;
import game.engine.Player;
import geom.Point;

public class Diffusion {
	private AbstractEntity[][][] eMap;
	private int[][] p1Map;
	private int[][] p1MapImage;
	private int[][] p2Map;
	private int[][] p2MapImage;
	private int enemyUnit = 5000;
	private int enemyBuilding = 5000; // TODO: Add buildings
	private int friendlyUnit = 100;
	private GameMap gameMap;
	private Player p1;
	private Player p2;
	private int nexus = 5000;

	public Diffusion() {
		init();
	}

	public void init() {
		gameMap = GameMap.getGameMapInstance();
		p1 = gameMap.getPlayerOne();
		p2 = gameMap.getPlayerTwo();
		eMap = gameMap.getEMap();
		p1Map = new int[eMap.length][eMap[0].length];
		p1MapImage = new int[eMap.length][eMap[0].length];
		p2Map = new int[eMap.length][eMap[0].length];
		p2MapImage = new int[eMap.length][eMap[0].length];
		
		for (int i = 0; i < eMap[0].length; i++) {
			for (int k = 0; k < eMap.length; k++) {
				p1Map[k][i] = 100;
				p1MapImage[k][i] = 100;
				p2Map[k][i] = 100;
				p2MapImage[k][i] = 100;
			}
		}
		for(int i = 0; i < 10; i++)
			diffuse();
		// TODO: add nexus
	}

	public void diffuse() {
		for (int i = 0; i < eMap[0].length; i++) {
			for (int k = 0; k < eMap.length; k++) {
				p1MapImage[k][i] = updateValue(p1Map, i, k, p1);
				p2MapImage[k][i] = updateValue(p2Map, i, k, p2);
			}
		}

		for (int i = 0; i < eMap[0].length; i++) {
			for (int k = 0; k < eMap.length; k++) {
				p1Map[k][i] = updateValue(p1MapImage, i, k, p1);
				p2Map[k][i] = updateValue(p2MapImage, i, k, p2);
			}
		}
	}

	private int updateValue(int[][] grid, int i, int k, Player player) {
		Point point = new Point(k, i);
		Player otherPlayer = player.equals(p1) ? p2 : p1;
		if (eMap[k][i][GameConstants.UNIT_LAYER] instanceof Unit) { // && !(eMap[k][i][GameConstants.BUILDING_LAYER] instanceof Building)) {
//			System.out.println("is unit at: (" + k + ", " + i + ")" + (Unit) (eMap[k][i][GameConstants.UNIT_LAYER]));
//			return eMap[k][i][GameConstants.UNIT_LAYER].getOwner().equals(player) ? friendlyUnit : enemyUnit;
			if (!eMap[k][i][GameConstants.UNIT_LAYER].getOwner().equals(player)) {
				return enemyUnit;
			}
		}
		if (eMap[k][i][GameConstants.BUILDING_LAYER] instanceof Building) {
			//return eMap[k][i][GameConstants.BUILDING_LAYER].getOwner().equals(player) ? grid[k][i]
				//	: enemyBuilding;
			if (!eMap[k][i][GameConstants.BUILDING_LAYER].getOwner().equals(player) && point.equals(otherPlayer.getNexusLocation())) {
				return nexus;
			}
			if (!eMap[k][i][GameConstants.BUILDING_LAYER].getOwner().equals(player)) {
				return enemyBuilding;
			}
			
		}
		if (eMap[k][i][GameConstants.TERRAIN_LAYER] instanceof Water || eMap[k][i][GameConstants.TERRAIN_LAYER] instanceof Wall) {
			return 0;
		}

		int newValue = 0;
		int numValues = 0;

		if (isPassable(i - 1, k)) {
			numValues++;
			newValue += grid[k][i - 1];
		}

		if (isPassable(i, k + 1)) {
			numValues++;
			newValue += grid[k + 1][i];
		}

		if (isPassable(i + 1, k)) {
			numValues++;
			newValue += grid[k][i + 1];
		}

		if (isPassable(i, k-1)) {
			numValues++;
			newValue += grid[k-1][i];
		}

		// TODO: Consider using current value in calculation

		return numValues == 0 ? grid[k][i] : newValue / numValues;

	}

	private boolean isPassable(int i, int k) {
		
		return !((i < 0) || i >= eMap[0].length || k < 0 || k >= eMap.length
				|| eMap[k][i][GameConstants.TERRAIN_LAYER] instanceof Water || eMap[k][i][GameConstants.TERRAIN_LAYER] instanceof Wall);
	}

	public Move getBestMove(Player p, Point point) {
		int grid[][] = p1.equals(p) ? p1Map : p2Map;
		int i = point.y;
		int k = point.x;
		int currMax = 0;
		Move bestMove = Move.STAY;
		if (isPassable(i, k+1) && isPassable(i+1, k+1)) {
			if (currMax <= grid[k+1][i]) {
				currMax = grid[k+1][i];
				bestMove = Move.EAST;
			}
		}
		
		if (isPassable(i, k-1) && isPassable(i+1, k-1)) {
			if (currMax <= grid[k-1][i]) {
				currMax = grid[k-1][i];
				bestMove = Move.WEST;
			}
		}
		
		if (isPassable(i-1, k) && isPassable(i-1, k+1)) {
			if (currMax < grid[k][i-1]) {
				currMax = grid[k][i-1];
				bestMove = Move.NORTH;
			}
		}
				
		if (isPassable(i+1, k) && isPassable(i+1, k+1)) {
			if (currMax < grid[k][i+1]) {
				currMax = grid[k][i+1];
				bestMove = Move.SOUTH;
			}
		}
		
		return bestMove;
		
	}
}
