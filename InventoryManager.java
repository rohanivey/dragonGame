package com.rohan.dragonGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class InventoryManager {
	
	enum ownerType {PLAYER, ENTITY, TRADE}
	private Cursor cursor;
	private Slot slot;
	private Player player;
	int playerStr;
	int playerWis;
	int playerIntel;
	int gridHeight;
	int gridWidth;
	int startingX;
	int startingY;
	SpriteBatch sb;
	private ownerType type;
	
	Boolean readyToDraw = false; 
	
	private Slot[][] grid;
	
	public InventoryManager(Entity inputEntity)
	{
		System.out.println("Setting up the Inventory Manager for an entity in the InventoryManager constructor");
		type = ownerType.ENTITY;
		gridHeight = 10;
		gridWidth = 8;
		startingX = Gdx.graphics.getWidth()/2;
		startingY = Gdx.graphics.getHeight()/2;
		
		sb = new SpriteBatch();
		cursor = inputEntity.p.getInventoryManager().getCursor();
		
		grid = setupGrid();
		readyToDraw = true;
	}
	
	public InventoryManager(int inputGH, int inputGW, int inputSX, int inputSY, Slot[][] originalGrid, ownerType inputType, Cursor inputCursor)
	{
		gridHeight = inputGH;
		gridWidth = inputGW;
		startingX = inputSX;
		startingY = inputSY;
		type = inputType;
		sb = new SpriteBatch();
		cursor = inputCursor;
		grid = copyGrid(originalGrid);
		readyToDraw = true;
		System.out.println("InventoryManager.Constructor for copyMe() has been created");
	}
	
	public InventoryManager(Player inputPlayer)
	{
		System.out.println("Setting up the Inventory Manager for the player in the InventoryManager constructor");
		player = inputPlayer;
		
		type = ownerType.PLAYER;
		
		playerStr = player.getStats("str");
		playerWis = player.getStats("wis");
		playerIntel = player.getStats("intel");
		
		//I'm certain there is a very simple solution to this problem
		//But I'm running low on coffee and the Red Cross won't consider that an official 'disaster'
		gridHeight = playerStr;  
		gridWidth = playerWis+playerIntel/2;
		
		startingX = Gdx.graphics.getWidth()/10;
		startingY = Gdx.graphics.getHeight()/2;
		
		sb = new SpriteBatch();
		cursor = new Cursor(this);
		
		grid = setupGrid();
		readyToDraw = true;
	}
	
	@SuppressWarnings("unused")
	public InventoryManager(TradeHandler inputTH)
	{
		System.out.println("Setting up the Inventory Manager for the Tradehandler in the InventoryManager constructor");
		type = ownerType.TRADE;
		TradeHandler th = inputTH;
		gridHeight = 6;
		gridWidth = 8;
		startingX = Gdx.graphics.getWidth()/3;
		startingY = Gdx.graphics.getHeight()/10;
		grid = setupGrid();
		sb = new SpriteBatch();
		cursor = inputTH.getMainCursor();
		readyToDraw = true;
	}
	
	public Boolean addItem(Item inputItem)
	{
		for(int row = 0; row < gridHeight; row++)
		{
			for(int col = 0; col < gridWidth; col++)
			{
				//If the scan finds an unused grid it will proceed to the next step
				if(grid[row][col].getCurrentType().equals("UNUSED"))
				{
					//If there's room for the item at the grid location this will return true and the item will be inserted
					//Otherwise it'll return false and the check will continue
					if(checkGridRoom(inputItem, row, col))
					{
						//Set the item for the chosen, approved slot
						//System.out.println("Setting the item in the slot to picked up item in InventoryManager.pickUpItem");
						grid[row][col].setItem(inputItem);
						//Tell that slot it is now a master
						//System.out.println("Setting slot to master status in InventoryManager.pickUpItem");
						grid[row][col].setType("MASTER");
						//Enslave the slots for this item
						//System.out.println("Enslaving slots in InventoryManager.pickUpItem");
						enslaveSlots(inputItem, row, col);
						//System.out.println("Setting item" + inputItem.getInputString() + " to slot [" + row  + ", " + col + "] in inventoryManager.addItem()");

						return true;
					}	
				}
			}
		}
		return false;
	}
	
	public void addItem(Slot inputSlot, Item inputItem)
	{
		grid[inputSlot.getRow()][inputSlot.getCol()].setItem(inputItem);
		grid[inputSlot.getRow()][inputSlot.getCol()].setType("MASTER");
		enslaveSlots(inputItem, inputSlot.getRow(),inputSlot.getCol());
	}
	
	public Boolean checkGridRoom(Item inputItem, int inputRow, int inputCol)
	{
		//Check grid size against item size
		//System.out.println("Item " + inputItem.getInputString() + " is " + inputItem.getGridWidth() + " tiles wide and " + inputItem.getGridHeight() + " tiles high.");
		//System.out.println("Grid is beginning at location [" + inputRow + ", " + inputCol + "]");
		int itemHeight = inputRow + inputItem.getGridHeight();
		int itemWidth = inputCol + inputItem.getGridWidth();
		int rowClear = 0;
		int colClear = 0;
		//First check the first vertical row
		for(int row = inputRow; row <= itemHeight && row < grid.length; row++)
		{
			rowClear += 1;
			
			//Reset the columns; none have been cleared for this row
			colClear = 0;
			//Against the columns in that row
			for(int col = inputCol; col <= itemWidth &&  col < grid[row].length; col++)
			{
				//If the grid is clear as it checks the length/width of the item in the rows/colums available
				if(grid[row][col].getCurrentType().equals("UNUSED"))
				{
					colClear += 1;
					//Broke up the below for debug purposes.
					//If the total check turns out that all dimensions needed are clear, then return true; the item can be added
					if(rowClear == inputItem.getGridHeight())
					{
						//System.out.println("Row was clear at " + rowClear + " rows tall clear, checking column");
						if(colClear == inputItem.getGridWidth())
						{
							//System.out.println("Column was clear at " + colClear + " columns wide clear, returning true");
							//If every number has been checked and returned as Unused, then we're all clear and proceed with the process
							return true;
						}

					}
				}
			}
			if(colClear == 0)
			{
				//If none of the blocks are clear on this row of columns, set rowClear back to 0 to show a break in storage
				rowClear = 0;
				//System.out.println("Checking next row, col did not return a valid destination for the item.");
			}
		}

		//There wasn't enough room for the item at the specified coordinates, so return false
		System.out.println("InventoryManager.checkGridRoom() didn't find enough room for the item");
		return false;
	}
	public Slot[][] copyGrid(Slot[][] inputGrid)
	{
		System.out.println("InventoryManager.copyGrid() is beginning");
		System.out.println("InventoryManager.copyGrid() inputGrid is " + inputGrid.length + " by " + inputGrid[0].length);
		//There won't be ragged arrays so assuming [0].length is the same as [15555].length is fine here
		Slot[][] tempGrid = setupGrid();
		System.out.println("InventoryManager.copyGrid() tempGrid is " + tempGrid.length + " by " + tempGrid[0].length);
		System.out.println("InventoryManager.copyGrid() has established tempGrid's[][]");
		
		
		System.out.println("InventoryManager.copyGrid() is entering for loop");		
		for(int row = 0; row < tempGrid.length; row++)
		{
			for(int col = 0; col < tempGrid[row].length; col++ )
			{
				//CHECK TO SEE IF THE SLOT IS SLAVE OR MASTER, not if there is an item there.
				//TODO: CHECK THIS FOR VERIFICATION
				if(inputGrid[row][col].getCurrentItem() != null)
				{
					System.out.println("InventoryManager.copyGrid() is trying to read the value at " + row + ", " + col);
					System.out.println("InventoryManager.copyGrid() inputGrid has an item named " + inputGrid[row][col].getCurrentItem().getInputString());
					Item tempItem = inputGrid[row][col].getCurrentItem().copyMe();
					System.out.println("InventoryManager.copyGrid() has an established tempItem with the name " + tempItem.getInputString());
					tempGrid[row][col].setItem(tempItem);
					System.out.println("InventoryManager.copyGrid() tempGrid now has an item named " + tempGrid[row][col].getCurrentItem().getInputString());
					//Need to tell the slot to become a master and tell this im that the new slots around it are slaves
					tempGrid[row][col].setType("MASTER");
					//Enslave the slots for this item
					System.out.println("Enslaving slots in InventoryManager.pickUpItem");
					enslaveSlots(tempGrid[row][col].getCurrentItem(), row, col);
					System.out.println("Adding copied item " + tempGrid[row][col].getCurrentItem().getInputString()+ " at location:[" + row + ", " + col + "]");
				}

			}
		}
		System.out.println("InventoryManager.copyGrid() is ending");
		return tempGrid;
	}
	public InventoryManager copyMe()
	{
		System.out.println("Entity type " + type.toString() + " has called for InventoryManager.CopyMe()");
		return new InventoryManager(gridHeight, gridWidth, startingX, startingY, grid, type, cursor);
	}
	public void draw()
	{
		sb.begin();
		
		//Draw every Slot
		for(int row = 0; row < gridHeight; row++)
		{
			for(int col = 0; col < gridWidth; col++)
			{
				//Draw slot at i,j
				
				grid[row][col].draw(row, col);
			}
		}
		sb.end();		

		switch(type)
		{
		case PLAYER:
			cursor.draw();
			break;
		default:
			break;
		}

		

	}
	
	public void enslaveSlots(Item inputItem, int inputRow, int inputCol)
	{
		int maxHeight = inputRow + inputItem.getGridHeight();
		int maxWidth = inputCol + inputItem.getGridWidth();
		for(int i = inputRow; i < maxHeight; i++)
		{
			for(int j = inputCol; j < maxWidth; j++)
			{
				//Set every free slot previously checked by checkGridRoom() to a slave status
				//TODO: Fix horrendous hack
				if(i == inputRow && j == inputCol)
				{
					//System.out.println("Doing nothing at this location");
				}
				else
				{
					grid[i][j].setType("SLAVE");
					//Debug info
					//System.out.println("The slot at coords " + i + ", " + j + " is now enslaved to slot [" + inputRow + ", " + inputCol + "].");					
				}
				//Tell every slot assigned as a slave that its new master slot is at grid inputX, inputY
				grid[i][j].setMaster(grid[inputRow][inputCol]);
				//Tell the master it has a new slave slot
				grid[inputRow][inputCol].addSlave(grid[i][j]);
			}
		}
	}
	
	public Cursor getCursor(){return cursor;}

	public Slot[][] getGrid(){return grid;}
	
	public Boolean getReady()
	{
		return readyToDraw;
	}
	
	public ownerType getType()
	{
		return type;
	}
	
	public void gridUpdate()
	{
		for(int row = 0; row < gridHeight; row++)
		{
			for(int col = 0; col < gridWidth; col++)
			{
				//Update every slot
				grid[row][col].update();
			}
		}
	}
	
	public void removeItem(Slot inputSlot)
	{
		//Setting the slot to an unused slot allows for handling all the dropping item, setting slot to unused, freeing its slaves, etc
		inputSlot.setType("UNUSED");
	}
	

	public void setGrid(Slot[][] inputGrid)
	{
		grid = inputGrid;
	}
	
	public Slot[][] setupGrid()
	{
		//Establish grid size/bounds
		grid = new Slot[gridHeight][gridWidth];
		//FILL UP DAT GRID SUCKA
		for(int row = 0; row < gridHeight; row++)
		{
			for(int col = 0; col < gridWidth; col++ )
			{
				slot = new Slot(this, row, col, startingX, startingY);
				grid[row][col] = slot;
				//System.out.println("Slot's owner is " + getType().toString());
				//System.out.println("Adding new slot at location:[" + row + ", " + col + "]");
			}
		}
		return grid;
	}
	
	public void update()
	{
		//No matter what the grid needs to update
		gridUpdate();
		switch(type)
		{
		case PLAYER:
			//The player cursor will always be present. There won't be a cursor without a player.
			cursor.update();
			break;
		case ENTITY:
			//Use trade handler to do stuff, get cursor
			
			break;
		case TRADE:
			//Use trade handler to do stuff, get cursor
			
			break;
		default:
			break;
		}
	}

}
