package com.rohan.dragonGame;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
	Vector2 chatLoc;
	ShapeRenderer sr;
	String[][] pKnowCopy;
	String myName;
	ArrayList<Item> inventory; 
	
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
		sb = ms.getSpriteBatch();
		chatBox = new Rectangle(0,0,ms.getHUDCamera().viewportWidth, ms.getHUDCamera().viewportHeight/4);
		chatFont = new BitmapFont();
		chatLoc = new Vector2(chatBox.x + 16, chatBox.y + chatBox.height - chatFont.getLineHeight());
		sr = new ShapeRenderer();
		myName = inputName;
		inventory = new ArrayList<Item>();



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
	public ArrayList<Item> getInventory(){return inventory;}
	
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
		
		//TODO: Add a Dialogue handler method here to refresh the selections available for chat
	}

		public DialogueHandler getDialogueHandler()
		{
			return dh;
		}
		
		public Rectangle getChatBox()
		{
			return chatBox;
		}
		
		public void setChatLocY(int inputY)
		{
			chatLoc.y = chatBox.height - chatFont.getLineHeight() - chatFont.getLineHeight()*inputY;
		}
		
		public Vector2 getChatLoc()
		{
			return chatLoc;
		}
		
		public float getChatLocY(int inputInt)
		{
			float y = chatBox.height - chatFont.getLineHeight() - chatFont.getLineHeight()*inputInt;
			return y;
		}
		
		public String getName(){return myName;}
		
		public void inputNewInventory(ArrayList<Item> inputInventory)
		{
			inventory = new ArrayList<Item>();
			for(Item i : inputInventory)
			{
				inventory.add(i);
			}
		}


}
