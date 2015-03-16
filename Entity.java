package com.rohan.dragonGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public abstract class Entity {
	
	MainState ms;
	Rectangle collisionShape;
	Vector2 location;
	
	Entity(int inputX, int inputY)
	{
		location = new Vector2(inputX, inputY);
	}
	
	public void update()
	{
	}
	
	public Rectangle getCollision(){return collisionShape;}
	public void setCollision(Texture inputImg){collisionShape = new Rectangle(location.x, location.y, inputImg.getWidth(), inputImg.getHeight());}
	public Circle getInteraction(){return null;}
	public abstract Texture getTexture();
	public abstract TextureRegion getTextureRegion();
	public float getX(){return location.x;}
	public float getY(){return location.y;}
	
	public void handleCollision(Entity e)
	{
		if(Intersector.overlaps(e.getCollision(), collisionShape))
		{
			System.out.println("Collision with player detected!");
		}
	}
	
	public void handleInteraction(Entity e)
	{
		if(Intersector.overlaps(e.getInteraction(), collisionShape))
		{
			interact();
		}
	}
	
	public void interact()
	{
		System.out.println("Interaction detected!");
	}
	


}
