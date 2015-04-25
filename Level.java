package com.rohan.dragonGame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Level {

	protected SpriteBatch sb;
	protected TiledMap map;
	protected TiledMapTileLayer bg;
	protected TiledMapTileLayer fg;
	protected TiledMapTileLayer oj;
	protected TiledMapTileLayer oh;
	protected OrthogonalTiledMapRenderer mapRenderer;
	protected ArrayList<Rectangle> colliders;
	protected ArrayList<Item> itemsOnScreen;
	protected ArrayList<Entity> critters;
	protected OrthographicCamera cam;
	protected Player player;
	protected BitmapFont chatFont;

	public Player getPlayer() {
		return player;
	}

	protected MainState ms;
	protected Music levelMusic;
	protected ArrayList<TiledMap> level;

	protected DialogueHandler dh;

	public Level(MainState inputMS, String inputLevel) {
		level = new ArrayList<TiledMap>();
		critters = new ArrayList<Entity>();
		itemsOnScreen = new ArrayList<Item>();
		ms = inputMS;
		dh = new DialogueHandler();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		chatFont = new BitmapFont();
		sb = new SpriteBatch();

		areaLoad(inputLevel);

		// //////////
		/*
		 * 
		 * ADD LEVEL LOADER HERE FOR PARSING THE LEVEL carnivalMusic =
		 * Gdx.audio.newMusic(Gdx.files .internal("Music/carnivalrides.ogg"));
		 */
	}

	public Level(MainState inputMS) {
		level = new ArrayList<TiledMap>();
		critters = new ArrayList<Entity>();
		itemsOnScreen = new ArrayList<Item>();
		ms = inputMS;
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		chatFont = new BitmapFont();
		dh = new DialogueHandler();
		sb = new SpriteBatch();

		areaLoad("firstMap.tmx");
		// //////////
		/*
		 * 
		 * ADD LEVEL LOADER HERE FOR PARSING THE LEVEL carnivalMusic =
		 * Gdx.audio.newMusic(Gdx.files .internal("Music/carnivalrides.ogg"));
		 */
	}

	public void update() {
		player.update();
		entityUpdate();
		cameraHandler();
	}

	public void areaLoad(String inputString) {
		System.out.println("Level.areaLoad() is beginning");
		map = makeTileMap(inputString);
		mapRenderer = new OrthogonalTiledMapRenderer(map, sb);

		// mapRenderer.setView(cam);

	}

	public void entityUpdate() {
		for (Entity e : critters) {
			e.update();
		}
	}

	public OrthographicCamera getCamera() {
		return cam;
	}

	public SpriteBatch getSB() {
		return sb;
	}

	public void draw() {
	}

	public void cameraHandler() {

		cam.position.set(player.getX()
				+ player.getTextureRegion().getRegionWidth() / 2, player.getY()
				+ player.getTextureRegion().getRegionHeight() / 2, 0);
		if (cam.position.x < 0 + cam.viewportWidth / 2) {
			cam.position.x = cam.viewportWidth / 2;
		}
		if (cam.position.x > (map.getProperties().get("width", Integer.class) * map
				.getProperties().get("tilewidth", Integer.class))
				- cam.viewportWidth / 2) {
			cam.position.x = (map.getProperties().get("width", Integer.class) * map
					.getProperties().get("tilewidth", Integer.class))
					- cam.viewportWidth / 2;
		}
		if (cam.position.y < 0 + cam.viewportHeight / 2) {
			cam.position.y = cam.viewportHeight / 2;
		}
		if (cam.position.y > (map.getProperties().get("height", Integer.class) * map
				.getProperties().get("tileheight", Integer.class))
				- cam.viewportHeight / 2) {
			cam.position.y = (map.getProperties().get("height", Integer.class) * map
					.getProperties().get("tileheight", Integer.class))
					- cam.viewportHeight / 2;
		}
		cam.update();
	}

	public ArrayList<Rectangle> getColliders() {
		return colliders;
	}

	public void stopMusic() {
		levelMusic.stop();
	}

	public void stopPlayer() {
		player.fullStop();
	}

	public void startMusic() {
		levelMusic.play();
	}

	public ArrayList<Entity> getCritters() {
		return critters;
	}

	public MapProperties getCurrentMapProperties() {
		return map.getProperties();
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

	public void setPlayer(Player inputPlayer) {
		player = inputPlayer;
	}

	public void addEntity(Entity e) {
		critters.add(e);
	}

}