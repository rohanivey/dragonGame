package com.rohan.dragonGame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.rohan.dragonGame.Player.State;

public class MainState extends GameState {

	private MainState ms;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	private Player player;
	private ArrayList<TiledMap> level;

	private SpriteBatch sb;
	private TiledMap map;
	private TiledMapTileLayer bg;
	private TiledMapTileLayer fg;
	private TiledMapTileLayer oj;
	private TiledMapTileLayer oh;
	int[] backgroundLayers;
	int[] overheadLayers;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Music carnivalMusic;
	private ArrayList<Rectangle> colliders;
	private Boolean paused;
	private Boolean changeState = false;

	private ShapeRenderer sr;
	private Rectangle chatBox;
	private Vector2 chatLoc;
	private BitmapFont chatFont;

	private ArrayList<Item> itemsOnScreen;

	private ArrayList<Entity> critters;

	public MainState() {
		ms = this;
		sb = new SpriteBatch();

		cam = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		hudCam = new OrthographicCamera(cam.viewportWidth, cam.viewportHeight);

		paused = false;

		critters = new ArrayList<Entity>();
		itemsOnScreen = new ArrayList<Item>();

		level = new ArrayList<TiledMap>();
		areaLoad("firstMap.tmx");
		System.out.println(colliders.size());

		// Render the map created above at 1/16th scale

		// Music
		carnivalMusic = Gdx.audio.newMusic(Gdx.files
				.internal("Music/carnivalrides.ogg"));
		carnivalMusic.setVolume(0.0f);
		// carnivalMusic.setLooping(true);

		player = new Player(30, 30, ms);
		Entity testDog = new Dog(90, 90, player, ms);
		addEntity(testDog);
		startMusic();

		// Inventory stuff

		Item testItem = new Item("Dagger", "none", 220, 90);
		itemsOnScreen.add(testItem);
		Item testItem2 = new Item("Shield", "none", 150, 90);
		itemsOnScreen.add(testItem2);
		Item testItem3 = new Item("DonkHammer", "none", 360, 70);
		itemsOnScreen.add(testItem3);
		Item testItem4 = new Item("Dagger", "none", 490, 90);
		itemsOnScreen.add(testItem4);

		chatBox = new Rectangle(0, 0, ms.getCamera().viewportWidth,
				ms.getCamera().viewportHeight / 4);
		chatFont = new BitmapFont();
		chatLoc = new Vector2(chatBox.x + 16, chatBox.y + chatBox.height
				- chatFont.getLineHeight());

		// Debug things
		sr = new ShapeRenderer();
		System.out.println("MainState initialized");
	}

	public void addEntity(Entity e) {
		critters.add(e);
	}

	public void areaLoad(String inputString) {
		map = makeTileMap(inputString);
		level.add(map);
		mapRenderer = new OrthogonalTiledMapRenderer(level.get(0), sb);
		if (level.size() > 1) {
			level.remove(1);
		}

	}

