package entities;



import entities.unit.Unit;
import game.engine.GameConstants;
import game.engine.GameMap;
import game.engine.Player;
import game.graphics.Sprite;
import game.pathfinding.Move;
import geom.Dimension;
import geom.Point;

import java.util.List;

public abstract class AbstractEntity implements Entity {
	
	protected Sprite sprite;
	protected Point p;
	protected float	dx = 0;
	protected float	dy =  0;
	protected GameMap gameMap = GameMap.getGameMapInstance();
	protected AbstractEntity[][][] e_map = gameMap.getEMap();
	
	
	protected AbstractEntity(Sprite sprite, Point p) {
		this.sprite = sprite;
		this.p = p;
	}
	
	
	public void move(long delta) {
		// update the location of the entity based on move speeds
		if (getOwner().equals(gameMap.getPlayerOne())) {
			dx = 32;
		}
		else
		{
			dx = -32;
		}
		Point newPoint = new Point((int) ((p.x + (delta * dx)) / 32), 
				(int)((p.y + (delta * dy)) / 32));
		if (e_map[newPoint.x][newPoint.y][0].getPassable()) {
			p.x = newPoint.x * 32;
			p.y = newPoint.y * 32; /// 1000;
		}
		dx = 0;
		dy = 0;
	}
	
	public void move(long delta, Move direction) {
		// update the location of the entity based on move speeds
		if (direction == Move.EAST) {
			dx = 32;
		}
		else if (direction == Move.WEST){
			dx = -32;
		}
		else if (direction == Move.NORTH){
			dy = -32;
		}
		else if (direction == Move.SOUTH){
			dy = 32;
		}

		Point newPoint = new Point((int) ((p.x + (delta * dx)) / 32), (int)((p.y + (delta * dy)) / 32));
		if (getPassable(newPoint) && GameMap.getGameMapInstance().getEMap()[newPoint.x][newPoint.y][GameConstants.TERRAIN_LAYER].getPassable()) {
			p.x += (delta * dx) / 32;
			p.y += (delta * dy) / 32;
		}
		dx = 0;
		dy = 0;
	}
	
	public abstract Player getOwner();
	
	public void draw() {
		this.sprite.draw(p.x, p.y);
	}
	
	
	@Override
	public Description getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getPassable() {
		return false;
	}
	
	public boolean getPassable(Point p) {
		return !(p.x < 0 || p.x >= e_map.length || p.y < 0 || p.y >= e_map[0].length);
	}

	@Override
	public Point getLocation() {
		// TODO Auto-generated method stub
		return p;
	}

	@Override
	public Dimension getDimensions() {
		// TODO Auto-generated method stub
		return sprite.getDimensions();
	}
	
	public Point getTile() {
		return new Point(p.x/32,p.y/32);
	}
	
	public void damage(Unit unit) {
		
	}


	public boolean inRange(AbstractEntity source, AbstractEntity targ) {
		return ((Unit) source).getAttackRange() >= source.getLocation().euclidianDistance(targ.getLocation());
	}


	public int getCurrentHP() { // TODO: Fix. This is done so everything has default hp > 0
		return 1;
	}

}
