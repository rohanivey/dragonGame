package com.rohan.dragonGame;

import com.badlogic.gdx.math.Rectangle;

public class TeleZone extends Zone {

	private String destination;
	protected mapType type;

	enum mapType {
		ORTHOGRAPHIC, PLATFORM
	}

	public TeleZone(Rectangle inputRectangle, String inputDestination,
			String inputType) {
		super(inputRectangle);
		destination = inputDestination;
		setType(inputType);

	}

	public String getDestination() {
		return destination;
	}

	@Override
	public void interact() {

	}

	public String getType() {
		return type.toString();
	}

	public void setType(String inputType) {
		switch (inputType) {
		case "orthographic":
			type = mapType.ORTHOGRAPHIC;
			break;
		case "platform":
			type = mapType.PLATFORM;
			break;
		}
	}

}
