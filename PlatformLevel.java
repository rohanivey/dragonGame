package com.rohan.dragonGame;

public class PlatformLevel extends Level {

	public PlatformLevel(MainState inputMS, String inputLevel) {
		super(inputMS, inputLevel);

		player = new Player(30, 30, this);

	}

	@Override
	public void draw() {
		sb.setProjectionMatrix(cam.combined);
		sb.enableBlending();
		sb.begin();

		mapRenderer.setView(cam);

		mapRenderer.renderTileLayer(bg);
		mapRenderer.renderTileLayer(fg);
		mapRenderer.renderTileLayer(oj);

		for (Item i : itemsOnScreen) {
			sb.draw(i.getTexture(), i.getLocation().x, i.getLocation().y);
		}

		sb.draw(player.getFrame(), player.getX()
				- player.getFrame().getRegionWidth() / 2, player.getY());

		for (Entity e : critters) {
			sb.draw(e.getTexture(), e.getX(), e.getY());
		}

		mapRenderer.renderTileLayer(oh);
		sb.end();

	}

}
