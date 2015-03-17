package com.rohan.dragonGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Dog extends Entity{

	Texture img = new Texture("dog.png");

	
	Dog(int inputX, int inputY, Player inputPlayer) {
		super(inputX, inputY, inputPlayer);
		setCollision(img);
		type = Type.Neutral;
	}
	
	public Texture Draw(){return img;}

	@Override
	public Texture getTexture() {
		return img;
	}
	


	@Override
	public TextureRegion getTextureRegion() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
