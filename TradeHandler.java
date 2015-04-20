package com.rohan.dragonGame;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TradeHandler{

	private Boolean trading = true;
	private InventoryManager playerInventoryCopy;
	private InventoryManager entityInventoryCopy;
	private InventoryManager tradeInventory;
	private BitmapFont font;
	private Boolean goodTrade = false;
	private String entityName;
	private Entity currentEntity;
	private Player player;
	private int playerCoins, entityCoins;
	
	
	
	TradeHandler(Player inputPlayer, Entity inputEntity)
	{
		font = new BitmapFont();
		player = inputPlayer;
		playerInventoryCopy = inputPlayer.getInventoryManager().copyMe();
		currentEntity = inputEntity;
		currentEntity.setupTrade();
		entityInventoryCopy = inputEntity.getInventoryManager().copyMe();
		entityName = currentEntity.getName();
		tradeInventory = new InventoryManager(this);
		playerCoins = player.getCoins();
		entityCoins = currentEntity.getCoins();
	}
	
	public Boolean checkPrices()
	{
		int playerWorth = getPlayerTotal() + playerCoins;
		//If the value of the items the player is submitting is higher than the entity's
		if(getPlayerTotal() >= getEntityTotal())
		{
			return true;
		}
		//Otherwise, if the value of the player's coin + traded items is greater than the value the entity's trade goods
		else if(playerWorth >= getEntityTotal())
		{
			return true;
		}
		//Otherwise, the player doesn't have enough net worth to trade for what they want
		return false;
	}
	
	public Boolean checkPricesAndCoins()
	{
		int playerWorth = getPlayerTotal() + playerCoins;
		//Otherwise, if the value of the player's coin + traded items is greater than the value the entity's trade goods
		if(playerWorth >= getEntityTotal())
		{
			return true;
		}
		//Otherwise, the player doesn't have enough net worth to trade for what they want
		return false;
	}
	
	public void concludeTrade()

	{
		player.setInventoryManager(playerInventoryCopy.copyMe());
		currentEntity.setInventoryManager(entityInventoryCopy.copyMe());
		player.setCoins(playerCoins);
		currentEntity.setCoins(entityCoins);
		setTrading(false);
		currentEntity.getDialogueHandler().setTrading(false);
		player.setStateChatting();
		player.setupTrading();
		
	}
	
	public void draw()
	{
		
		if(entityInventoryCopy.getReady())
		{
			entityInventoryCopy.draw();			
		}

		if(tradeInventory.getReady())
		{
			tradeInventory.draw();
		}
		
		if(playerInventoryCopy.getReady())
		{
			playerInventoryCopy.draw();
		}	
	}
	
	public void exchangeCoins()
	{
		//TODO: IMPLEMENT BARTER SKILL TO ADJUST THE AMOUNT OF COIN PAID?
		//If the value of the entity's goods are greater than the player's goods, the player should pay money
		if(getEntityTotal() > getPlayerTotal())
		{
			int difference = getEntityTotal() - getPlayerTotal();
			playerCoins -= difference;
			entityCoins += difference;
			
		}
		//Else if the value of the player's goods are greater than the entity's, the entity should pay money
		else if(getPlayerTotal() > getEntityTotal())
		{
			int difference = getPlayerTotal() - getEntityTotal();
			playerCoins += difference;
			entityCoins -= difference;
		}
		
	}
	public void exchangeGoods()
	{
		for(int row = 0; row < tradeInventory.gridHeight; row++)
		{
			for(int col = 0; col < tradeInventory.gridWidth; col++)
			{
				//If the item in the slot is marked for trading
				if(tradeInventory.getGrid()[row][col].getCurrentItem() != null)
				{
					//If the item is owned by the player, it needs to go to the entity
					if(tradeInventory.getGrid()[row][col].getCurrentItem().getOwner() == "PLAYER")
					{
						//Set the owner of the item for trade as the entity
						tradeInventory.getGrid()[row][col].getCurrentItem().setOwner(entityName);
						//Add the item to the entity's inventory
						entityInventoryCopy.addItem(tradeInventory.getGrid()[row][col].getCurrentItem());
						//Remove the item from the player's inventory
						tradeInventory.removeItem(tradeInventory.getGrid()[row][col]);
					}
					//Else, if the item is owned by the entity, the player needs to take it
					else 
					{
						//Set the owner of the item for trade as the player
						tradeInventory.getGrid()[row][col].getCurrentItem().setOwner("player");
						//Add the item to the entity's inventory
						playerInventoryCopy.addItem(tradeInventory.getGrid()[row][col].getCurrentItem());
						//Remove the item from the trade inventory
						tradeInventory.removeItem(tradeInventory.getGrid()[row][col]);	
					}
				}
			}
		}
	}
	public Entity getCurrentEntity(){return currentEntity;}
	public InventoryManager getEICopy(){return entityInventoryCopy;}
	public int getEntityTotal()
	{
		int entityTotal = 0;
		for(int row = 0; row < tradeInventory.gridHeight; row++)
		{
			for(int col = 0; col < tradeInventory.gridWidth; col++)
			{
				//If the item in the slot is marked for trading
				if(tradeInventory.getGrid()[row][col].getCurrentItem() != null)
				{
					if(tradeInventory.getGrid()[row][col].getCurrentItem().getOwner() == "PLAYER")
					{
					}
					else 
					{
						entityTotal += tradeInventory.getGrid()[row][col].getCurrentItem().getValue();
					}
				}
			}
		}
		return entityTotal;
	}
	public BitmapFont getFont(){return font;}
	public Boolean getGoodTrade(){return goodTrade;}
	public Cursor getMainCursor(){return playerInventoryCopy.getCursor();}
	public InventoryManager getPBCopy(){return tradeInventory;}
	
	public InventoryManager getPICopy(){return playerInventoryCopy;}
	
	public int getPlayerTotal()
	{
		int playerTotal = 0;
		for(int row = 0; row < tradeInventory.gridHeight; row++)
		{
			for(int col = 0; col < tradeInventory.gridWidth; col++)
			{
				//If the item in the slot is marked for trading
				if(tradeInventory.getGrid()[row][col].getCurrentItem() != null)
				{
					if(tradeInventory.getGrid()[row][col].getCurrentItem().getOwner() == "PLAYER")
					{
						playerTotal += tradeInventory.getGrid()[row][col].getCurrentItem().getValue();
					}
				}
			}
		}
		return playerTotal;
	}
	
	public Boolean getTrading(){return trading;}
	
	public void setTrading(Boolean inputBoolean){trading = inputBoolean;}

	public void tryTrade()
	{
		if(checkPrices())
		{
			exchangeGoods();
			concludeTrade();
		}
		else if(checkPricesAndCoins())
		{
			exchangeCoins();
			exchangeGoods();
			concludeTrade();
		}
	}
	
	public void Update()
	{
		playerInventoryCopy.update();
		entityInventoryCopy.update();
		tradeInventory.update();			
	}
	
	
}
