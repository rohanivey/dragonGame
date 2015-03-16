package com.rohan.dragonGame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

public class GameStateManager {

	private ArrayList<GameState> gsList;
	private MenuState menu;
	private MenuState options;
	private MainState mainGame;
	
	private GameStateManager gsm;
	private int currentState;
	
	private float ticks;
	private float frames;
	private float totalTime;
	
	public GameStateManager()
	{
		gsm = this;
		
		frames = 60;
		ticks = 1/frames;
		
		gsList = new ArrayList<GameState>();
		menu = new MenuState(gsm);
		mainGame = new MainState(gsm);
		gsList.add(menu);
		gsList.add(options);
		gsList.add(mainGame);
		gsList.trimToSize();
		
		setState(0);
		
		System.out.println("GameStateManager initialized");
		System.out.println("Gamestate Size is " + gsList.size());
	}
	
	public void update()
	{
		totalTime += Gdx.graphics.getDeltaTime();
		if(totalTime >= ticks)
		{
			gsList.get(currentState).update();
			totalTime = 0;
			//System.out.println("Time Reset!");
		}
		
		//System.out.println("Total Time: " + totalTime + "\nDelta Time: " + Gdx.graphics.getDeltaTime() + "\n\n********\n\n");
	}
	
	public ArrayList<GameState> getList()
	{
		return gsList;
	}
	
	public GameStateManager returnGameStateManager()
	{
		return gsm;
	}
	
	public void setState(int inputState)
	{
		gsList.get(currentState).stopMusic();
		currentState = inputState;
		gsList.get(currentState).startMusic();
		/*Current States are
		 * Main = 0
		 * Options = 1
		 * Game = 2
		 * Inventory = 3
		 * Credits = 4
		 */
		
	}
	
	public int getCurrentState() {return currentState;}
	
	
}
