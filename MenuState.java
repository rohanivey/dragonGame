package com.rohan.dragonGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuState extends GameState {

	private SpriteBatch sb;
	private Texture bg;
	private BitmapFont font;
	private String[] menu = { "New Game", "Options", "Exit" };
	private int currentSelection;
	private float timer;
	private Boolean changeState = false;

	private enum State {
		Options, Play
	}

	private State newState;

	private Music menuMusic;

	public MenuState() {
		sb = new SpriteBatch();
		bg = new Texture("badlogic.jpg");
		font = new BitmapFont();
		currentSelection = 0;
		menuMusic = Gdx.audio.newMusic(Gdx.files
				.internal("Music/No More Magic.ogg"));
		menuMusic.setLooping(true);
		menuMusic.setVolume(0.3f);

	}

	@Override
	public void update() {
		timer += Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.ENTER)) {
			select();
		} else if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			System.out.println("Escape pressed");
			System.exit(0);
		}

		if (timer > 0.1) {
			if (Gdx.input.isKeyPressed(Keys.W)
					| Gdx.input.isKeyPressed(Keys.UP)) {
				System.out.println("Up/W pressed");
				timer = 0;
				if (currentSelection > 0)
					--currentSelection;
				else
					currentSelection = menu.length - 1;
			} else if (Gdx.input.isKeyPressed(Keys.S)
					| Gdx.input.isKeyPressed(Keys.DOWN)) {
				System.out.println("Down/S pressed");
				timer = 0;
				if (currentSelection < menu.length - 1)
					++currentSelection;
				else
					currentSelection = 0;
			}
		}

	}

	@Override
	public void startMusic() {
		menuMusic.play();
	}

	@Override
	public void stopMusic() {
		menuMusic.stop();
	}

	public void select() {
		changeState = true;
		switch (currentSelection) {
		// New Game
		case 0:
			// Swap state to MainState
			newState = newState.Play;
			break;
		// Options
		case 1:
			// Swap state to Options
			newState = newState.Options;
			break;
		// Exit game
		case 2:
			System.exit(0);
			break;
		case 3:

			break;
		default:
		}

	}

	public String getState() {
		String returnString = new String();
		switch (newState) {
		case Play:
			returnString = "Play";
			break;
		case Options:
			returnString = "Options";
			break;
		}
		return returnString;
	}

	@Override
	public void draw() {
		sb.begin();
		sb.setColor(256, 256, 256, 1);
		for (int i = 0; i < menu.length; i++) {
			// Change the color based on selection
			if (i == currentSelection) {
				font.setColor(0, 1, 0, 1);
			} else
				font.setColor(1, 1, 1, 1);

			// 1024x768 is default config state

			font.draw(sb, menu[i], 450, 400 - i * 30);
		}
		sb.end();
	}

	@Override
	public void dispose() {
		sb.dispose();
		bg.dispose();
		font.dispose();
		menuMusic.dispose();
	}

	public Boolean stateChange() {
		return changeState;
	}

}
