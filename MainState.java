package com.rohan.dragonGame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class MainState extends GameState{

	private MainState ms;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	private Player player;
	private ArrayList<TiledMap> level;
	private GameStateManager gsm;
	private SpriteBatch sb;
	private SpriteBatch hud;
	private BitmapFont hudFont;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Music carnivalMusic;
	
	
	private ArrayList<Entity> critters;
	
	public MainState(GameStateManager inputGSM)
	{
		ms = this;
		gsm = inputGSM;
		sb = new SpriteBatch();
		hud = new SpriteBatch();
		cam = new OrthographicCamera(450,450);
		hudCam = new OrthographicCamera(450, 450);
		hudFont = new BitmapFont();
		
		critters = new ArrayList<Entity>();
		
		level = new ArrayList<TiledMap>();
		map = new TmxMapLoader().load("firstMap.tmx");
		level.add(map);
		//Render the map created above at 1/16th scale
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		carnivalMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/carnivalrides.ogg"));
		carnivalMusic.setVolume(0.4f);
		carnivalMusic.setLooping(true);
		
		player = new Player(30, 30, ms);
		
		
		//Box 2d stuff
		addEntity(player);
		
		System.out.println("MainState initialized");
	}
	
	public void update()
	{
		for(int i = 0; i < critters.size(); i++){critters.get(i).update();}
		cameraHandler();
		//System.out.println("MainState updated");
		
	}
	
	public void worldUpdate(){}
	
	public void startMusic(){carnivalMusic.play();}
	public void stopMusic(){carnivalMusic.stop();}
	
	public void cameraHandler()
	{
		cam.position.set(player.getX() + player.getTexture().getRegionWidth()/2, player.getY() + player.getTexture().getRegionHeight()/2, 0);
		if(cam.position.x < 0 + cam.viewportWidth/2){cam.position.x = cam.viewportWidth/2; }
		if(cam.position.x > (level.get(0).getProperties().get("width", Integer.class) * level.get(0).getProperties().get("tilewidth", Integer.class)) - cam.viewportWidth/2){cam.position.x = (level.get(0).getProperties().get("width", Integer.class) * level.get(0).getProperties().get("tilewidth", Integer.class)) - cam.viewportWidth/2;}
		if(cam.position.y < 0 + cam.viewportHeight/2){cam.position.y = cam.viewportHeight/2; }
		if(cam.position.y > (level.get(0).getProperties().get("height", Integer.class) * level.get(0).getProperties().get("tileheight", Integer.class)) - cam.viewportHeight/2){cam.position.y = (level.get(0).getProperties().get("height", Integer.class) * level.get(0).getProperties().get("tileheight", Integer.class)) - cam.viewportHeight/2;}
		cam.update();
	}
	
	public void draw()
	{
		//System.out.println("Mainstate drawn");
		
		mapRenderer.setView(cam);
		mapRenderer.render();
		
		sb.setProjectionMatrix(cam.combined);
		
		sb.begin();
		//sb.draw(player.getTexture(), player.getX() - player.getTexture().getWidth()/2, player.getY() - player.getTexture().getWidth()/2);
		//sb.draw(player.getFrame(), player.getX() - player.getTexture().getRegionWidth()/2, player.getY() - player.getTexture().getRegionHeight()/2);
		sb.draw(player.getFrame(), player.getX(), player.getY());
		Texture t;
		sb.draw(t = new Texture("badlogic.jpg"), player.getInteraction().x, player.getInteraction().y, player.getInteraction().area(), player.getInteraction().area());
		sb.end();
		
		hud.setProjectionMatrix(hudCam.combined);
		hud.begin();
		hudFont.draw(hud, "PlayerX:" + player.getX(), 60, 20);
		hudFont.draw(hud, "Rectangle X:" + player.getCollision().x, 60, 10);
		hud.end();
		
		
	}
	
	public void addEntity(Entity e)
	{
		
		critters.add(e);
	}
	
	public void dispose()
	{
			
	}
	
	public MapProperties getCurrentMapProperties(){ return level.get(0).getProperties();}
	public GameStateManager getGSM(){return gsm;}
	
}
