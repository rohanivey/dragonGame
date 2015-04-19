package com.rohan.dragonGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Slot {

	private Item currentItem;
	private Slot masterSlot;
	private InventoryManager myIM;
	private int widthLoc, heightLoc;
	private Texture img = new Texture("box.png");
	private int slotWidth = img.getWidth();
	private int slotHeight = img.getHeight();
	private float clickCooldown = 0.2f;
	
	private slotType currentType = slotType.UNUSED;
	
	enum slotType  
		{
			MASTER, SLAVE, UNUSED
		}
	
	public Slot(InventoryManager inputIM,int inputHeight,int inputWidth)
	{
		myIM = inputIM;
		heightLoc = inputHeight;
		widthLoc = inputWidth;
		

	}
	
	public void update()
	{
		clickCooldown -= Gdx.graphics.getDeltaTime();
		//System.out.println(clickCooldown);
		if( (clickCooldown <= 0) & (Gdx.input.isTouched()) )
		{
			clickCooldown = 0.2f;
			//System.out.println("The player has clicked");
			if(checkCoords())
			{
				System.out.println("The player has clicked within a slot's bounds");
				System.out.println("Width: " + widthLoc + " Height: " + heightLoc);
				checkType();				
			}
		}
}
	
	
	public Boolean checkCoords()
	{
		float tempX = myIM.getCursor().getSprite().getX();
		float tempY = myIM.getCursor().getSprite().getY();
		
		//if the slot's starting location is less than the mouse x location and the slot's width at its end is greater than the mouse X
		if(widthLoc * slotWidth <= tempX && (widthLoc+1) * slotWidth - 1 >= tempX)
		{
			//AND if the slot's starting location is less than the mouse y location and the slot's height at its end is greater than the mouse Y
			if(heightLoc * slotHeight <= tempY && (heightLoc+1) * slotHeight - 1 >= tempY)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void checkType()
	{
		switch(currentType)
		{
			case MASTER:
				System.out.println("Master Input is being checked in Slot checkType()");
				checkMasterInput();
				break;
			case SLAVE:
				System.out.println("Slave Input is being checked in Slot checkType()");
				checkSlaveInput();
				break;
			case UNUSED:
				System.out.println("Unused Input is being checked in Slot checkType()");
				checkUnusedInput();
				break;
		}
	}
	
	public void setItem(Item inputItem)
	{
		currentItem = inputItem;
		currentType = slotType.MASTER;
	}
	
	public void setType(String inputType)
	{
		switch(inputType)
		{
		case "MASTER":
			currentType = slotType.MASTER;
			System.out.println("Setting type to MASTER in Slot.setType()");
			break;
		case "SLAVE":
			currentType = slotType.SLAVE;
			System.out.println("Setting type to SLAVE in Slot.setType()");
			break;
		case "UNUSED":
			currentType = slotType.UNUSED;
			System.out.println("Setting type to UNUSED in Slot.setType()");
			break;
		
		}
	}
	
	public void setMaster(Slot inputSlot)
	{
		masterSlot = inputSlot;
	}
	
	public Slot getMasterSlot(){return masterSlot;}
	public Item getCurrentItem(){return currentItem;}
	public String getCurrentType(){return currentType.toString();}
	
	public void checkMasterInput()
	{
		//If the master slot is touched
		if(Gdx.input.isTouched())
		{
			touchMaster();
		}
	}
	
	public void touchMaster()
	{
		//If the cursor has no item in hand
		if(myIM.getCursor().getItem() == null)
		{
			//Give it this slot's current item
			myIM.getCursor().setItem(currentItem);
			myIM.getCursor().setTexture();
			//Set this slot's current item to null
			currentItem = null;
			//Set this slot and all slaves to unused again
			currentType = slotType.UNUSED;
		}
		else if(myIM.getCursor().getItem() != null)
		{
			currentItem = myIM.getCursor().getItem();
			myIM.getCursor().defaultTexture();
			myIM.getCursor().setItem(null);
			currentType = slotType.MASTER;
		}
	}
	public void checkSlaveInput()
	{
		//If the slave slot is touched
		if(Gdx.input.isTouched())
		{
			//Make its master update touch
			masterSlot.touchMaster();
		}
	}
	public void checkUnusedInput()
	{
		//If the unused slot is touched
		if(Gdx.input.isTouched())
		{
			//If the cursor has an item in hand
			if(myIM.getCursor().getItem() != null)
			{
				//Set this current slot's item to the item in the player hand;
				currentItem = myIM.getCursor().getItem();
				//Set the item in the cursor's hand to null since it now exists in the slot
				myIM.getCursor().setItem(null);
				//This slot is now a master slot
				currentType = slotType.MASTER;
				masterSlot = this;
			}
		}
	}
	
	public void draw(int inputRow, int inputCol)
	{
		BitmapFont font = new BitmapFont();
		
		switch(currentType)
		{
			case UNUSED:
				myIM.sb.draw(img, widthLoc * slotWidth, heightLoc * slotHeight);
				font.draw(myIM.sb, "[" + inputRow + ", " + inputCol + "]: U", widthLoc * (slotWidth+32)+320, heightLoc * slotHeight+320);
				break;
			case MASTER:
				myIM.sb.draw(currentItem.getTexture(), widthLoc*slotWidth, heightLoc*slotHeight, slotWidth * currentItem.getGridWidth(), slotHeight * currentItem.getGridHeight());
				font.draw(myIM.sb,"[" + inputRow + ", " + inputCol + "]:M", widthLoc * (slotWidth+32)+320, heightLoc * slotHeight+320);
				break;
			case SLAVE:
				font.draw(myIM.sb,"[" + inputRow + ", " + inputCol + "]: S", widthLoc * (slotWidth+32)+320, heightLoc * slotHeight+320);
				break;
		}
	}
	
	
	
	
}
