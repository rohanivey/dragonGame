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
	
	public void checkInput(){}
	
	public void defaultTexture()
	{
		sprite.setRegion(img);
	}
	
	public void draw()
	{
		sb.begin();
		sb.draw(sprite, sprite.getX(), sprite.getY());
		sb.end();
	}
	
	public Item getItem(){return heldItem;}
	
	public Sprite getSprite(){return sprite;}
	
	public void onClick(){}
	
	public void setItem(Item inputItem){heldItem = inputItem;}
	
	public void setTexture()
	{
		//TODO: Find some way to get a copy of a texture so I can break links if need be
		tempImg = heldItem.getTexture();
		sprite.setRegion(tempImg);
	}
	
	public void swapItem(Slot inputSlot)
	{
		//Get a safe copy of the item from the slot in spare hand
		Item tempItem = inputSlot.getCurrentItem().copyMe();
		System.out.println("Cursor.swapItem() now has a tempItem with the value of " + tempItem.getInputString());
		//Removes item we just got a copy of
		inputSlot.getIM().removeItem(inputSlot);
		System.out.println("Cursor.swapItem() has removed the original item in the inputSlot");
		//Add the item from main hand to slot
		inputSlot.getIM().addItem(inputSlot, heldItem);
		System.out.println("Cursor.swapItem() now has added the item it held into the InventoryManager at the slot");
		//Swap item in our off hand to main hand
		heldItem = tempItem;
		System.out.println("Cursor.SwapItem() now holds tempItem as its current heldItem");
		System.out.println("Cursor.swapItem() how has a heldItem with a value of " + heldItem.getInputString());
		setTexture();
		
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
	
	
	public Boolean testSwap(Slot inputSlot)
	{
		//Make a copy of everything involved so I don't mishandle an object
		System.out.println("Cursor.testSwap() is creating a copy of the inventory involved with the swap");
		InventoryManager tempIM = inputSlot.getIM().copyMe();
		System.out.println(inputSlot.getIM());
		System.out.println(tempIM);
		Slot[][] tempGrid = tempIM.getGrid();
		Slot tempSlot = tempGrid[inputSlot.getRow()][inputSlot.getCol()];
		System.out.println("Cursor.testSwap() how holds an item in tempSlot. The item is named: " + tempSlot.getCurrentItem().getInputString());
		

		
		//Play with the copies		
		tempIM.removeItem(tempSlot);
		System.out.println("Slot.testSwap() has now removed the item in tempSlot");
		if(tempIM.checkGridRoom(heldItem, tempSlot.getRow(), tempSlot.getCol()))
		{
			System.out.println("Cursor.testSwap() returned true");
			return true;
		}
		System.out.println("Cursor.testSwap() returned false");
		return false;
	}
	
	
	public void update()
	{
		tempX = Gdx.input.getX();
		tempY = Gdx.input.getY();
		
		tempY = Gdx.graphics.getHeight() - tempY;

		
		sprite.setX(tempX);
		sprite.setY(tempY);
	}
	
}
