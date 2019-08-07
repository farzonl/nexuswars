package game.engine;

import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.util.glu.GLU.*;

import entities.AbstractEntity;
import entities.building.Building;
import game.graphics.Color;
import game.graphics.Menu;
import game.graphics.Sprite;
import geom.Dimension;
import geom.Point;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import Intel.MediaSdk.*;

public class Game {

	private static final String WINDOW_TITLE = "World War Farzon";;
	private static boolean isRunning;
	private boolean fullscreen;
	private Dimension dim;

	private long lastLoopTime;
	private int fps;
	private long lastFpsTime;

	private GameMap gm;
	private Menu menu;
	int viewWidth, viewHeight, zoom = 1;
	private final int scroll_speed = 20;
	private boolean showMiniMap = true;
	private Sprite gameOverLose, gameOverWin;
	
	private FBOtoMediaSdk msdk_ogl;
	private boolean isEncoding = false;
	private boolean isEdown   = false;
	private boolean isHdown   = false;
	
	public Game(boolean fullscreen, int width, int height) {
		this.fullscreen = fullscreen;
		this.dim = new Dimension(width, height);
		this.viewWidth = 0;
		this.viewHeight = 0;
		// this.viewHeight = -2*dim.height;

		initialize();
	}

	public int getWidth() {
		return dim.width;
	}

	public int getHeight() {
		return dim.height;
	}

	/**
	 * Get the high resolution time in milliseconds
	 * 
	 * @return The high resolution time in milliseconds
	 */
	public static long getTime() {
		// we get the "timer ticks" from the high resolution timer
		// multiply by 1000 so our end result is in milliseconds
		// then divide by the number of ticks in a second giving
		// us a nice clear time in milliseconds
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public void initialize() {
		// initialize the window beforehand
		try {
			setDisplayMode();
			Display.setTitle(WINDOW_TITLE);
			Display.setFullscreen(fullscreen);
			Display.create();
			Game.isRunning = true;
			this.dim = new Dimension(Display.getWidth(), Display.getHeight());
			initEntities();

			// enable textures since we're going to use these for our sprites
			glEnable(GL_TEXTURE_2D);

			// disable the OpenGL depth test since we're rendering 2D graphics
			glDisable(GL_DEPTH_TEST);

			// GL11.glEnable(GL11.GL_BLEND);
			// GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();

			glOrtho(0, dim.width, dim.height, 0, -1, 1);

			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			// glViewport(0, 0, dim.height, dim.width);
			gameOverWin = new Sprite(this.dim, "../res/gameOverWinner.png");
			gameOverLose = new Sprite(this.dim, "../res/gameOverDefeat.png");

		} catch (LWJGLException le) {
			System.out.println("Game exiting - exception in initialization:");
			le.printStackTrace();
			Game.isRunning = false;
			return;
		}
		// add any game state init here
		this.msdk_ogl = new FBOtoMediaSdk(dim.width, dim.height);
	}

	private boolean setDisplayMode() {
		try {
			// get modes
			DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(
					dim.width, dim.height, -1, -1, -1, -1, 60, 60);
			// org.lwjgl.util.Display.setDisplayMode(dm, new String[1]);
			org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
					"width=" + dim.width,
					"height=" + dim.height,
					"freq=" + 60,
					"bpp="
							+ org.lwjgl.opengl.Display.getDisplayMode()
									.getBitsPerPixel() });
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Unable to enter fullscreen, continuing in windowed mode");
		}

		return false;
	}
	private void encodeSwitch()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) 
		{
			if(!isEdown)
			{
				isEdown = true;
				isEncoding = !isEncoding;
				if(isEncoding) System.out.println("Encoding Starting");
				else System.out.println("Encoding Stoping");
			}
		}
		else
		{
			isEdown = false;
		}
	}

	private void encodeSwap()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_H)) 
		{
			if(!isHdown)
			{
				isHdown = true;
				msdk_ogl.hw_swSwap();
				System.out.println("Swappin encoding type");
			}
		}
		else
		{
			isHdown = false;
		}
	}
	public void startLoop() {
		
		while (Game.isRunning) {
			encodeSwap();
			encodeSwitch();
			if(isEncoding)
				msdk_ogl.FBOsetup();
			
			// clear screen
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();

			if (gm.isOver()) {
				if (gm.isWinner()) {
					this.gameOverWin.draw(0, 0);
				} else {
					this.gameOverLose.draw(0, 0);
				}
			} else {
				// let subsystem paint
				frameRendering();

				// update window contents

				gm.update();
			}
			Display.update();
			if (Display.isCloseRequested()
					|| Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Game.isRunning = false;
			}
			if(isEncoding)
				msdk_ogl.processFrame();

		}

		// clean up
		// soundManager.destroy();

		Display.destroy();
		msdk_ogl.cleanUp();
	}

	public void frameRendering() {
		// SystemTimer.sleep(lastLoopTime+10-SystemTimer.getTime());
		Display.sync(60);

		long delta = getTime() - lastLoopTime;
		lastLoopTime = getTime();
		lastFpsTime += delta;
		fps++;

		// update our FPS counter if a second has passed
		if (lastFpsTime >= 1000) {
			Display.setTitle(WINDOW_TITLE + " (FPS: " + fps + ")");
			lastFpsTime = 0;
			fps = 0;
		}
		updateEntities(delta);
		drawEntities();
	}

	private static boolean released = true;
	private void updateEntities(long delta) {
		/*
		 * for (AbstractEntity entity : entities) { entity.move(delta); }
		 */
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			if (0 > this.viewWidth)
				this.viewWidth += scroll_speed;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			if (-zoom / 1.5 * dim.width < this.viewWidth)
				this.viewWidth -= scroll_speed;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			if (-zoom / 1.5 * dim.height < this.viewHeight) {
				this.viewHeight -= scroll_speed;
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			if (0 > this.viewHeight)
				this.viewHeight += scroll_speed;
		}

		int mWheel = Mouse.getDWheel();
		if (Keyboard.isKeyDown(Keyboard.KEY_X) || mWheel > 0) {
			if (zoom < 3)
				zoom++;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_Z) || mWheel < 0) {
			if (zoom > 1)
				zoom--;
			this.viewHeight = this.viewHeight / 2;
			this.viewHeight = this.viewHeight / 2;
		}

		else if (Keyboard.isKeyDown(Keyboard.KEY_M) && released) {
			showMiniMap = !showMiniMap;
			released = false;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_M)) {
			released = true;
			
		} else if (Keyboard.isKeyDown(Keyboard.KEY_F11)) {
			fullscreen = !fullscreen;
			try {
				Display.setFullscreen(fullscreen);
			} catch (LWJGLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// if(mWheel != 0) System.out.print(mWheel);
		menu.updateMenu(zoom, this.viewWidth, this.viewHeight);
	}

	void drawEntities() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		glPushMatrix();
		// glScalef(1.0f,.79f,0);
		glViewport(this.viewWidth, this.viewHeight, (zoom) * dim.width, (zoom)
				* dim.height);
		// glTranslatef(0.0f,-.2f*dim.height, 0);

		for (AbstractEntity entity : gm.getDrawList()) {
			entity.draw();
		}

		glPopMatrix();
		if (showMiniMap)
			menu.drawMenu();

	}

	private void initEntities() {
		gm = GameMap.getGameMapInstance();
		menu = new Menu(gm, dim);
		gm.initDem(dim);// NOTE initialize after passing gm to menu

	}

	public static void main(String argv[]) {
		Game g = new Game(false, 1280, 600);
		g.startLoop();
		System.exit(0);
	}
}
