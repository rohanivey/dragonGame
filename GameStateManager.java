package com.rohan.dragonGame;

import com.badlogic.gdx.Gdx;

public class GameStateManager {

	private MenuState menu;
	private MenuState options;
	private MainState mainGame;

	private float ticks;
	private float frames;
	private float totalTime;

	private float dt;

	private State currentState = State.MainMenu;

	private enum State {
		MainMenu, OptionsMenu, Play, Inventory
	}

	public GameStateManager() {
		// gsm = this;

		frames = 60;
		ticks = 1 / frames;

		menu = new MenuState();
		mainGame = new MainState();

		System.out.println("GameStateManager initialized");
	}

	public void update() {
		dt = Gdx.graphics.getDeltaTime();
		totalTime += dt;
		if (totalTime >= ticks) {
			// gsList.get(currentState).update();
			totalTime = 0;

			switch (currentState) {
			case MainMenu:
				if (menu.stateChange()) {
					String tempString = menu.getState();
					changeState(tempString);
				} else {
					menu.update();
				}
				break;
			// case OptionsMenu:
			// menu.update();
			// break;
			case Play:
				if (mainGame.stateChange()) {
					String tempString = menu.getState();
					changeState(tempString);
				} else {
					mainGame.update();
				}
				break;
			// case Inventory:
			// menu.update();
			// break;
			default:
				break;

			}

		}

		// System.out.println("Total Time: " + totalTime + "\nDelta Time: " +
		// Gdx.graphics.getDeltaTime() + "\n\n********\n\n");
	}

	public float getDeltaTime() {
		return dt;
	}

	public void stateDraw() {
		switch (currentState) {
		case MainMenu:
			menu.draw();
			break;
		case Play:
			mainGame.draw();
			break;
		default:
			break;
		}
	}

	public void changeState(String inputString) {
		switch (inputString) {
		case "Main":
			currentState = currentState.MainMenu;
			break;
		case "Play":
			currentState = currentState.Play;
			break;
		case "Options":
			currentState = currentState.OptionsMenu;
			break;
		}
	}

}
