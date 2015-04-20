package com.rohan.dragonGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Cursor {

	private Item heldItem;
	private Sprite sprite;
	private Texture img = new Texture("pointer.png");
	private Texture tempImg;
	private int tempX, tempY;
	private SpriteBatch sb;
	
	public Cursor(InventoryManager inputIM)
	{
		sprite = new Sprite();
		sprite.setRegion(img);
		sb = new SpriteBatch();
		
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
		sb.begin();
		sb.draw(sprite, sprite.getX(), sprite.getY());
		sb.end();
	}
	
	public void checkInput(){}
	
	public void onClick(){}
	
	public Item getItem(){return heldItem;}
	
	public void setItem(Item inputItem){heldItem = inputItem;}
	
	public Sprite getSprite(){return sprite;}
	
	public void swapItem(Slot inputSlot)
	{
		//Get the item from the slot in spare hand
		Item tempItem = inputSlot.getCurrentItem();
		//Removes item we just got
		inputSlot.getIM().removeItem(inputSlot);
		//Add the item from main hand to slot
		inputSlot.getIM().addItem(inputSlot, heldItem);
		//Swap item in our off hand to main hand
		heldItem = tempItem;
		setTexture();
		
	}
	
	public Boolean testSwap(Slot inputSlot)
	{
		//Make a copy of everything involved so I don't mishandle an object
		InventoryManager tempIM = inputSlot.getIM().copyMe(); 
		Slot[][] tempGrid = tempIM.getGrid();
		Slot tempSlot = tempGrid[inputSlot.getRow()][inputSlot.getCol()];

		
		//Play with the copies
		tempIM.removeItem(tempSlot);
		if(tempIM.checkGridRoom(heldItem, tempSlot.getRow(), tempSlot.getCol()))
		{
			return true;
		}
		return false;
	}
	
	public Boolean testOwner(Slot inputSlot)
	{
		String slotOwner = inputSlot.getIM().getType().toString();
		String itemOwner = heldItem.getOwner();
		System.out.println("\n This slot belongs to " + slotOwner);
		System.out.println("\n This item belongs to " + itemOwner);
			//If the slot owner equals the item owner: ONLY TRUE FOR PLAYER
			if(slotOwner.equals(itemOwner))
				return true;
			//If the slot is owned by an entity and the item isn't owned by the player
			else if(slotOwner.equals("ENTITY") && !itemOwner.equals("PLAYER"))
			{
				return true;
			}
			else if(itemOwner.equals("none"))
				return true;
			else if(slotOwner.equals("TRADE"))
				return true;
		return false;
	}
	
	
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
