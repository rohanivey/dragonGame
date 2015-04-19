package com.rohan.dragonGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Cursor {

	private Item heldItem;
	private Sprite sprite;
	private Texture img = new Texture("pointer.png");
	private Texture tempImg;
	private InventoryManager myIM;
	private int tempX, tempY;
	
	public Cursor(InventoryManager inputIM)
	{
		sprite = new Sprite();
		sprite.setRegion(img);
		myIM = inputIM;
		
	}
	
	public void update()
	{
		tempX = Gdx.input.getX();
		tempY = Gdx.input.getY();
		
		tempY = Gdx.graphics.getHeight() - tempY;

		
		sprite.setX(tempX);
		sprite.setY(tempY);
	}
	
	public void draw()
	{
		myIM.sb.draw(sprite, sprite.getX(), sprite.getY());
	}
	
	public void checkInput(){}
	
	public void onClick(){}
	
	public Item getItem(){return heldItem;}
	
	public void setItem(Item inputItem){heldItem = inputItem;}
	
	public Sprite getSprite(){return sprite;}
	
	
	public void setTexture()
	{
		//TODO: Find some way to get a copy of a texture so I can break links if need be
		tempImg = heldItem.getTexture();
		sprite.setRegion(tempImg);
	}
	
	
	public void defaultTexture()
	{
		sprite.setRegion(img);
	}
	
}
