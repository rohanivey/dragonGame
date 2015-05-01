package com.rohan.dragonGame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class CharacterCreationState implements ApplicationListener {

	private String newState;
	private Boolean changeState = false;

	private Stage stage;
	private Skin skin;
	private Table mainTable;

	private enum Process {
		makeName, makeStats, makeFeats, makeSprite, makeOverview
	}

	private Process currentPage;

	private TextField firstNameTextField;
	private TextField secondNameTextField;
	private TextButton tb;

	private String tempFirst, tempSecond;
	private int tempStr, tempWis, tempInt, tempAgi, tempVit;

	// Debug
	ShapeRenderer sr;

	public CharacterCreationState() {
		create();
	}

	@Override
	public void create() {
		currentPage = Process.makeName;
		skin = new Skin(Gdx.files.internal("Character Creation/uiskin.json"));
		skin.getFont("default-font").setColor(Color.WHITE);
		sr = new ShapeRenderer();
		makeNameSetup();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void render() {
		stage.act(Gdx.graphics.getDeltaTime());

		// sb.setProjectionMatrix(cam.combined);
		// System.out.println("drawing");
		stage.draw();

		// sr.setProjectionMatrix(cam.combined);
		sr.setAutoShapeType(true);
		sr.setColor(Color.WHITE);
		sr.begin();
		// mainTable.drawDebug(sr);
		sr.end();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Boolean stateChange() {
		return changeState;
	}

	public String getState() {
		return newState;
	}

	public void makeStatsSetup() {

		stage.dispose();

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		mainTable = new Table(skin);
		stage.addActor(mainTable);
		mainTable.setFillParent(true);
		TextButton minusStr = new TextButton("-", skin);
		TextButton plusStr = new TextButton("+", skin);
		TextButton minusWis = new TextButton("-", skin);
		TextButton plusWis = new TextButton("+", skin);
		TextButton minusInt = new TextButton("-", skin);
		TextButton plusInt = new TextButton("+", skin);
		TextButton minusAgi = new TextButton("-", skin);
		TextButton plusAgi = new TextButton("+", skin);
		TextButton minusVit = new TextButton("-", skin);
		TextButton plusVit = new TextButton("+", skin);

		Label str = new Label(String.valueOf(tempStr), skin, "default");
		Label wis = new Label(String.valueOf(tempWis), skin, "default");
		Label intel = new Label(String.valueOf(tempInt), skin, "default");
		Label agi = new Label(String.valueOf(tempAgi), skin, "default");
		Label vit = new Label(String.valueOf(tempVit), skin, "default");

		Label strText = new Label("Strength: ", skin, "default");
		Label wisText = new Label("Wisdom: ", skin, "default");
		Label intelText = new Label("Intelligence: ", skin, "default");
		Label agiText = new Label("Agility: ", skin, "default");
		Label vitText = new Label("Vitality: ", skin, "default");

		mainTable.add(strText).uniform();
		mainTable.add(minusStr).uniform();
		mainTable.add(str);
		mainTable.add(plusStr).uniform();
		mainTable.row();

		mainTable.add(wisText);
		mainTable.add(minusWis);
		mainTable.add(wis);
		mainTable.add(plusWis);
		mainTable.row();

		mainTable.add(intelText);
		mainTable.add(minusInt);
		mainTable.add(intel);
		mainTable.add(plusInt);
		mainTable.row();

		mainTable.add(agiText);
		mainTable.add(minusAgi);
		mainTable.add(agi);
		mainTable.add(plusAgi);
		mainTable.row();

		mainTable.add(vitText);
		mainTable.add(minusVit);
		mainTable.add(vit);
		mainTable.add(plusVit);
		mainTable.row();

		currentPage = Process.makeStats;

	}

	public void makeNameSetup() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		mainTable = new Table(skin);
		// mainTable.setWidth(Gdx.graphics.getWidth());
		// mainTable.setHeight(Gdx.graphics.getHeight());
		mainTable.setFillParent(true);
		stage.addActor(mainTable);

		// sr = new ShapeRenderer();
		// mainTable.setDebug(true);

		firstNameTextField = new TextField("First Name", skin, "default");
		firstNameTextField.setColor(Color.WHITE);
		firstNameTextField.setWidth(32f);
		firstNameTextField.setHeight(20f);
		firstNameTextField.setY(Gdx.graphics.getHeight());
		mainTable.add(firstNameTextField).uniform().center();
		mainTable.row();
		secondNameTextField = new TextField("Surname", skin);
		secondNameTextField.setWidth(32f);
		secondNameTextField.setHeight(20f);
		mainTable.add(secondNameTextField).uniform().center();
		mainTable.padTop(Gdx.graphics.getHeight() / 2 - 20);

		// nameTable = new Table(skin);
		// nameTable.setHeight(200);
		// nameTable.setWidth(200);
		// nameTable.add(secondNameTextField).expandX();
		// nameTable.center();
		mainTable.row();
		tb = new TextButton("Next", skin);
		mainTable.add(tb).expand().bottom().right();

		defineNextButton(tb);
		// mainTable.addActor(nameTable);
		// mainTable.center();

		System.out.println(stage.getWidth());
		System.out.println(stage.getHeight());
		System.out.println(mainTable.getWidth());
		System.out.println(mainTable.getHeight());
		stage.setDebugAll(true);
	}

	public Boolean doubleCheck() {
		final Table tempTable = new Table(skin);
		tempTable.setFillParent(true);
		mainTable.setVisible(false);
		switch (currentPage) {
		case makeName:

			System.out.println(tb.isDisabled());

			Label tempLabel = new Label("Are you sure you are " + tempFirst
					+ " " + tempSecond + "?", skin);
			TextButton yesButton = new TextButton("Yes", skin);
			TextButton noButton = new TextButton("No", skin);

			tempTable.add(tempLabel);
			tempTable.row();
			tempTable.add(yesButton).uniform();
			tempTable.add(noButton).uniform();
			stage.addActor(tempTable);
			tempTable.toFront();

			yesButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int yesButton) {
					makeStatsSetup();
					return true;
				}
			});
			noButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int noButton) {
					mainTable.setVisible(true);
					tempTable.remove();
					return true;
				}
			});
			break;
		case makeStats:
			break;
		case makeFeats:
			break;
		case makeSprite:
			break;
		case makeOverview:
			break;
		default:
			break;
		}
		return false;
	}

	public void defineNextButton(Button inputButton) {
		Button button = inputButton;

		switch (currentPage) {
		case makeFeats:
			break;
		case makeName:
			button.addListener(new InputListener() {
				// Triggered on pressing down
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("Button Hit");
					tempFirst = firstNameTextField.getText();
					tempSecond = secondNameTextField.getText();
					doubleCheck();
					return true;
				}

				// Triggered on release
				public void touchUp() {

				}
			});
			break;
		case makeOverview:
			break;
		case makeSprite:
			break;
		case makeStats:
			break;
		default:
			break;

		}
	}

}
