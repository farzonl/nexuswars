package game.parts;

public enum Tile {
	
	GROUND(true), WATER(false), WALL(false);
	public static final int size = 32;
	public final boolean passable;
	
	private Tile(boolean passable) {
		this.passable = passable;
	}
	
}
