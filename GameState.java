package com.rohan.dragonGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameState {
	
	protected GameState gs;
	protected SpriteBatch batch;
	protected Texture img;
	
	public GameState()
	{
		gs = this;
	}
	
	public void startMusic(){}
	public void stopMusic(){}
	public void update(){}
	public void draw(){}
	public void dispose(){}

}
