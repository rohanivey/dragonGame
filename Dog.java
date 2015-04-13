package com.rohan.dragonGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Dog extends Entity{

	Texture img = new Texture("dog.png");

	
	Dog(int inputX, int inputY, Player inputPlayer, MainState inputMainState) {
		super(inputX, inputY, inputPlayer, "dog", inputMainState);
		setCollision(img);
		type = Type.Neutral;
		inventory.add(new Item("Dagger"));
		inventory.add(new Item("Shield"));
		inventory.add(new Item("Dagger"));
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

	@Override
	public void interact() {

		dh.setChatting(true);
		
		p.setActiveEntity(this);
		p.setStateChatting();
		dh.createDialogue();


		

		
	}

	
}
