package game.graphics;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glViewport;
import entities.AbstractEntity;
import entities.building.Building;
import entities.building.BuildingFactory;
import game.engine.GameConstants;
import game.engine.GameMap;
import game.engine.Player;
import game.parts.Tile;
import geom.Dimension;
import geom.Point;

import java.awt.Font;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;



public class Menu {
	private String minerals, income, numUnits, numBuildings;
	
	private GameMap gm;
	private Dimension dim;
	private Font font;
	private boolean antiAlias = true;
	Sprite border;
	Sprite currBuild;
	
	private int curr_building = GameConstants.Barrack_BLD;
	public Menu(GameMap gm,Dimension dim)
	{
		this.gm = gm;
		this.dim = dim;
		border =new Sprite(new Dimension(dim.getWidth()/2+buildings.length*32
				,dim.getHeight()/4),Color.RED);
		font = new Font("Times New Roman", Font.ITALIC, 24);
		currBuild = new Sprite(new Dimension(Tile.size/2,Tile.size/2),Color.PURPLE);
		
		
	}
	private Point translatePoint()
	{
		int x = Mouse.getX();
		int y = dim.getHeight()-Mouse.getY();
		/*int x = x_raw - zoom*this.viewWidth;
		int y = y_raw - (zoom)*this.viewHeight;
		*/
		
		/*for (Building b : gm.getPlayerOne().getBuildingList()) {
			System.out.println("B: "+b.getLocation());
		}
		System.out.print("Display Height: "+Display.getHeight()); 
		System.out.print("Display Width: "+Display.getWidth()); */
		
		return new Point(x,y);
	}
	private void selectBuilding()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_1)) curr_building =1;
		else if (Keyboard.isKeyDown(Keyboard.KEY_2)) curr_building =2;
		else if (Keyboard.isKeyDown(Keyboard.KEY_3)) curr_building =3;
		else if (Keyboard.isKeyDown(Keyboard.KEY_4)) curr_building =4;
		else if (Keyboard.isKeyDown(Keyboard.KEY_5)) curr_building =5;
		else{}
	}
	
	private void updateStats()
	{
		
		income   = "Income: "+gm.getPlayerOne().getIncome();
		minerals = "Minerals: "+gm.getPlayerOne().getMinerals();
		numUnits = "Total Units: "+gm.getPlayerOne().getUnitList().size();
		numBuildings = "Total Buildings: "+gm.getPlayerOne().getBuildingList().size();
	}
	
	public void updateMenu(int zoom, int w, int h)
	{
		updateStats();
		selectBuilding();
		if(Mouse.isButtonDown(0) && zoom == 1)
		{
			Point p = translatePoint();
			//System.out.println(p.toString());
			p.x = (w+p.x)/Tile.size;
			p.y = (h+p.y)/Tile.size;
			//System.out.println(p.toString());
			if(p.x < 7)
				gm.addABuilding(p,curr_building);
			
		}
		if(Mouse.isButtonDown(1) && zoom == 1)
		{
			Point p = translatePoint();
			p.x = (w+p.x)/Tile.size;
			p.y = (h+p.y)/Tile.size;
			//System.out.println(p.toString());
			if(p.x < 7)
				gm.rmABuilding(p,curr_building);
		}
			
		
	}
	
	
	public void drawMenu()
	{
		
		glPushMatrix();
		glViewport(0, 0, dim.width, dim.height);

//		//glTranslatef(0.0f,.8f*dim.height, 0);
		glScalef(.2f,.2f,0);
		for (AbstractEntity entity : gm.getDrawList()) {
			entity.draw();
		}
		glScalef(4.0f,4.0f,0);
		
		glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
		
		border.draw(dim.width/4, 0);
		glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
		
		glColor3f(1.0f, 1.0f, 0.0f);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, dim.width, 0,dim.height, -1, 1);		
		SimpleText.drawString(income,dim.width/4+10,       180+dim.height);
		SimpleText.drawString(minerals,dim.width/4+10,     (int) (180+dim.height-(dim.height*.05)));
		SimpleText.drawString(numUnits,dim.width/4+10,     (int) (180+dim.height-(dim.height*.1)));
		SimpleText.drawString(numBuildings,dim.width/4+10, (int) (180+dim.height-(dim.height*.15)));
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, dim.width,dim.height,0, -1, 1);	
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		//font.drawString(dim.width/4+10,(int) (5), Income, org.newdawn.slick.Color.yellow);
		//font.drawString(dim.width/4+10,(int) (dim.height*.05), Minerals, org.newdawn.slick.Color.yellow);
		//font.drawString(dim.width/4+10,(int) (dim.height*.1),numUnits, org.newdawn.slick.Color.yellow);
		//font.drawString(dim.width/4+10,(int) (dim.height*.15), numBuildings, org.newdawn.slick.Color.yellow);
		int addVal = 0;
		for (Building b : buildings)
		{
			int x = dim.width/2 + addVal - 100;
			int y = 17;
			b.getSprite().draw(x, y);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, dim.width, 0,dim.height, -1, 1);		
			SimpleText.drawString("" + b.getCost(), x + 2, dim.height-(y-4));
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, dim.width,dim.height,0, -1, 1);	
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			
			
			addVal+=b.getDimensions().width;
		}
		
		currBuild.draw(dim.width/2+(curr_building)*Tile.size - 100,Tile.size+17);
		glPopMatrix();

		
	}
	
	// player 0 is left, 1 is right
	public static Building[] buildings;
	static {
		buildings = new Building[6];
		buildings[0] = BuildingFactory.nexus(Player.neutral, 0, 0);
		buildings[1] = BuildingFactory.barracks(Player.neutral, 0, 0);
		buildings[2] = BuildingFactory.dojo(Player.neutral, 0, 0);
		buildings[3] = BuildingFactory.academy(Player.neutral, 0, 0);
		buildings[4] = BuildingFactory.rpgfactroy(Player.neutral, 0, 0);
		buildings[5] = BuildingFactory.tankfactory(Player.neutral, 0, 0);
	}
}
