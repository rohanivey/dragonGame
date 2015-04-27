package com.rohan.dragonGame;

import com.badlogic.gdx.math.Rectangle;

public class Zone {

	protected Rectangle r;

	public Zone(Rectangle inputRectangle) {
		r = inputRectangle;

	}

	public Rectangle getRectangle() {
		return r;
	}

	public void interact() {
	}

}
