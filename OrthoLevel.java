package com.rohan.dragonGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.rohan.dragonGame.Player.State;

public class OrthoLevel extends Level {

	public OrthoLevel(MainState inputMS, String inputLevel) {

		super(inputMS, inputLevel);

		// Test world stuff
		// TODO: CHANGE MS TO LEVEL
		player = new Player(30, 30, this);

		// startMusic();
	}

	public OrthoLevel(MainState inputMS) {

		super(inputMS);

		// Test world stuff
		player = new Player(30, 30, this);
		Entity testDog = new Dog(90, 90, player, this);
		addEntity(testDog);
		// startMusic();

		Item testItem = new Item("Dagger", "none", 220, 90);
		itemsOnScreen.add(testItem);
		Item testItem2 = new Item("Shield", "none", 150, 90);
		itemsOnScreen.add(testItem2);
		Item testItem3 = new Item("DonkHammer", "none", 360, 70);
		itemsOnScreen.add(testItem3);
		Item testItem4 = new Item("Dagger", "none", 490, 90);
		itemsOnScreen.add(testItem4);

	}

	@Override
	public void draw() {

		sb.setProjectionMatrix(cam.combined);
		sb.enableBlending();
		mapRenderer.setView(cam);

		if (player.getState() == State.Moving
				|| player.getState() == State.Chatting) {
			sb.begin();

			mapRenderer.renderTileLayer(bg);
			mapRenderer.renderTileLayer(fg);
			mapRenderer.renderTileLayer(oj);
			mapRenderer.renderTileLayer(zones);

			// Draw every item on screen
			for (Item i : itemsOnScreen) {
				sb.draw(i.getTexture(), i.getLocation().x, i.getLocation().y);
			}
			// Draw the player at its current frame
			sb.draw(player.getFrame(), player.getX()
					- player.getFrame().getRegionWidth() / 2, player.getY());
			// Draw every entity on the map
			for (Entity e : critters) {
				sb.draw(e.getTexture(), e.getX(), e.getY());
			}
			// Render overhead tile map
			mapRenderer.renderTileLayer(oh);
			sb.end();
		}

		if (player.getState() == State.Chatting) {
			// Shape renderer and sprite batch both force OpenGL to different
			// states, so you must end one before beginning the next
			// TODO: Replace render chat box with real image, so alpha blending
			// can occur
			player.getActiveEntity().getDialogueHandler().renderChatBox();
			player.getActiveEntity().getDialogueHandler().Draw();

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
