package game.parts;

public class Grid<T> {

	private T[][] grid;
	
	public Grid(T[][] grid) {
		this.grid = grid;
	}
	
	public T[][] getGrid() {
		return grid;
	}
	
	public void setGrid(T[][] grid) {
		this.grid = grid;
	}
	
}
