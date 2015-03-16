package com.rohan.dragonGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public class Dog extends Entity{

	Texture img = new Texture("badlogic.jpg");
	
	Dog(int inputX, int inputY) {
		super(inputX, inputY);
		setCollision(img);
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
