package game.graphics;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import geom.Dimension;

import java.io.IOException;

public class Sprite {
	private TextureLoader loader = new TextureLoader();
	private boolean isTexture;
	private Texture texture;
	private Dimension dim;
	private Color color;

	private Sprite(Dimension dim) {
		this.dim = dim;
	}

	public Sprite(Dimension dim, Color c) {
		this(dim);
		this.isTexture = false;
		this.color = c;
	}

	public Sprite(Dimension dim, String ref) {
		this(dim);

		try {
			texture = loader.getTexture(ref);
			//dim = new Dimension(texture.getImageWidth(),texture.getImageHeight());
			this.isTexture = true;
			color = Color.WHITE;

		} catch (IOException ioe) {
			ioe.printStackTrace();
			
			this.isTexture = false;
		}
	}

	public void draw(int x, int y) {
		glPushMatrix();

		// bind to the appropriate texture for this sprite
		if (isTexture)
			texture.bind();

		// translate to the right location and prepare to draw
		glTranslatef(x, y, 0);

		// draw a quad textured to match the sprite
		glBegin(GL_QUADS);
		{
			if (isTexture)
				textureDraw();
			else
				colorDraw();

		}
		glEnd();
		if(isTexture)
			texture.release();

		// restore the model view matrix to prevent contamination
		glPopMatrix();

	}
	public void setDim(Dimension d)
	{
		this.dim = d;
	}
	public void setColor(Color c)
	{
		this.color = c;
	}

	public void textureDraw() {
		
		//glColor3f(1.0f, 1.0f, 1.0f);
		pickAcolor(color);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);

		glTexCoord2f(0, texture.getHeight());
		glVertex2f(0, dim.height);

		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(dim.width, dim.height);

		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(dim.width, 0);
	}

	public void colorDraw() {
		pickAcolor(null);
		glVertex2f(0, 0);
		glVertex2f(0, dim.height);
		glVertex2f(dim.width, dim.height);
		glVertex2f(dim.width, 0);
	}

	public void pickAcolor(Color c) {
		if (c != null)
			color = c;
		switch (color) {
		case RED:
			glColor3f(1.0f, 0.0f, 0.0f);
			break;
		case BLUE:
			glColor3f(0.0f, 0.0f, 1.0f);
			break;
		case GREEN:
			glColor3f(0.0f, 1.0f, 0.0f);
			break;
		case BLACK:
			glColor3f(0.0f, 0.0f, 0.0f);
			break;
		case PURPLE:
			glColor3f(1.0f, 0.0f, 1.0f);
			break;
		case TEAL:
			glColor3f(0.0f, 1.0f, 1.0f);
			break;
		case WHITE:
			glColor3f(1.0f, 1.0f, 1.0f);
			break;
		case YELLOW:
			glColor3f(1.0f, 1.0f, 0.0f);
			break;
		default:
			break;
		}

	}

	public Dimension getDimensions() {
		return dim;

	}

}
