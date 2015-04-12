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
		
		
		for(Item i : inputPlayerInventory)
		{
			Item iCopy = new Item(i.getInputString());
			playerInventoryCopy.add(iCopy);
		}
		for(Item i : inputEntityInventory)
		{
			Item iCopy = new Item(i.getInputString());
			entityInventoryCopy.add(iCopy);
		}
	}
	
	public void Update()
	{
		checkInput();
	}
	
	
	public void checkInput()
	{
		if(Gdx.input.isButtonPressed(Keys.W))
		{
			if(list == Lists.Player)
			{
				if(currentSelectionPL < playerInventoryCopy.size()){currentSelectionPL++;}
				else{currentSelectionPL = 0;}
				System.out.println("Incrementing selection in player list");
			}
			else
			{
				if(currentSelectionEL <  entityInventoryCopy.size()){currentSelectionEL++;}
				else{currentSelectionEL = 0;}
				System.out.println("Incrementing selection in entity list");
			}
		}
		
		if(Gdx.input.isButtonPressed(Keys.S))
		{
			if(list == Lists.Player)
			{
				if(currentSelectionPL > 0){currentSelectionPL--;}
				else{currentSelectionPL = playerInventoryCopy.size() - 1;}
				System.out.println("Decrementing selection in player list");
			}
			else
			{
				if(currentSelectionEL > 0){currentSelectionEL--;}
				else{currentSelectionEL = entityInventoryCopy.size() - 1;}			}
				System.out.println("Decrementing selection in entity list");
		}
		if(Gdx.input.isButtonPressed(Keys.A) || Gdx.input.isButtonPressed(Keys.D))
		{
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
	
	public void Draw()
	{
		Rectangle vertRect = new Rectangle(0,cam.viewportHeight/4,cam.viewportWidth, cam.viewportHeight - cam.viewportHeight/4);
		Rectangle horRect = new Rectangle(0,0, cam.viewportWidth, cam.viewportHeight * 0.25f);
		sr.setProjectionMatrix(cam.combined);
		sr.begin(ShapeType.Filled);
		sr.rect(vertRect.x, vertRect.y, vertRect.width, vertRect.height);
		sr.rect(horRect.x, horRect.y, horRect.width, horRect.height);
		sr.end();
		sb.setProjectionMatrix(cam.combined);

		//Draw left pane
		//Draw center pane
		//Draw right pane
		//SCREW ALL THE ABOVE, DRAW ONE PANE WITH TWO LINES :P
		//Draw bottom pane, stretches across entire screen
	}
	
}
