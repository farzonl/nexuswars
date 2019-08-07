package entities;

import game.engine.Player;
import geom.Dimension;
import geom.Point;

public interface Entity extends Describable {

	boolean getPassable();
	Point getLocation();
	Dimension getDimensions();
	Player getOwner();
	Point getTile();
	
}