	public void cameraHandler() {

		cam.position.set(player.getX()
				+ player.getTextureRegion().getRegionWidth() / 2, player.getY()
				+ player.getTextureRegion().getRegionHeight() / 2, 0);
		if (cam.position.x < 0 + cam.viewportWidth / 2) {
			cam.position.x = cam.viewportWidth / 2;
		}
		if (cam.position.x > (level.get(0).getProperties()
				.get("width", Integer.class) * level.get(0).getProperties()
				.get("tilewidth", Integer.class))
				- cam.viewportWidth / 2) {
			cam.position.x = (level.get(0).getProperties()
					.get("width", Integer.class) * level.get(0).getProperties()
					.get("tilewidth", Integer.class))
					- cam.viewportWidth / 2;
		}
		if (cam.position.y < 0 + cam.viewportHeight / 2) {
			cam.position.y = cam.viewportHeight / 2;
		}
		if (cam.position.y > (level.get(0).getProperties()
				.get("height", Integer.class) * level.get(0).getProperties()
				.get("tileheight", Integer.class))
				- cam.viewportHeight / 2) {
			cam.position.y = (level.get(0).getProperties()
					.get("height", Integer.class) * level.get(0)
					.getProperties().get("tileheight", Integer.class))
					- cam.viewportHeight / 2;
		}
		cam.update();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void draw() {

		mapRenderer.setView(cam);

		sb.setProjectionMatrix(cam.combined);
		sb.enableBlending();

		if (player.getState() == State.Moving
				|| player.getState() == State.Chatting) {
			sb.begin();

			mapRenderer.renderTileLayer(bg);
			mapRenderer.renderTileLayer(fg);
			mapRenderer.renderTileLayer(oj);

			for (Item i : itemsOnScreen) {
				sb.draw(i.getTexture(), i.getLocation().x, i.getLocation().y);
			}

			// Shape testing and boundary checker
			/*
			 * sr.setProjectionMatrix(cam.combined); sr.begin(ShapeType.Filled);
			 * sr.setColor(Color.RED); //sr.rect(player.getCollision().x,
			 * player.getCollision().y, player.getCollision().width,
			 * player.getCollision().height); sr.setColor(Color.BLUE);
			 * //for(Entity e: critters){sr.rect(e.getCollision().x,
			 * e.getCollision().y, e.getCollision().width,
			 * e.getCollision().height);} sr.setColor(Color.BLACK);
			 * sr.circle(player.getInteraction().x, player.getInteraction().y,
			 * player.getInteraction().radius); sr.setColor(Color.GREEN);
			 * //for(Rectangle r: colliders){sr.rect(r.x, r.y, r.width,
			 * r.height);} for(Item i:
			 * itemsOnScreen){sr.rect(i.getCollisionShape().x,
			 * i.getCollisionShape().y, i.getCollisionShape().width,
			 * i.getCollisionShape().height);} sr.end();
			 */

			sb.draw(player.getFrame(), player.getX()
					- player.getFrame().getRegionWidth() / 2, player.getY());
			for (Entity e : critters) {
				sb.draw(e.getTexture(), e.getX(), e.getY());
			}
			mapRenderer.renderTileLayer(oh);
			sb.end();
		}

		if (player.getState() == State.Chatting) {
			// Shape renderer and sprite batch both force OpenGL to different
			// states, so you must end one before beginning the next
			// TODO: Replace render chat box with real image, so alpha blending
			// can occur
			renderChatBox();
			sb.begin();

			if (player.getActiveEntity().getDialogueHandler().getResponding()) {
				setChatLocY(0);
				chatFont.draw(sb, player.getActiveEntity().getDialogueHandler()
						.getResponse(), chatLoc.x, chatLoc.y);
			} else {
				for (int i = 0; i < player.getActiveEntity()
						.getDialogueHandler().getTalkingPoints().size(); i++) {
					setChatLocY(i);
					chatFont.draw(sb, player.getActiveEntity()
							.getDialogueHandler().produceDialogue(i),
							chatLoc.x, chatLoc.y);
				}
				sb.draw(player.getActiveEntity().getDialogueHandler()
						.getTexture(), chatLoc.x - 12,
						getChatLocY(player.getChatSelection()) - 8, 12, 8);
			}
			sb.end();

		}

		if (player.getState() == State.Trading && player.getTradeSetup()) {

			player.getTradeHandler().draw();
			// If the value of the goods in the trade window for the player
			// exceeds the value of the good of the NPC
			sb.begin();
			switch (player.getTradeHandler().checkValue()) {
			case "PLAYER":
				int profit = player.getTradeHandler().getPlayerTotal()
						- player.getTradeHandler().getEntityTotal();
				chatFont.draw(sb, "Player value: "
						+ player.getTradeHandler().getPlayerTotal(), player
						.getTradeHandler().getPICopy().startingX, player
						.getTradeHandler().getPICopy().startingY - 64);
				chatFont.draw(sb, "Entity value: "
						+ player.getTradeHandler().getEntityTotal(), player
						.getTradeHandler().getEICopy().startingX, player
						.getTradeHandler().getEICopy().startingY - 64);
				chatFont.draw(sb, "Profit: " + profit, player.getTradeHandler()
						.getTrade().x
						- player.getTradeHandler().getTradeButton().width / 2,
						player.getTradeHandler().getTrade().y);
				break;
			case "ENTITY":
				int cost = player.getTradeHandler().getEntityTotal()
						- player.getTradeHandler().getPlayerTotal();
				chatFont.draw(sb, "Player value: "
						+ player.getTradeHandler().getPlayerTotal(), player
						.getTradeHandler().getPICopy().startingX, player
						.getTradeHandler().getPICopy().startingY - 64);
				chatFont.draw(sb, "Entity value: "
						+ player.getTradeHandler().getEntityTotal(), player
						.getTradeHandler().getEICopy().startingX, player
						.getTradeHandler().getEICopy().startingY - 64);
				chatFont.draw(sb, "Cost: " + cost, player.getTradeHandler()
						.getTrade().x
						- player.getTradeHandler().getTradeButton().width / 2,
						player.getTradeHandler().getTrade().y);
				break;
			default:
				break;
			}
			chatFont.draw(sb, "Trade", player.getTradeHandler().getTrade().x,
					player.getTradeHandler().getTrade().y);
			// UGH REPLACE THIS WITH METHOD CALLS FOR THE VALUES
			chatFont.draw(
					sb,
					"Player coins: "
							+ String.valueOf(player.getTradeHandler()
									.getPlayerCoins()), player
							.getTradeHandler().getPICopy().startingX, player
							.getTradeHandler().getPICopy().startingY - 32);
			chatFont.draw(
					sb,
					"Entity coins: "
							+ String.valueOf(player.getTradeHandler()
									.getEntityCoins()), player
							.getTradeHandler().getEICopy().startingX, player
							.getTradeHandler().getEICopy().startingY - 32);
			// chatFont.draw(sb, "Trade handler memory address: " +
			// player.getTradeHandler().toString(), 100, 30);
			sb.end();
		}

		if (player.getState() == State.Inventory) {
			player.getInventoryManager().draw();
		}
	}

	public void entityUpdate() {
		for (Entity e : critters) {
			e.update();
		}
	}

	public OrthographicCamera getCamera() {
		return cam;
	}

	public Vector2 getChatLoc() {
		return chatLoc;
	}

	public float getChatLocY(int inputInt) {
		float y = chatBox.height - chatFont.getLineHeight()
				- chatFont.getLineHeight() * inputInt;
		return y;
	}

	public ArrayList<Rectangle> getColliders() {
		return colliders;
	}

	public MainState getCopy() {
		MainState msCopy = new MainState();
		return msCopy;
	}

	public ArrayList<Entity> getCritters() {
		return critters;
	}

	public MapProperties getCurrentMapProperties() {
		return level.get(0).getProperties();
	}

	public OrthographicCamera getHUDCamera() {
		return hudCam;
	}

	public ArrayList<Item> getItems() {
		return itemsOnScreen;
	}

	public SpriteBatch getSpriteBatch() {
		return sb;
	}

	public void itemUpdate() {
		for (Item i : itemsOnScreen) {
			i.handleInteraction(player.getInteraction());
		}
	}

	public TiledMap makeTileMap(String inputString) {
		TiledMap tileMap = new TmxMapLoader().load(inputString);
		colliders = new ArrayList<Rectangle>();
		bg = (TiledMapTileLayer) tileMap.getLayers().get("Background");
		fg = (TiledMapTileLayer) tileMap.getLayers().get("Foreground");
		oj = (TiledMapTileLayer) tileMap.getLayers().get("Objects");
		oh = (TiledMapTileLayer) tileMap.getLayers().get("Overhead");

		for (int row = 0; row < oj.getHeight(); row++) {
			for (int col = 0; col < oj.getWidth(); col++) {
				Cell cell = oj.getCell(col, row);
				if (cell == null)
					continue;
				if (cell.getTile() == null)
					continue;

				Rectangle r = new Rectangle(col * oj.getTileWidth(), row
						* oj.getTileHeight(), oj.getTileWidth(),
						oj.getTileHeight());
				colliders.add(r);
			}
		}

		return tileMap;
	}

	public void pauseUpdate(Boolean inputBoolean) {
		paused = inputBoolean;
	}

	public void renderChatBox() {
		sr.setColor(0, 0, 1, 0.1f);
		sr.setProjectionMatrix(ms.getCamera().combined);

		sr.begin(ShapeType.Filled);
		sr.rect(chatBox.x, chatBox.y, chatBox.width, chatBox.height);
		sr.end();
	}

	public void setChatLocY(int inputY) {
		chatLoc.y = chatBox.height - chatFont.getLineHeight()
				- chatFont.getLineHeight() * inputY;
	}

	@Override
	public void startMusic() {
		carnivalMusic.play();
	}

	public Boolean stateChange() {
		return changeState;
	}

	@Override
	public void stopMusic() {
		carnivalMusic.stop();
	}

	public void stopPlayer() {
		player.fullStop();
	}

	@Override
	public void update() {
		if (!paused) {
			player.update();
			entityUpdate();
			cameraHandler();
		}

	}

}
