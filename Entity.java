package com.rohan.dragonGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
	DialogueHandler dh;
	BitmapFont chatFont;
	SpriteBatch sb;
	Rectangle chatBox;
	ShapeRenderer sr;
	
	
	enum Type
	{
		Friendly, Neutral, Hostile
	}
	
	Entity(int inputX, int inputY, Player inputPlayer, String inputName, MainState inputMainState)
	{
		ms = inputMainState;
		location = new Vector2(inputX, inputY);
		p = inputPlayer;
		dh = new DialogueHandler(inputName);
		sb = new SpriteBatch();
		chatBox = new Rectangle(0,0,ms.getHUDCamera().viewportWidth, ms.getHUDCamera().viewportHeight/4);
		chatFont = new BitmapFont();
		sr = new ShapeRenderer();
		System.out.println(dh.produceDialogue(0));
		

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
	
	public abstract void interact();
	
	public void chat()
	{
		//Remember to set the player state to chatting when the sub entity interacts with the player
		sb.setProjectionMatrix(ms.getCamera().combined);
		sr.setColor(0, 0, 1, 0.5f);
		sr.setProjectionMatrix(ms.getCamera().combined);
		
		sr.begin(ShapeType.Filled);
		sr.rect(chatBox.x, chatBox.y, chatBox.width,chatBox.height);
		sr.end();
		
		sb.begin();
		chatFont.draw(sb, dh.produceDialogue(0), chatBox.x + 32, chatBox.y + chatBox.height-chatFont.getLineHeight()*2);
		sb.end();
	}



}
