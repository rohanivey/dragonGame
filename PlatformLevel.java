package com.rohan.dragonGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class PlatformLevel extends Level {

	public PlatformLevel(MainState inputMS, String inputLevel) {
		super(inputMS, inputLevel);

		player = new PlatformPlayer(30, 90, this);

	}

	public void drawInventory() {
		player.getInventoryManager().draw();
	}

	public void drawMap() {
		sb.begin();
		mapRenderer.setView(cam);

		mapRenderer.renderTileLayer(bg);
		mapRenderer.renderTileLayer(fg);
		mapRenderer.renderTileLayer(oj);
		mapRenderer.renderTileLayer(zones);

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

	@Override
	public void draw() {
		sb.setProjectionMatrix(cam.combined);
		sb.enableBlending();
		switch (player.getState()) {
		case Chatting:
			break;
		case Fishing:
			break;
		case Inventory:
			drawInventory();
			break;
		case Moving:
			drawMap();
			break;
		case Trading:
			break;
		default:
			break;
		}

		if (ms.getDebug()) {
			sr.setProjectionMatrix(cam.combined);
			sr.setAutoShapeType(true);
			sr.setColor(Color.BLACK);
			sr.begin();
			sr.set(ShapeType.Filled);
			for (Zone z : zoneArray) {
				Rectangle r = z.getRectangle();
				if (z instanceof TeleZone)
					sr.setColor(Color.BLACK);
				if (z instanceof LadderZone)
					sr.setColor(Color.BLUE);
				sr.rect(r.x, r.y, r.width, r.height);
			}
			sr.end();
		}

	}

}
