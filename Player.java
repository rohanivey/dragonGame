package com.rohan.dragonGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player{

	private Vector2 location;
	private Vector2 previousLocation;
	private Vector2 tempLocation;
	private Texture img;
	private MainState ms;
	private MapProperties mapProperties;
	private int mapWidth;
	private int mapHeight;
	
	private Animation walkLeft;
	private Animation walkRight;
	private Animation walkUp;
	private Animation walkDown;
	State state = State.Down;
	float stateTime;
	TextureRegion[] animationFrames;
	TextureRegion currentFrame;
	//Horizontal
	private static final int FRAME_COLS = 9;
	//Vertical
	private static final int FRAME_ROWS = 4;
	
	private Rectangle boundingRectangle;
	private Circle interactCircle;
	private Vector2 interactCircleLocation;
	private float interactTimer;

	enum State 
	{
		Left, Right, Up, Down
	}
	
	public Player(int inputX, int inputY, MainState inputMainState)
	{
		
		location = new Vector2(inputX, inputY);
		previousLocation = location;
		ms = inputMainState;
		img = new Texture("animations/player.png");
		TextureRegion[][] tempFrames = TextureRegion.split(img, img.getWidth()/FRAME_COLS, img.getHeight()/FRAME_ROWS);
		//animationFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		//int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++)
		{
			animationFrames = new TextureRegion[FRAME_COLS];
			int index = 0;
			for(int  j = 0; j < FRAME_COLS; j++)
			{
				animationFrames[index++] = tempFrames[i][j];
			}
			switch(i)
			{
			case 0:
				walkUp = new Animation(1f/9f, animationFrames);
				break;
			case 1:
				walkLeft = new Animation(1f/9f, animationFrames);
				break;
			case 2:
				walkDown = new Animation(1f/9f, animationFrames);
				break;
			case 3:
				walkRight = new Animation(1f/9f, animationFrames);
				break;				
			
			}
		}
		/*
		walkLeft = new Animation(1f/9f, animationFrames[1]);
		walkRight = new Animation(1f/9f, animationFrames[3]);
		walkUp = new Animation(1f/9f, animationFrames[0]);
		walkDown = new Animation(1f/9f, animationFrames[2]);
		*/
		stateTime = 0f;
		loadMap();
		currentFrame = walkDown.getKeyFrame(stateTime, true);
		
		
		boundingRectangle = new Rectangle();
		interactCircleLocation = new Vector2(location);
		interactCircle = new Circle(interactCircleLocation.x, interactCircleLocation.y, 1);
		interactTimer = 0f;
		
	}

	public void update()
	{ 
		handleInput();
		handleCollision();
		handleAnimation();
		screenEdging();
	}
	
	public void handleCollision()
	{
		previousLocation = tempLocation;
		tempLocation = location;
		boundingRectangle.set(location.x - currentFrame.getRegionWidth()/4, location.y, currentFrame.getRegionWidth()/2, currentFrame.getRegionHeight());
	}
	
	public void interact()
	{
		interactCircle.setPosition(location);
		interactTimer -= ms.getGSM().getDeltaTime();
		
		if(interactTimer <= 0)
		{
			if(Gdx.input.isKeyPressed(Keys.E))
			{
				interactTimer = 1f;
				interactCircle.setPosition(interactCircleLocation);
				System.out.println("Circle created at " + interactCircle.x + "," + interactCircle.y);
			}
		}
	} 
	
	public void handleAnimation()
	{
		stateTime += ms.getGSM().getDeltaTime();
		switch(state)
		{
		case Left:
			//System.out.println("State is left");
			currentFrame = walkLeft.getKeyFrame(stateTime,true);
			interactCircleLocation.set(location.x - currentFrame.getRegionWidth()/3, location.y + currentFrame.getRegionHeight()/4);
			break;
		case Right:
			//System.out.println("State is right");
			currentFrame = walkRight.getKeyFrame(stateTime,true);
			interactCircleLocation.set(location.x + currentFrame.getRegionWidth()/3, location.y + currentFrame.getRegionHeight()/4);
			break;
		case Up:
			//System.out.println("State is up");
			currentFrame = walkUp.getKeyFrame(stateTime,true);
			interactCircleLocation.set(location.x, location.y + currentFrame.getRegionHeight()/2);
			break;
		case Down:
			//System.out.println("State is down");
			currentFrame = walkDown.getKeyFrame(stateTime,true);
			interactCircleLocation.set(location.x, location.y - currentFrame.getRegionHeight()/2);
			break;
		}
	}
	
	public void handleInput()
	{
		interact();
		if(Gdx.input.isKeyPressed(Keys.W)){ location.y += 40 ; state = State.Up;}
		else if(Gdx.input.isKeyPressed(Keys.S)){ location.y -=4 ; state = State.Down;}
		else if(Gdx.input.isKeyPressed(Keys.D)){ location.x += 40 ; state = State.Right;}
		else if(Gdx.input.isKeyPressed(Keys.A)){ location.x -= 4 ; state = State.Left;}
	}
	
	public void loadMap()
	{
		mapProperties = ms.getCurrentMapProperties();
		int mapGridWidth = mapProperties.get("width", Integer.class);
		int mapGridHeight = mapProperties.get("height", Integer.class);
		int tilePixelWidth = mapProperties.get("tilewidth", Integer.class);
		int tilePixelHeight = mapProperties.get("tileheight", Integer.class);
		
		mapWidth = mapGridWidth * tilePixelWidth;
		mapHeight = mapGridHeight * tilePixelHeight;
	}
	
	public void screenEdging()
	{
		if(location.x < currentFrame.getRegionWidth()/4){location.x = currentFrame.getRegionWidth()/4; }
		if(location.x > mapWidth - currentFrame.getRegionWidth()/4){location.x = mapWidth - currentFrame.getRegionWidth()/4;}
		if(location.y < 0){location.y = 0;}
		if(location.y > mapHeight - currentFrame.getRegionHeight()){location.y = mapHeight - currentFrame.getRegionHeight();}
	}
	
	public Texture getTexture(){return null;}
	public TextureRegion getTextureRegion(){ return currentFrame; }
	public TextureRegion getFrame()
	{
		return currentFrame;
	}
	
	public float getX(){ return location.x; }
	public float getY(){ return location.y; }
	
	public void dispose()
	{
		img.dispose();
	}
	
	public Rectangle getCollision(){return boundingRectangle;}
	public Circle getInteraction(){return interactCircle;}
	
	public void fullStop()
	{
		location = previousLocation;
		System.out.println("FULL STOP!");
	}

	
	
	
}
