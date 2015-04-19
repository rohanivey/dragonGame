package com.rohan.dragonGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class InventoryManager {
	
	private Cursor cursor;
	private Slot slot;
	private Player player;
	int playerStr;
	int playerWis;
	int playerIntel;
	int gridHeight;
	int gridWidth;
	SpriteBatch sb;
	
	private Slot[][] grid; 
	
	public InventoryManager(Player inputPlayer)
	{
		player = inputPlayer;
		playerStr = player.getStats("str");
		playerWis = player.getStats("wis");
		playerIntel = player.getStats("intel");
		

		//WARNING: These are inverted
		//I'm certain there is a very simple solution to this problem
		//But I'm running low on coffee and the Red Cross won't consider that an official 'disaster'
		gridHeight = playerStr;  
		gridWidth = playerWis+playerIntel/2; 
		
		sb = new SpriteBatch();
		cursor = new Cursor(this);
		
		
		//Establish grid size/bounds
		grid = new Slot[gridHeight][gridWidth];
		//FILL UP DAT GRID SUCKA
		for(int row = 0; row < gridHeight; row++)
		{
			for(int col = 0; col < gridWidth; col++ )
			{
				slot = new Slot(this, row, col);
				grid[row][col] = slot;
				System.out.println("Adding new slot at location:[" + row + ", " + col + "]");
			}
		}
	}

	public void update()
	{
		cursor.update();
		for(int row = 0; row < gridHeight; row++)
		{
			for(int col = 0; col < gridWidth; col++)
			{
				//Update every slot
				grid[row][col].update();
			}
		}
	}
	
	public Cursor getCursor(){return cursor;}
	
	public Boolean pickUpItem(Item inputItem)
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
						grid[row][col].setItem(inputItem);
						//Tell that slot it is now a master
						grid[row][col].setType("MASTER");
						//Enslave the slots for this item
						enslaveSlots(inputItem, row, col);
						System.out.println("Setting item" + inputItem.getInputString() + " to slot [" + row  + ", " + col + "] in inventoryManager.pickUpItem()");
						return true;
					}	
				}
			}
		}
		return false;
	}
	
	public Boolean checkGridRoom(Item inputItem, int inputRow, int inputCol)
	{
		//Check grid size against item size
		System.out.println("Item " + inputItem.getInputString() + " is " + inputItem.getGridWidth() + " tiles wide and " + inputItem.getGridHeight() + " tiles high.");
		System.out.println("Grid is beginning at location [" + inputRow + ", " + inputCol + "]");
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
						System.out.println("Row was clear at " + rowClear + " rows tall clear, checking column");
						if(colClear == inputItem.getGridWidth())
						{
							System.out.println("Column was clear at " + colClear + " columns wide clear, returning true");
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
				System.out.println("Checking next row, col did not return a valid destination for the item.");
			}
		}

		//There wasn't enough room for the item at the specified coordinates, so return false
		System.out.println("InventoryManager.checkGridRoom() didn't find enough room for the item");
		return false;
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
				grid[i][j].setType("SLAVE");
				//Debug info
				System.out.println("The slot at coords " + i + ", " + j + " is now enslaved to slot [" + inputRow + ", " + inputCol + "].");
				//Tell every slot assigned as a slave that its new master slot is at grid inputX, inputY
				grid[i][j].setMaster(grid[inputRow][inputCol]);
			}
		}
		//Set the master slot back to the master position instead of being slaved to itself
		grid[inputRow][inputCol].setType("MASTER");
		grid[inputRow][inputCol].setMaster(null);
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
				grid[row][col].draw(row,col);
			}
		}
		
		//Draw dat mouse cursor
		cursor.draw();
		
		
		sb.end();
	}

}
