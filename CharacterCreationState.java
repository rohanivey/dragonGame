package com.rohan.dragonGame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class CharacterCreationState implements ApplicationListener {

	private enum Process {
		makeName, makeStats, makeFeats, makeSprite, makeOverview
	}

	private String newState;
	private Boolean changeState = false;

	private Player tempPlayer;
	private Stage stage;
	private Skin skin;

	private Table mainTable;
	private ScrollPane scroll;

	private Process currentPage;

	private TextField firstNameTextField;
	private TextField secondNameTextField;
	private TextButton tb;

	private String tempFirst, tempSecond;

	private int tempStr, tempWis, tempInt, tempAgi, tempVit, pointsToSpend;
	TextButton minusStr;
	TextButton plusStr;
	TextButton minusWis;
	TextButton plusWis;
	TextButton minusInt;
	TextButton plusInt;
	TextButton minusAgi;
	TextButton plusAgi;
	TextButton minusVit;
	TextButton plusVit;

	Label pTS;
	Label str;
	Label wis;
	Label intel;
	Label agi;
	Label vit;

	Label pointsText;
	Label strText;
	Label wisText;
	Label intelText;
	Label agiText;
	Label vitText;

	FeatManager tempFeatManager;
	Image featImage;
	Label featDescription;
	Label featRequirement;

	// Debug
	ShapeRenderer sr;

	public CharacterCreationState() {
		create();
	}

	public void assignStatArrowButtons() {
		plusStr.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int plusStr) {
				if (pointsToSpend > 0) {
					tempStr += 1;
					System.out.println(tempStr);
					pointsToSpend -= 1;
					str.setText(String.valueOf(tempStr));
					pTS.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		minusStr.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int minusStr) {
				if (tempStr > 0) {
					tempStr -= 1;
					pointsToSpend += 1;
					str.setText(String.valueOf(tempStr));
					pTS.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});
		plusWis.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int plusWis) {
				if (pointsToSpend > 0) {
					tempWis += 1;
					System.out.println(tempWis);
					pointsToSpend -= 1;
					wis.setText(String.valueOf(tempWis));
					pTS.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		minusWis.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int minusWis) {
				if (tempWis > 0) {
					tempWis -= 1;
					pointsToSpend += 1;
					wis.setText(String.valueOf(tempWis));
					pTS.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});
		plusInt.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int plusInt) {
				if (pointsToSpend > 0) {
					tempInt += 1;
					System.out.println(tempInt);
					pointsToSpend -= 1;
					intel.setText(String.valueOf(tempInt));
					pTS.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		minusInt.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int minusInt) {
				if (tempInt > 0) {
					tempInt -= 1;
					pointsToSpend += 1;
					intel.setText(String.valueOf(tempInt));
					pTS.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		plusAgi.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int plusAgi) {
				if (pointsToSpend > 0) {
					tempAgi += 1;
					System.out.println(tempAgi);
					pointsToSpend -= 1;
					agi.setText(String.valueOf(tempAgi));
					pTS.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		minusAgi.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int minusAgi) {
				if (tempAgi > 0) {
					tempAgi -= 1;
					pointsToSpend += 1;
					agi.setText(String.valueOf(tempAgi));
					pTS.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		plusVit.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int plusVit) {
				if (pointsToSpend > 0) {
					tempVit += 1;
					System.out.println(tempVit);
					pointsToSpend -= 1;
					vit.setText(String.valueOf(tempVit));
					pTS.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});

		minusVit.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int minusVit) {
				if (tempVit > 0) {
					tempVit -= 1;
					pointsToSpend += 1;
					vit.setText(String.valueOf(tempVit));
					pTS.setText(String.valueOf(pointsToSpend));
				}
				// else play buzz or something
				return true;
			}
		});
	}

	@Override
	public void create() {
		currentPage = Process.makeName;
		tempPlayer = new Player();
		pointsToSpend = 6;
		skin = new Skin(Gdx.files.internal("Character Creation/uiskin.json"));
		skin.getFont("default-font").setColor(Color.WHITE);
		sr = new ShapeRenderer();
		makeNameSetup();
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Boolean doubleCheck() {
		final Table tempTable = new Table(skin);
		tempTable.setFillParent(true);
		mainTable.setVisible(false);
		Label tempLabel;
		TextButton yesButton;
		TextButton noButton;
		switch (currentPage) {
		case makeName:
			tempLabel = new Label("Are you sure you are " + tempFirst + " "
					+ tempSecond + "?", skin);
			yesButton = new TextButton("Yes", skin);
			noButton = new TextButton("No", skin);

			tempTable.add(tempLabel);
			tempTable.row();
			tempTable.add(yesButton);
			tempTable.add(noButton).uniform();
			stage.addActor(tempTable);
			tempTable.toFront();

			yesButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int yesButton) {
					tempPlayer.setFirstName(tempFirst);
					tempPlayer.setLastName(tempSecond);
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
			tempLabel = new Label("Are you happy with what you can do?", skin,
					"default");
			yesButton = new TextButton("Yes", skin);
			noButton = new TextButton("No", skin);

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
					tempPlayer.setStats("Strength", tempStr);
					tempPlayer.setStats("Wisdom", tempWis);
					tempPlayer.setStats("Intelligence", tempInt);
					tempPlayer.setStats("Agility", tempAgi);
					tempPlayer.setStats("Vitality", tempVit);
					makeFeatsSetup();
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

	public String getState() {
		return newState;
	}

	public void makeFeatsSetup() {
		stage = new Stage();
		tempFeatManager = new FeatManager(tempPlayer);
		Gdx.input.setInputProcessor(stage);
		mainTable = new Table(skin);
		stage.addActor(mainTable);

		Table featTable = new Table(skin);
		Texture tempImg = new Texture(tempFeatManager.getFeatList().get(0)
				.getImg());
		featImage = new Image(tempImg);

		featRequirement = new Label("", skin, "default");
		featDescription = new Label("", skin, "default");
		for (final Feat f : tempFeatManager.getFeatList()) {
			Button tempButton = new TextButton(f.getName(), skin);
			tempButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int tempButton) {
					Image newImage = new Image(new Texture(f.getImg()));
					featImage = newImage;
					featDescription.setText(f.getDescription());

					return true;
				}
			});
			featTable.add(tempButton);
		}

		scroll = new ScrollPane(featTable);
		scroll.setScrollBarPositions(false, true);
		// scroll.setSize(featTable.getWidth(), featTable.getHeight());

		Label titleLabel = new Label("Choose a Feat", skin, "default");

		/*
		 * titleLabel.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
		 * / 10f); featTable.setSize(Gdx.graphics.getWidth() / 2,
		 * Gdx.graphics.getHeight() * 0.40f);
		 * featImage.setSize(Gdx.graphics.getWidth() / 2,
		 * Gdx.graphics.getHeight() * 0.40f);
		 * featRequirement.setSize(Gdx.graphics.getWidth(),
		 * Gdx.graphics.getHeight() / 10f);
		 * featDescription.setSize(Gdx.graphics.getWidth(),
		 * Gdx.graphics.getHeight() / 5f);
		 */

		/*
		 * mainTable.add(titleLabel) .size(Gdx.graphics.getWidth(),
		 * Gdx.graphics.getHeight() / 10f) .row();
		 * mainTable.add(scroll).size(Gdx.graphics.getWidth() / 2,
		 * Gdx.graphics.getHeight() * 0.40f); mainTable .add(featImage)
		 * .size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() *
		 * 0.40f).row(); mainTable.add(featRequirement)
		 * .size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 10f)
		 * .row(); mainTable.add(featDescription).size(Gdx.graphics.getWidth(),
		 * Gdx.graphics.getHeight() / 5f);
		 */

		mainTable.setFillParent(true);
		mainTable.add(titleLabel).center().colspan(2).row();
		mainTable.add(scroll).center().expandX().fillX()
				.width(Gdx.graphics.getWidth() / 2);
		mainTable.add(featImage).expandX().fillX()
				.width(Gdx.graphics.getWidth() / 2)
				.height(Gdx.graphics.getHeight() * 0.40f).row();
		mainTable.add(featRequirement).width(Gdx.graphics.getWidth())
				.height(Gdx.graphics.getHeight() / 10).expandX().colspan(2)
				.row();
		mainTable.add(featDescription).expand().fill().colspan(2).row();

		stage.setDebugAll(true);

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

	public void makeStatsSetup() {

		stage.dispose();

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		mainTable = new Table(skin);
		stage.addActor(mainTable);
		mainTable.setFillParent(true);
		minusStr = new TextButton("-", skin);
		plusStr = new TextButton("+", skin);
		minusWis = new TextButton("-", skin);
		plusWis = new TextButton("+", skin);
		minusInt = new TextButton("-", skin);
		plusInt = new TextButton("+", skin);
		minusAgi = new TextButton("-", skin);
		plusAgi = new TextButton("+", skin);
		minusVit = new TextButton("-", skin);
		plusVit = new TextButton("+", skin);

		pTS = new Label(String.valueOf(pointsToSpend), skin, "default");
		str = new Label(String.valueOf(tempStr), skin, "default");
		wis = new Label(String.valueOf(tempWis), skin, "default");
		intel = new Label(String.valueOf(tempInt), skin, "default");
		agi = new Label(String.valueOf(tempAgi), skin, "default");
		vit = new Label(String.valueOf(tempVit), skin, "default");

		pointsText = new Label("Points to spend: ", skin, "default");
		strText = new Label("Strength: ", skin, "default");
		wisText = new Label("Wisdom: ", skin, "default");
		intelText = new Label("Intelligence: ", skin, "default");
		agiText = new Label("Agility: ", skin, "default");
		vitText = new Label("Vitality: ", skin, "default");

		mainTable.add(pointsText);
		mainTable.add(pTS).uniform();
		mainTable.row();

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

		assignStatArrowButtons();

		tb = new TextButton("Next", skin);
		mainTable.add(tb).expand().bottom().right();

		defineNextButton(tb);

		currentPage = Process.makeStats;
		mainTable.setDebug(true);

	}

	@Override
	public void pause() {
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
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	public Boolean stateChange() {
		return changeState;
	}
}
