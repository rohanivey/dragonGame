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
	Type type;
	Player p;
	
	enum Type
	{
		Friendly, Neutral, Hostile
	}
	
	Entity(int inputX, int inputY, Player inputPlayer)
	{
		location = new Vector2(inputX, inputY);
		p = inputPlayer;
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
	
	public void handleCollision(Player player)
	{
		if(Intersector.overlaps(player.getCollision(), collisionShape))
		{
			p.fullStop();
			switch(type)
			{
			case Friendly:
				System.out.println("Friendly collision detected with " + this.toString());
				break;
			case Hostile:
				System.out.println("Hostile collision detected with " + this.toString());
				break;
			default:
				System.out.println("Neutral collision detected with " + this.toString());
				break;
			
			}
			System.out.println("Collision with player detected!");
			collide();
		}
	}
	
	public void handleInteraction(Player player)
	{
		if(Intersector.overlaps(player.getInteraction(), collisionShape))
		{
			interact();
		}
	}
	
	public void collide(){}
	
	public void interact()
	{
		System.out.println("Interaction detected!");
	}
	


}
