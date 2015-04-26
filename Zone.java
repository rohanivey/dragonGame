package com.rohan.dragonGame;

import com.badlogic.gdx.math.Rectangle;

public class Zone {

	protected Rectangle r;
	protected String destination;
	protected mapType type;

	enum mapType {
		ORTHOGRAPHIC, PLATFORM
	}

	public Zone(Rectangle inputRectangle, String inputDestination,
			String inputType) {
		r = inputRectangle;
		destination = inputDestination;
		setType(inputType);

	}

	public String getDestination() {
		return destination;
	}

	public Rectangle getRectangle() {
		return r;
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
