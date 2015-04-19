package com.rohan.dragonGame;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Item {

	private String id;
	private String description;
	private XmlReader reader;
	private Element root;
	private Element thisItem;
	private Vector2 location;
	private Texture img;
	private Rectangle collisionShape;
	private String whatAmI;
	private int value;
	private Boolean trading = false;
	
	
	private int gridHeight = 0;
	private int gridWidth = 0;
	
	
	public Item(String inputString)
	{
		whatAmI = inputString;
		itemSetup(inputString);
	}
	
	public Item(String inputString, int inputX, int inputY)
	{
		whatAmI = inputString;
		itemSetup(inputString);
		
		location = new Vector2(inputX, inputY);
		
		setCollision(img);
	}	
	
	
	public void itemSetup(String inputString)
	{
		reader = new XmlReader();
		try {
			root = reader.parse(Gdx.files.internal("ItemList.xml"));
			System.out.println("Got item list");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("GOOFED UP DAT ITEM LIST HONKA!");
		}
		thisItem = root.getChildByName(inputString);		
		id = thisItem.getAttribute("id");
		description = thisItem.getAttribute("description", "NO DESCRIPTION SET!");
		img = new Texture(Gdx.files.internal(thisItem.getAttribute("sprite")));
		value = Integer.parseInt(thisItem.getAttribute("initialValue"));
		gridHeight = Integer.parseInt(thisItem.getAttribute("gridHeight"));
		gridWidth = Integer.parseInt(thisItem.getAttribute("gridWidth"));
	}
	
	public void setCollision(Texture inputImg){collisionShape = new Rectangle(location.x, location.y, inputImg.getWidth(), inputImg.getHeight());}
	
	public void handleInteraction(Circle interactCircle)
	{
		//System.out.println("Handling interaction");
		if(Intersector.overlaps(interactCircle, collisionShape))
		{
			System.out.println(description);
		}
	}
	
	public Texture getTexture(){return img;}
	public Vector2 getLocation(){return location;}
	public Rectangle getCollisionShape(){return collisionShape;}
	public String getDescription(){return description;}
	public String getInputString(){return whatAmI;}
	public int getValue(){return value;}
	public void setTrading()
	{
		if(trading)
			trading = false;
		else
			trading = true;
	}
	public Boolean getTrading(){return trading;}
	
	public int getGridHeight(){return gridHeight;}
	public int getGridWidth(){return gridWidth;}
	
}
