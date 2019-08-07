package entities.staticObjects;

import entities.AbstractEntity;
import game.engine.Player;
import game.graphics.Sprite;
import game.parts.Tile;
import geom.Point;

public class Ground extends AbstractEntity{
	private final Tile type = Tile.GROUND;
	public Ground(Sprite sprite, Point p) {
		super(sprite, p);
	}
	
	public void move(long delta) {}

	@Override
	public Player getOwner() {
		return null;
	}
	
	@Override
	public boolean getPassable() {
		return true;
	}
}
