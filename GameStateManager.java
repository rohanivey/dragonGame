package com.rohan.dragonGame;

import com.badlogic.gdx.Gdx;

public class GameStateManager {

	private MenuState menu;
	private MenuState options;
	private MainState mainGame;
	private CharacterCreationState characterCreation;

	private float ticks;
	private float frames;
	private float totalTime;

	private float dt;

	private State currentState = State.MainMenu;

	private enum State {
		MainMenu, OptionsMenu, Play, CharacterCreation
	}

	public GameStateManager() {
		// gsm = this;

		frames = 60;
		ticks = 1 / frames;

		menu = new MenuState();
		mainGame = new MainState();
		characterCreation = new CharacterCreationState();

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
			case OptionsMenu:
				options.update();
				break;
			case Play:
				if (mainGame.stateChange()) {
					String tempString = menu.getState();
					changeState(tempString);
				} else {
					mainGame.update();
				}
				break;

			case CharacterCreation:
				if (characterCreation.stateChange()) {
					System.out
							.println("GameStateManager sees the change in characterCreation");
					String tempString = characterCreation.getState();
					changeState(tempString);
				} else {
					// Due to buffering/clearing, don't update here since render
					// also includes draw state for Scene2d
					// characterCreation.render();
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
		case CharacterCreation:
			characterCreation.render();
			break;
		default:
			break;
		}
	}

	public void changeState(String inputString) {
		switch (inputString) {
		case "Main":
			currentState = State.MainMenu;
			break;
		case "Play":
			currentState = State.Play;
			System.out.println("Setting current state to play");
			break;
		case "Options":
			currentState = State.OptionsMenu;
			break;
		case "Character Creation":
			// currentState = State.CharacterCreation;
			currentState = State.CharacterCreation;
			break;

		}
	}

}
