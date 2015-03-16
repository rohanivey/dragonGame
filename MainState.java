package com.rohan.dragonGame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MainState extends GameState{

	private GameState gs;
	private MainState ms;
	private OrthographicCamera cam;
	private Player player;
	private ArrayList<TiledMap> world;
	private GameStateManager gsm;
	private SpriteBatch sb;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Music carnivalMusic;
	
	public MainState(GameStateManager inputGSM)
	{
		gs = this;
		ms = this;
		gsm = inputGSM;
		sb = new SpriteBatch();
		cam = new OrthographicCamera(450,450);

		world = new ArrayList<TiledMap>();
		map = new TmxMapLoader().load("firstMap.tmx");
		world.add(map);
		//Render the map created above at 1/16th scale
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		carnivalMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/carnivalrides.ogg"));
		carnivalMusic.setVolume(0.4f);
		carnivalMusic.setLooping(true);
		
		player = new Player(30, 30, ms);
		
		System.out.println("MainState initialized");
	}
	
	public void update()
	{
		player.update();
		cameraHandler();
		//System.out.println("MainState updated");
		
	}
	
	public void startMusic(){carnivalMusic.play();}
	public void stopMusic(){carnivalMusic.stop();}
	
	public void cameraHandler()
	{
		cam.position.set(player.getX() + player.getTexture().getRegionWidth()/2, player.getY() + player.getTexture().getRegionHeight()/2, 0);
		if(cam.position.x < 0 + cam.viewportWidth/2){cam.position.x = cam.viewportWidth/2; }
		if(cam.position.x > (world.get(0).getProperties().get("width", Integer.class) * world.get(0).getProperties().get("tilewidth", Integer.class)) - cam.viewportWidth/2){cam.position.x = (world.get(0).getProperties().get("width", Integer.class) * world.get(0).getProperties().get("tilewidth", Integer.class)) - cam.viewportWidth/2;}
		if(cam.position.y < 0 + cam.viewportHeight/2){cam.position.y = cam.viewportHeight/2; }
		if(cam.position.y > (world.get(0).getProperties().get("height", Integer.class) * world.get(0).getProperties().get("tileheight", Integer.class)) - cam.viewportHeight/2){cam.position.y = (world.get(0).getProperties().get("height", Integer.class) * world.get(0).getProperties().get("tileheight", Integer.class)) - cam.viewportHeight/2;}
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
		sb.end();
		//world.get(0).draw();
	}
	public void dispose()
	{
			
	}
	
	public MapProperties getCurrentMapProperties(){ return world.get(0).getProperties();}
	
}
