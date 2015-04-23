package com.rohan.dragonGame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

//Make a Game State Manager
//Make a super Game state
//MAke individual Game States to inherit

public class DragonGame extends ApplicationAdapter {
	// SpriteBatch batch;
	// Texture img;

	private GameStateManager gsm;

	@Override
	public void create() {
		/*
		 * Removed batch and img since states will be called for drawing
		 */
		// batch = new SpriteBatch();
		// img = new Texture("badlogic.jpg");

		gsm = new GameStateManager();
	}

	@Override
	public void render() {
		gsm.update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.stateDraw();
		// gsm.getList().get(gsm.getCurrentState()).draw();
		// batch.begin();
		// batch.draw(img, 0, 0);
		// batch.end();
	}
}
