package com.rohan.dragonGame;

public class MainState extends GameState {

	private MainState ms;

	private Boolean paused;
	private Boolean changeState = false;

	private String currentMap;
	private Level currentLevel;

	public MainState(String inputMap) {
		ms = this;
		paused = false;
		// Input a hash map for this to read the inputMap and switch/case
		// for orthographic level or platform level
		OrthoLevel ol = new OrthoLevel(ms, inputMap);
		currentLevel = ol;
		System.out.println("MainState initialized");
	}

	public MainState() {
		ms = this;
		paused = false;
		OrthoLevel ol = new OrthoLevel(ms);
		currentLevel = ol;
		System.out.println("MainState initialized");
	}

	@Override
	public void dispose() {

	}

	@Override
	public void draw() {

		currentLevel.draw();

	}

	public void pauseUpdate(Boolean inputBoolean) {
		paused = inputBoolean;
	}

	public Boolean stateChange() {
		return changeState;
	}

	@Override
	public void update() {
		if (!paused) {
			currentLevel.update();
		}

	}

	public String getCurrentMap() {
		return currentMap;
	}

	public void setCurrentMap(String currentMap) {
		this.currentMap = currentMap;
	}

}
