package com.rohan.dragonGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TradeHandler {

	private Boolean trading = true;
	private InventoryManager playerInventoryCopy;
	private InventoryManager entityInventoryCopy;
	private InventoryManager tradeInventory;
	private BitmapFont font;
	private Boolean goodTrade = false;
	private String entityName;
	private Entity currentEntity;
	private Player player;
	private int playerCoins, entityCoins;
	private Rectangle tradeButton;

	TradeHandler(Player inputPlayer, Entity inputEntity) {
		font = new BitmapFont();
		player = inputPlayer;
		playerInventoryCopy = inputPlayer.getInventoryManager().copyMe();
		currentEntity = inputEntity;
		currentEntity.setupTrade();
		entityInventoryCopy = inputEntity.getInventoryManager().copyMe();
		entityName = currentEntity.getName();
		tradeInventory = new InventoryManager(this);
		playerCoins = player.getCoins();
		entityCoins = currentEntity.getCoins();
		tradeButton = new Rectangle();
		tradeButton.height = 100f;
		tradeButton.width = 200f;
		tradeButton.x = Gdx.graphics.getWidth() / 2;
		tradeButton.y = Gdx.graphics.getHeight() / 2;
	}

	public String checkValue() {
		// If the value of the goods the player is submitting is greater than
		// the Entity's, the player's items have higher value
		String winner;
		if (getPlayerTotal() >= getEntityTotal()) {
			winner = "PLAYER";
		} else {
			winner = "ENTITY";
		}
		return winner;
	}

	public Boolean checkPlayerWorthAndCoins() {
		int playerWorth = getPlayerTotal() + playerCoins;
		// Otherwise, if the value of the player's coin + traded items is
		// greater than the value the entity's trade goods
		if (playerWorth >= getEntityTotal()) {
			System.out
					.println("TradeHandler.checkPlayerWorthAndCoins() has determined the player has enough coin and value to pay entity");
			return true;
		}
		// Otherwise, the player doesn't have enough net worth to trade for what
		// they want
		System.out
				.println("TradeHandler.checkPlayerWorthAndCoins() has determined the player does not have enough coin and value to pay entity");
		return false;
	}

	public Boolean checkEntityWorthAndCoins() {
		int entityWorth = getEntityTotal() + entityCoins;
		// Otherwise, if the value of the entity's coin + traded items is
		// greater than the value the player's trade goods
		if (entityWorth >= getPlayerTotal()) {
			System.out
					.println("TradeHandler.checkEntityWorthAndCoins() has determined the entity has enough coin and value to pay player");
			return true;
		}
		// Otherwise, the entity doesn't have enough net worth to trade for what
		// they want
		System.out
				.println("TradeHandler.checkEntityWorthAndCoins() has determined the entity does not have enough coin and value to pay player");
		return false;
	}

	public void concludeTrade()

	{
		player.setInventoryManager(playerInventoryCopy.copyMe());
		currentEntity.setInventoryManager(entityInventoryCopy.copyMe());
		// System.out.println("TradeHandler.concludeTrade() is setting player coins from copy.\n Copy coins: "
		// + playerCoins);
		player.setCoins(playerCoins);
		// System.out.println("TradeHandler.concludeTrade() has set player coins.\n Player coins: "
		// + player.getCoins());
		currentEntity.setCoins(entityCoins);
		setTrading(false);
		currentEntity.getDialogueHandler().setTrading(false);
		player.setStateChatting();
		player.setupTrading();
		dispose();

	}

	public void draw() {

		if (entityInventoryCopy.getReady()) {
			entityInventoryCopy.draw();
		}

		if (tradeInventory.getReady()) {
			tradeInventory.draw();
		}

		if (playerInventoryCopy.getReady()) {
			playerInventoryCopy.draw();
		}
	}

	public void exchangeCoins() {
		// TODO: IMPLEMENT BARTER SKILL TO ADJUST THE AMOUNT OF COIN PAID?
		// If the value of the entity's goods are greater than the player's
		// goods, the player should pay money
		System.out.println("TradeHandler.exchangeCoins() is beginning");
		if (getEntityTotal() > getPlayerTotal()) {
			int difference = getEntityTotal() - getPlayerTotal();
			playerCoins -= difference;
			entityCoins += difference;
			System.out
					.println("TradeHandler.exchangeCoins() is paying the entity "
							+ difference);

		}
		// Else if the value of the player's goods are greater than the
		// entity's, the entity should pay money
		else if (getPlayerTotal() > getEntityTotal()) {
			// If the entity has the cash on hand to actually trade goods
			int difference = getPlayerTotal() - getEntityTotal();
			if (getEntityCoins() >= difference) {
				playerCoins += difference;
				entityCoins -= difference;
				System.out
						.println("TradeHandler.exchangeCoins() is paying the player "
								+ difference);
			} else {
				System.out.println("The entity does not have enough money");
			}
		}
	}

	public void exchangeGoods() {
		for (int row = 0; row < tradeInventory.gridHeight; row++) {
			for (int col = 0; col < tradeInventory.gridWidth; col++) {
				// If the item in the slot is marked for trading
				if (tradeInventory.getGrid()[row][col].getCurrentItem() != null) {
					// If the item is owned by the player, it needs to go to the
					// entity
					if (tradeInventory.getGrid()[row][col].getCurrentItem()
							.getOwner() == "PLAYER") {
						// Set the owner of the item for trade as the entity
						tradeInventory.getGrid()[row][col].getCurrentItem()
								.setOwner(entityName);
						System.out.println("TradeHandler.exchangeGoods() item "
								+ tradeInventory.getGrid()[row][col]
										.getCurrentItem().getInputString()
								+ " is setting ownership to " + entityName);
						// Add the item to the entity's inventory
						entityInventoryCopy
								.addItem(tradeInventory.getGrid()[row][col]
										.getCurrentItem());
						// Remove the item from the player's inventory
						tradeInventory
								.removeItem(tradeInventory.getGrid()[row][col]);
					}
					// Else, if the item is owned by the entity, the player
					// needs to take it
					else {
						// Set the owner of the item for trade as the player
						tradeInventory.getGrid()[row][col].getCurrentItem()
								.setOwner("PLAYER");
						System.out.println("TradeHandler.exchangeGoods() item "
								+ tradeInventory.getGrid()[row][col]
										.getCurrentItem().getInputString()
								+ " is setting ownership to " + entityName);
						// Add the item to the entity's inventory
						playerInventoryCopy
								.addItem(tradeInventory.getGrid()[row][col]
										.getCurrentItem());
						// Remove the item from the trade inventory
						tradeInventory
								.removeItem(tradeInventory.getGrid()[row][col]);
					}
				}
			}
		}
	}

	public Entity getCurrentEntity() {
		return currentEntity;
	}

	public InventoryManager getEICopy() {
		return entityInventoryCopy;
	}

	public int getEntityCoins() {
		return entityCoins;
	}

	public int getPlayerCoins() {
		return playerCoins;
	}

	public int getEntityTotal() {
		int entityTotal = 0;
		for (int row = 0; row < tradeInventory.gridHeight; row++) {
			for (int col = 0; col < tradeInventory.gridWidth; col++) {
				// If the item in the slot is marked for trading
				if (tradeInventory.getGrid()[row][col].getCurrentItem() != null) {
					if (tradeInventory.getGrid()[row][col].getCurrentItem()
							.getOwner() == "PLAYER") {
					} else {
						entityTotal += tradeInventory.getGrid()[row][col]
								.getCurrentItem().getValue();
					}
				}
			}
		}
		return entityTotal;
	}

	public BitmapFont getFont() {
		return font;
	}

	public Boolean getGoodTrade() {
		return goodTrade;
	}

	public Cursor getMainCursor() {
		return playerInventoryCopy.getCursor();
	}

	public InventoryManager getPBCopy() {
		return tradeInventory;
	}

	public InventoryManager getPICopy() {
		return playerInventoryCopy;
	}

	public int getPlayerTotal() {
		int playerTotal = 0;
		for (int row = 0; row < tradeInventory.gridHeight; row++) {
			for (int col = 0; col < tradeInventory.gridWidth; col++) {
				// If the item in the slot is marked for trading
				if (tradeInventory.getGrid()[row][col].getCurrentItem() != null) {
					if (tradeInventory.getGrid()[row][col].getCurrentItem()
							.getOwner() == "PLAYER") {
						playerTotal += tradeInventory.getGrid()[row][col]
								.getCurrentItem().getValue();
					}
				}
			}
		}
		// System.out.println(String.valueOf(playerTotal));
		return playerTotal;
	}

	public Boolean getTrading() {
		return trading;
	}

	public Vector2 getTrade() {
		Vector2 tempV = new Vector2(tradeButton.x, tradeButton.y);
		return tempV;
	}

	public Rectangle getTradeButton() {
		return tradeButton;
	}

	public void setTrading(Boolean inputBoolean) {
		trading = inputBoolean;
	}

	public void tryTrade() {
		System.out.println("TradeHandler.tryTrade() is beginning");
		String value = checkValue();
		System.out
				.println("TradeHandler.checkValue() has determined the higher value belongs to "
						+ value);
		switch (value) {
		// This means the submitted items have a value greater than the entity's
		case "PLAYER":
			if (checkEntityWorthAndCoins()) {
				exchangeCoins();
				exchangeGoods();
				concludeTrade();
			}
			break;
		// This means the submitted items are less valuable than the entity's
		case "ENTITY":
			if (checkPlayerWorthAndCoins()) {
				exchangeCoins();
				exchangeGoods();
				concludeTrade();
			}
			break;
		default:
			System.out
					.println("TradeHandler.checkValue() goofed up somewhere and returned default value");
			break;
		}
	}

	public void Update() {
		playerInventoryCopy.update();
		entityInventoryCopy.update();
		tradeInventory.update();
		tradeButtonUpdate();

		if (Gdx.input.isKeyPressed(Keys.Q)) {
			for (int row = 0; row < entityInventoryCopy.getGrid().length; row++) {
				for (int col = 0; col < entityInventoryCopy.getGrid()[row].length; col++) {
					if (entityInventoryCopy.getGrid()[row][col]
							.getCurrentItem() != null) {
						System.out
								.println(entityInventoryCopy.getGrid()[row][col]
										.getCurrentItem().getInputString());
					}
				}
			}
		}

	}

	public void tradeButtonUpdate() {
		if (Gdx.input.justTouched()) {
			if (tradeButton.contains(Gdx.input.getX(), Gdx.input.getY())) {
				this.tryTrade();
			}
		}
	}

	public void dispose() {
		System.out
				.println("TradeHandler.dispose called. Disposing all input entities");
		player = null;
		currentEntity = null;
	}

}
