package com.rohan.dragonGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;

public class Player {

	private Vector2 location;
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
	
	
	enum State 
	{
		Left, Right, Up, Down
	}
	
	public Player(int inputX, int inputY, MainState inputMainState)
	{
		location = new Vector2(inputX, inputY);
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
	}

	public void update()
	{ 
		handleInput();
		handleAnimation();
		screenEdging();
	}
	
	public void handleAnimation()
	{
		stateTime += Gdx.graphics.getDeltaTime();
		switch(state)
		{
		case Left:
			//System.out.println("State is left");
			currentFrame = walkLeft.getKeyFrame(stateTime,true);
			break;
		case Right:
			//System.out.println("State is right");
			currentFrame = walkRight.getKeyFrame(stateTime,true);
			break;
		case Up:
			//System.out.println("State is up");
			currentFrame = walkUp.getKeyFrame(stateTime,true);
			break;
		case Down:
			//System.out.println("State is down");
			currentFrame = walkDown.getKeyFrame(stateTime,true);
			break;
		}
	}
	
	public void handleInput()
	{
		if(Gdx.input.isKeyPressed(Keys.W)){ location.y += 40 ; state = State.Up;}
		else if(Gdx.input.isKeyPressed(Keys.S)){ location.y -=40 ; state = State.Down;}
		else if(Gdx.input.isKeyPressed(Keys.D)){ location.x += 40 ; state = State.Right;}
		else if(Gdx.input.isKeyPressed(Keys.A)){ location.x -= 40 ; state = State.Left;}
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
		if(location.x < 4){location.x = 4; }
		if(location.x > mapWidth - currentFrame.getRegionWidth()){location.x = mapWidth - currentFrame.getRegionWidth();}
		if(location.y < 4){location.y = 4; }
		if(location.y > mapHeight - currentFrame.getRegionHeight()){location.y = mapHeight - currentFrame.getRegionHeight();}
	}
	
	public TextureRegion getTexture(){ return currentFrame; }
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
	
}
