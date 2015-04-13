package com.rohan.dragonGame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

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
				if(currentSelectionPL < playerInventoryCopy.size() - 1){currentSelectionPL++;}
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
				else{currentSelectionPL = playerInventoryCopy.size() - 1;}
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
	}
	
	public void concludeTrade()
	{
		setTrading(false);
	}
	
	public void setTrading(Boolean inputBoolean){trading = inputBoolean;}
	public Boolean getTrading(){return trading;}
	public BitmapFont getFont(){return font;}
	public ArrayList<Item> getPICopy(){return playerInventoryCopy;}
	public ArrayList<Item> getEICopy(){return entityInventoryCopy;}
	public int getPISelection(){return currentSelectionPL;}
	public int getEISelection(){return currentSelectionEL;}
	public String getCurrentList(){return list.toString();}
	
}
