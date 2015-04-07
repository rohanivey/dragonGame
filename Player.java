package com.rohan.dragonGame;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
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
	AnimationState animationState = AnimationState.Down;
	State currentState = State.Moving;
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
	
	private int speed;
	
	private Entity activeEntity;
	
	private int chatSelection;
	
	private String[][] characterKnowledge;
	Boolean foundName;

	enum AnimationState 
	{
		Left, Right, Up, Down
	}
	
	enum State
	{
		Chatting, Moving, Trading, Fishing
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
		
		speed = 4;
		
		characterKnowledge = new String[1000][100];
		characterKnowledge[0][0] = "thyself";
		
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
				//System.out.println("Player has pressed E!");
				interactTimer = 1f;
				interactCircle.setPosition(interactCircleLocation);
				
				for(Entity e: ms.getCritters())
				{
					if(Intersector.overlaps(interactCircle, e.getCollision()))
							{
								for(int i = 0; i < characterKnowledge.length; i++)
								{
									if(characterKnowledge[i][0]== e.getName())
									{
										foundName = true;
										System.out.println("Found Name!");
										break;
									}
									else
									{
										foundName = false;
										//System.out.println("No name found!");
										//System.out.println(i);
									}
								}
								
								if(foundName){e.interact();}
								else
								{
									System.out.println("Firing up adding name system");
									//boolean charKnown = false;
									for(int i = 0; i < characterKnowledge.length; i++)
									{
										System.out.println("I don't know the name yet!");
										System.out.println("For loop at location " + i);
											if(characterKnowledge[i][0] == null )
											{
												characterKnowledge[i][0] = e.getName();
												//charKnown = true;
												e.getDialogueHandler().setPlayerKnowledgeCopy(characterKnowledge);
												System.out.println("I know the name now! Stored at location " + i);
												break;
											}
									}
									e.interact();
								}
								
							}
				}
				
			}
		}
	} 
	
	public void handleAnimation()
	{
		stateTime += ms.getGSM().getDeltaTime();
		switch(animationState)
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

		
		//If the player is currently in the moving state then check for movement input
		if(currentState == State.Moving)
		{
		checkMovement();
		interact();
		}
		
		//Else, check if player is chatting
		else if (currentState == State.Chatting)
		{
		checkChatting();	
		}
		
		//Else check if fishing
		else if(currentState == State.Fishing){}
		
		//Else check if trading
		else if(currentState == State.Trading){}
		
		if(Gdx.input.isKeyJustPressed(Keys.Q))
		{
			for(int i = 0; i < characterKnowledge.length; i++)
			{
				if(characterKnowledge[i][0] != null)
				{
					System.out.println(characterKnowledge[i][0]);					
				}
			}
		}
	}
	
	public void checkMovement()
	{
		
		Rectangle tempCollision;
		Boolean canMove = true;

		
		if(Gdx.input.isKeyPressed(Keys.W))
		{
			animationState = AnimationState.Up;
		
			for(Entity e: ms.getCritters())
			{
				tempCollision = new Rectangle(this.getCollision().x, this.getCollision().y + speed, this.getCollision().width, this.getCollision().height);
				if(Intersector.overlaps(tempCollision, e.getCollision()))
				{
					canMove = false;
					e.handleCollision(this);
				}
			}
			for(Rectangle r: ms.getColliders())
			{
				tempCollision = new Rectangle(this.getCollision().x, this.getCollision().y + speed, this.getCollision().width, this.getCollision().height);
				if(Intersector.overlaps(tempCollision, r))
				{
					canMove = false;
				}
			}
			if(canMove)
			{
				location.y += speed;
			}
		}
		else if(Gdx.input.isKeyPressed(Keys.S))
		{
			animationState= AnimationState.Down;
			for(Entity e: ms.getCritters())
			{
				tempCollision = new Rectangle(this.getCollision().x, this.getCollision().y - speed, this.getCollision().width, this.getCollision().height);
				if(Intersector.overlaps(tempCollision, e.getCollision()))
				{
					canMove = false;
					e.handleCollision(this);
				}
			}
			for(Rectangle r: ms.getColliders())
			{
				tempCollision = new Rectangle(this.getCollision().x, this.getCollision().y - speed, this.getCollision().width, this.getCollision().height);
				if(Intersector.overlaps(tempCollision, r))
				{
					canMove = false;
				}
			}
			if(canMove)
			{
				location.y -= speed;
			}
		}
		else if(Gdx.input.isKeyPressed(Keys.A))
		{
			animationState = AnimationState.Left;
			for(Entity e: ms.getCritters())
			{
				tempCollision = new Rectangle(this.getCollision().x - speed, this.getCollision().y, this.getCollision().width, this.getCollision().height);
				if(Intersector.overlaps(tempCollision, e.getCollision()))
				{
					canMove = false;
					e.handleCollision(this);
				}
			}
			for(Rectangle r: ms.getColliders())
			{
				tempCollision = new Rectangle(this.getCollision().x - speed, this.getCollision().y, this.getCollision().width, this.getCollision().height);
				if(Intersector.overlaps(tempCollision, r))
				{
					canMove = false;
				}
			}
			if(canMove)
			{
				location.x -= speed;
			}
		}
		else if(Gdx.input.isKeyPressed(Keys.D))
		{
			animationState = AnimationState.Right;
			for(Entity e: ms.getCritters())
			{
				tempCollision = new Rectangle(this.getCollision().x + speed, this.getCollision().y, this.getCollision().width, this.getCollision().height);
				if(Intersector.overlaps(tempCollision, e.getCollision()))
				{
					canMove = false;
					e.handleCollision(this);
				}
			}
			for(Rectangle r: ms.getColliders())
			{
				tempCollision = new Rectangle(this.getCollision().x + speed, this.getCollision().y, this.getCollision().width, this.getCollision().height);
				if(Intersector.overlaps(tempCollision, r))
				{
					canMove = false;
				}
			}
			if(canMove)
			{
				location.x += speed;
			}
		}
	}
	
	public void checkChatting()
	{
		if(!activeEntity.getDialogueHandler().getChatting())
		{
			currentState = State.Moving;
		}
		//System.out.println("Entering chat mode!");
		interactTimer -= ms.getGSM().getDeltaTime();
		if(interactTimer <= 0)
		{
			if(Gdx.input.isKeyPressed(Keys.W))
			{
				interactTimer = 0.25f;
				if(chatSelection == 0)
				{
					chatSelection = activeEntity.getDialogueHandler().getTalkingPointsSize() - 1;
				}
				else
				{
					chatSelection--;
				}
			}
			
			if(Gdx.input.isKeyPressed(Keys.S))
			{
				interactTimer = 0.25f;
				if(chatSelection < activeEntity.getDialogueHandler().getTalkingPointsSize() - 1)
				{
					chatSelection += 1;
				}
				else
				{
					chatSelection = 0;
				}
			}
			if(Gdx.input.isKeyPressed(Keys.E))
			{
				interactTimer = 0.25f;
				System.out.println("Player pressed E!");
				System.out.println("Player e: assigning new knowledge");
				characterKnowledge[checkNPCID()][findEmptyKnowledge(checkNPCID())] = activeEntity.getDialogueHandler().selectDialogue(chatSelection);
				System.out.println("Player e: Player should have learned " + activeEntity.getDialogueHandler().selectDialogue(chatSelection));
				System.out.println("Player e: Reassigning player knowledge copy");
				activeEntity.getDialogueHandler().setPlayerKnowledgeCopy(characterKnowledge);
				System.out.println("Player e: Attempting to create dialogue");
				activeEntity.getDialogueHandler().createDialogue();
			}
		}
		
	}
	
	public int checkNPCID()
	{
		for(int i = 0; i < characterKnowledge.length; i++)
		{
			if(characterKnowledge[i][0].equals(activeEntity.getName()))
			{
				return i;
			}
		}
		System.out.println("No npc yo");
		return 99999999;
	}
	
	public int findEmptyKnowledge(int inputNPCNumber)
	{
		for(int i = 0; i < characterKnowledge[inputNPCNumber].length; i++)
		{
			if(characterKnowledge[inputNPCNumber][i] == null)
			{
				return i;
			}
		}
		System.out.println("No available spaces for new info on this NPC yo");
		return 999999;
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

	@SuppressWarnings("static-access")
	public void setStateMoving()
	{
		currentState = currentState.Moving;
	}
	
	@SuppressWarnings("static-access")
	public void setStateChatting()
	{
		currentState = currentState.Chatting;
	}
	
	@SuppressWarnings("static-access")
	public void setStateTrading()
	{
		currentState = currentState.Trading;
	}
	
	@SuppressWarnings("static-access")
	public void setStateFishing()
	{
		currentState = currentState.Fishing;
	}
	
	public State getState()
	{
		return currentState;
	}
	
	public void setActiveEntity(Entity inputActiveEntity)
	{
		activeEntity = inputActiveEntity;
	}
	
	public Entity getActiveEntity()
	{
		return activeEntity;
	}
	
	public int getChatSelection()
	{
		return chatSelection;
	}
	
	

	

	
	
}
