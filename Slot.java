package com.rohan.dragonGame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Slot {

	enum slotType {
		MASTER, SLAVE, UNUSED
	}

	private Item currentItem;
	private Slot masterSlot;
	private InventoryManager myIM;
	private int widthLoc, heightLoc, startingX, startingY;
	private Texture img = new Texture("box.png");
	private int slotWidth = img.getWidth();
	private int slotHeight = img.getHeight();
	private float clickCooldown = 0.2f;

	private ArrayList<Slot> slaveList;

	private slotType currentType = slotType.UNUSED;

	public Slot(InventoryManager inputIM, int inputRow, int inputCol,
			int inputX, int inputY) {
		myIM = inputIM;
		heightLoc = inputRow;
		widthLoc = inputCol;

		startingX = inputX;
		startingY = inputY;

		slaveList = new ArrayList<Slot>();
	}

	public void addSlave(Slot inputSlot) {
		slaveList.add(inputSlot);
	}

	public Boolean checkCoords() {
		float tempX = myIM.getCursor().getSprite().getX();
		float tempY = myIM.getCursor().getSprite().getY();

		// if the slot's starting location is less than the mouse x location and
		// the slot's width at its end is greater than the mouse X
		if (widthLoc * slotWidth + startingX <= tempX
				&& (widthLoc + 1) * slotWidth + startingX - 1 >= tempX) {
			// AND if the slot's starting location is less than the mouse y
			// location and the slot's height at its end is greater than the
			// mouse Y
			if (heightLoc * slotHeight + startingY <= tempY
					&& (heightLoc + 1) * slotHeight + startingY - 1 >= tempY) {
				return true;
			}
		}

		return false;
	}

	public void checkMasterInput() {
		// If the master slot is touched
		if (Gdx.input.isTouched()) {
			touchMaster();
			System.out
					.println("Touching master through slave slot in Slot.checkMasterInput()");
		}
	}

	public void checkSlaveInput() {
		// If the slave slot is touched
		if (Gdx.input.isTouched()) {
			// Make its master update touch
			masterSlot.touchMaster();
		}
	}

	public void checkType() {
		switch (currentType) {
		case MASTER:
			// System.out.println("Master Input is being checked in Slot.checkType()");
			checkMasterInput();
			break;
		case SLAVE:
			// System.out.println("Slave Input is being checked in Slot.checkType()");
			checkSlaveInput();
			break;
		case UNUSED:
			// System.out.println("Unused Input is being checked in Slot.checkType()");
			checkUnusedInput();
			break;
		}
	}

	public void checkUnusedInput() {
		// If the unused slot is touched
		if (Gdx.input.isTouched()) {
			// System.out.println("Checking state input in Slot.checkUnusedInput()");
			// If the cursor has an item in hand and this slot has no item
			if (myIM.getCursor().getItem() != null && currentItem == null
					&& myIM.getCursor().testOwner(this)) {
				// System.out.println("Cursor has an item, slot does not have item");
				// Check with the master grid to see if there's room for this
				// item at this slot's location
				if (myIM.checkGridRoom(myIM.getCursor().getItem(), heightLoc,
						widthLoc)) {
					// System.out.println("Adjusting slot in Slot.touchMaster()");
					// If there is room, then set this slot's item to the
					// cursor's item
					setItem(myIM.getCursor().getItem());
					// Change the cursor back to a pointer
					myIM.getCursor().defaultTexture();
					// Set the cursor's item to null
					myIM.getCursor().setItem(null);
					// Set this slot to a master
					setType("MASTER");
					// Add every slot occupied by the item to the slaveList
					// arrayList
					myIM.enslaveSlots(currentItem, heightLoc, widthLoc);
				}
			}
		}
	}

	public void draw(int inputRow, int inputCol) {
		// BitmapFont font = new BitmapFont();

		switch (currentType) {
		case UNUSED:
			myIM.sb.draw(img, widthLoc * slotWidth + startingX, heightLoc
					* slotHeight + startingY);
			// font.draw(myIM.sb, "[" + inputRow + ", " + inputCol + "]: U",
			// widthLoc * (slotWidth+32), heightLoc * slotHeight+320);
			break;
		case MASTER:
			myIM.sb.draw(currentItem.getTexture(), widthLoc * slotWidth
					+ startingX, heightLoc * slotHeight + startingY, slotWidth
					* currentItem.getGridWidth(),
					slotHeight * currentItem.getGridHeight());
			// font.draw(myIM.sb,"[" + inputRow + ", " + inputCol + "]:M",
			// widthLoc * (slotWidth+32), heightLoc * slotHeight+320);
			break;
		case SLAVE:
			// font.draw(myIM.sb,"[" + inputRow + ", " + inputCol + "]: S",
			// widthLoc * (slotWidth+32), heightLoc * slotHeight+320);
			break;
		}
	}

	public void forceType(String inputString) {
		switch (inputString) {
		case "MASTER":
			currentType = slotType.MASTER;
			break;
		case "SLAVE":
			currentType = slotType.SLAVE;
			break;
		case "UNUSED":
			currentType = slotType.UNUSED;
			System.out
					.println("Current Slot type forced to UNUSED inside Slot.forceType()");
			break;
		}
	}

	public int getCol() {
		return widthLoc;
	}

	public Item getCurrentItem() {
		return currentItem;
	}

	public String getCurrentType() {
		return currentType.toString();
	}

	public InventoryManager getIM() {
		return myIM;
	}

	public Slot getMasterSlot() {
		return masterSlot;
	}

	public int getRow() {
		return heightLoc;
	}

	public void setItem(Item inputItem) {
		// System.out.println("Setting item in Slot.setItem()");
		currentItem = inputItem;
	}

	public void setMaster(Slot inputSlot) {
		masterSlot = inputSlot;
	}

	public void setType(String inputType) {
		switch (inputType) {
		case "MASTER":
			// Make a new ArrayList of Slots
			slaveList = new ArrayList<Slot>();
			// Set this type to MASTER
			currentType = slotType.MASTER;
			// System.out.println("Setting type to MASTER in Slot.setType()");
			break;
		case "SLAVE":
			currentType = slotType.SLAVE;
			// System.out.println("Setting type to SLAVE in Slot.setType()");
			break;
		case "UNUSED":
			// System.out.println("Setting type to UNUSED in Slot.setType()");
			if (slaveList != null) {
				// System.out.println("Slave list is > 0 in Slot.setType()");
				for (Slot s : slaveList) {
					s.forceType("UNUSED");
				}
				// System.out.println("Setting slave list to null in Slot.setType()");
				slaveList = null;
			}
			forceType("UNUSED");
			currentItem = null;
			break;

		}
	}

	public void touchMaster() {
		System.out.println("Checking state input in Slot.touchMaster()");
		// If the cursor has no item in hand
		if (myIM.getCursor().getItem() == null) {
			// System.out.println("Cursor has no item, adjusting slot in Slot.touchMaster()");
			// Give it this slot's current item
			myIM.getCursor().setItem(currentItem);
			// Set the cursor as the item in question's texture
			myIM.getCursor().setTexture();
			// Set this slot and all slaves to unused again
			setType("UNUSED");
		} else if (myIM.getCursor().getItem() != null
				&& myIM.getCursor().testOwner(this)) {
			// System.out.println("Cursor has an item, however there is already an item in Slot at Slot.touchMaster()");
			if (myIM.getCursor().testSwap(this)) {
				System.out
						.println("Slot.touchMaster() is now calling its cursor.swapItem() method");
				myIM.getCursor().swapItem(this);
			}
		}

	}

	public void update() {
		clickCooldown -= Gdx.graphics.getDeltaTime();
		// System.out.println(clickCooldown);
		if ((clickCooldown <= 0) & (Gdx.input.isTouched())) {
			clickCooldown = 0.2f;
			// System.out.println("The player has clicked");
			if (checkCoords()) {
				System.out
						.println("The player has clicked within a slot's bounds");
				System.out.println("Width: " + widthLoc + " Height: "
						+ heightLoc);
				checkType();
			}
		}
	}

}
