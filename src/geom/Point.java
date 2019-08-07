package geom;

public class Point {

	public  int x;
	public  int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public double euclidianDistance(Point p) {
		return Math.sqrt(Math.pow(this.y - p.y, 2) + Math.pow(this.x - p.x, 2));
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	
}
