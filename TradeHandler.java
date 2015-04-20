package com.rohan.dragonGame;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TradeHandler{

	private Boolean trading = true;
	//private ArrayList<Item> playerInventoryCopy;
	//private ArrayList<Item> entityInventoryCopy;
	private InventoryManager playerInventoryCopy;
	private InventoryManager entityInventoryCopy;
	private InventoryManager tradeInventory;
	//private Lists list = Lists.Player;
	//private int currentSelectionPL = 0;
	//private int currentSelectionEL = 0;
	//private SpriteBatch sb;
	private BitmapFont font;
	//private OrthographicCamera cam;
	//private ShapeRenderer sr;
	//private float keyCooldown;
	private Boolean goodTrade = false;
	//ArrayList<Item> playerBarter;
	//ArrayList<Item> entityBarter;
	private String entityName;
	private Entity currentEntity;
	private Player player;
	
	
	
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
	}
	
	public Boolean checkPrices()
	{
		//If the value of the items the player is submitting is higher than the entity's
		if(getPlayerTotal() >= getEntityTotal())
		{
			return true;
		}
		return false;
	}
	
	public void concludeTrade()
	{
		setTrading(false);
		currentEntity.getDialogueHandler().setTrading(false);
		player.setStateChatting();
		player.setupTrading();
		//Tell the entity it's no longer trading
		//activeEntity.getDialogueHandler().setTrading(false);
		//Tell the player to go back to chat menu
		//currentState = State.Chatting;
		//Declare there is no longer a chat set up
		//tradeSetup = false;
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
	public void exchangeGoods()
	{
		for(int row = 0; row < playerInventoryCopy.gridHeight; row++)
		{
			for(int col = 0; col < playerInventoryCopy.gridWidth; col++)
			{
				//If the item in the slot is marked for trading
				if(tradeInventory.getGrid()[row][col].getCurrentItem() != null)
				{
					//If the item is owned by the player, it needs to go to the entity
					if(tradeInventory.getGrid()[row][col].getCurrentItem().getOwner() == "player")
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
					if(tradeInventory.getGrid()[row][col].getCurrentItem().getOwner() == "player")
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
					if(tradeInventory.getGrid()[row][col].getCurrentItem().getOwner() == "player")
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
	}
	
	public void Update()
	{
		playerInventoryCopy.update();
		entityInventoryCopy.update();
		tradeInventory.update();			
	}
	
	
}
