package com.rohan.dragonGame;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TradeHandler{

	private Boolean trading = true;
	private ArrayList<Item> playerInventoryCopy;
	private ArrayList<Item> entityInventoryCopy;
	private Lists list = Lists.Player;
	private int currentSelectionPL = 0;
	private int currentSelectionEL = 0;
	private SpriteBatch sb;
	private BitmapFont font;
	private OrthographicCamera cam;
	private ShapeRenderer sr;
	private float keyCooldown;
	private Boolean goodTrade = false;
	ArrayList<Item> playerBarter;
	ArrayList<Item> entityBarter;
	
	
	private enum Lists 
	{
		Player, Entity 
	}
	
	TradeHandler(ArrayList<Item> inputPlayerInventory, ArrayList<Item> inputEntityInventory)
	{
		sb = new SpriteBatch();
		font = new BitmapFont();
		cam = new OrthographicCamera(900f ,600f);
		sr = new ShapeRenderer();
		playerInventoryCopy = new ArrayList<Item>();
		entityInventoryCopy = new ArrayList<Item>();
		playerBarter = new ArrayList<Item>();
		entityBarter = new ArrayList<Item>();
		keyCooldown = 0.25f;
		
		
		System.out.println("In TradeHandler constructor: ");
		for(Item i : inputPlayerInventory)
		{
			System.out.println("Trying to add item " + i.getInputString());
			Item iCopy = new Item(i.getInputString());
			playerInventoryCopy.add(iCopy);
			System.out.println("Added item " + i.getInputString());
		}
		for(Item i : inputEntityInventory)
		{
			Item iCopy = new Item(i.getInputString());
			entityInventoryCopy.add(iCopy);
		}
	}
	
	public void Update()
	{
		//System.out.println("Updating in TradeHandler");
		keyCooldown -= Gdx.graphics.getDeltaTime();
		//System.out.println(keyCooldown);
		checkInput();			
	}
	
	
	public void checkInput()
	{
		//System.out.println("Checking for player input in checkInput() in TradeHandler");
		if(Gdx.input.isKeyPressed(Keys.S) && keyCooldown <= 0)
		{
			keyCooldown = 0.25f;
			if(list == Lists.Player)
			{
				if(currentSelectionPL < playerInventoryCopy.size()){currentSelectionPL++;}
				else{currentSelectionPL = 0;}
				System.out.println("Incrementing selection in player list to " + currentSelectionPL);
			}
			else
			{
				if(currentSelectionEL <  entityInventoryCopy.size() - 1){currentSelectionEL++;}
				else{currentSelectionEL = 0;}
				System.out.println("Incrementing selection in entity list to " + currentSelectionEL);
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.W) && keyCooldown <= 0)
		{
			keyCooldown = 0.25f;
			if(list == Lists.Player)
			{
				if(currentSelectionPL > 0){currentSelectionPL--;}
				else{currentSelectionPL = playerInventoryCopy.size();}
				System.out.println("Decrementing selection in player list to " + currentSelectionPL);
			}
			else
			{
				if(currentSelectionEL > 0){currentSelectionEL--;}
				else{currentSelectionEL = entityInventoryCopy.size() - 1;}
				System.out.println("Decrementing selection in entity list to " + currentSelectionEL);
			}
			//Overriding the issues that can arise when currentSelection is smaller than 0
			if(currentSelectionPL < 0 ){currentSelectionPL = 0;System.out.println("Overriding selection back to 0");}
			if(currentSelectionEL < 0 ){currentSelectionEL = 0;System.out.println("Overriding selection back to 0");}
		}
		
		if(Gdx.input.isKeyPressed(Keys.A)  && keyCooldown <= 0 || Gdx.input.isKeyPressed(Keys.D) && keyCooldown <= 0)
		{
			keyCooldown = 0.25f;
			if(list == Lists.Player){list = Lists.Entity;}
			else {list = Lists.Player;}
			System.out.println("Swapping current list to: " + list.toString());
		}
		
		if(Gdx.input.isKeyPressed(Keys.E) && keyCooldown <= 0)
		{
			keyCooldown = 0.25f;
			switch(list)
			{
			case Entity:
				if(entityInventoryCopy.size() > currentSelectionEL)
				{
				entityInventoryCopy.get(currentSelectionEL).setTrading();
				}
				break;
			case Player:
				if(playerInventoryCopy.size() > currentSelectionPL)
				{
					playerInventoryCopy.get(currentSelectionPL).setTrading();
				}
				else if(playerInventoryCopy.size() == currentSelectionPL)
				{
					if(goodTrade())
					{
						exchangeGoods();
						goodTrade = true;
					}
					else
						System.out.println("BAD TRADE YO, TELL THE PLAYER THIS");
				}
				break;
			default:
				break;
			}
		}
	}
	
	public void concludeTrade()
	{
		setTrading(false);
	}
	
	public void setTrading(Boolean inputBoolean){trading = inputBoolean;}
	public Boolean getTrading(){return trading;}
	public Boolean getGoodTrade(){return goodTrade;}
	public BitmapFont getFont(){return font;}
	public ArrayList<Item> getPICopy(){return playerInventoryCopy;}
	public ArrayList<Item> getEICopy(){return entityInventoryCopy;}
	public ArrayList<Item> getPBCopy(){return playerBarter;}
	public int getPISelection(){return currentSelectionPL;}
	public int getEISelection(){return currentSelectionEL;}
	public String getCurrentList(){return list.toString();}
	
	public int checkPlayerPrices()
	{
		int total = 0;
		for(Item i : playerInventoryCopy)
			if(i.getTrading())
				total += i.getValue();
		return total;
	}
	
	public int checkEntityPrices()
	{
		int total = 0;
		for(Item i: entityInventoryCopy)
			if(i.getTrading())
				total += i.getValue();
		return total;
	}
	
	public Boolean goodTrade()
	{
		Boolean YoN = false;
		if(checkPlayerPrices() >= checkEntityPrices())
			YoN = true;
		return YoN;
	}
	
	public void exchangeGoods()
	{
		for(Iterator<Item> iterator = playerInventoryCopy.iterator(); iterator.hasNext();)
		{
			Item i = iterator.next();
			if(i.getTrading())
			{
				i.setTrading();
				entityInventoryCopy.add(i);
				iterator.remove();
			}
		}
		
		for(Iterator<Item> iterator = entityInventoryCopy.iterator(); iterator.hasNext();)
		{
			Item i = iterator.next();
			if(i.getTrading())
			{
				i.setTrading();
				playerInventoryCopy.add(i);
				iterator.remove();
			}
		}
	}
	
	
}
