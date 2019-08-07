package entities.staticObjects;

import entities.AbstractEntity;
import game.engine.Player;
import game.graphics.Sprite;
import game.parts.Tile;
import geom.Point;

public class Water extends AbstractEntity{
	private final Tile type = Tile.WATER;
	public Water(Sprite sprite, Point p) {
		super(sprite, p);
		// TODO Auto-generated constructor stub
	}
	
	public void move(long delta) {}

	@Override
	public Player getOwner() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean getPassable() {
		return false;
	}

}
