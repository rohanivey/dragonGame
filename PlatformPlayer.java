package com.rohan.dragonGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class PlatformPlayer extends Player {

	private int jumpCount;
	private float jumpCooldown = 0.1f;
	private float vertVelocity = -0.8f;
	private float horVelocity = 0f;
	private float friction = 1f;
	private floorState fs = floorState.inAir;

	enum floorState {
		onGround, inAir, onLadder
	}

	public PlatformPlayer(int inputX, int inputY, Level inputLevel) {
		super(inputX, inputY, inputLevel);
		jumpCount = agility / 4;
		speed = agility;
	}

	@Override
	public void update() {

		handleInput();
		handleCollision();
		checkCollision();
		screenEdging();
		checkLevelChange();

		switch (fs) {
		case onGround:
			reduceJumpCooldown();
			break;
		case inAir:
			applyGravity();
			adjustGravity();
			break;
		case onLadder:
			reduceJumpCooldown();
			break;
		}

	}

	@Override
	public void handleCollision() {
		previousLocation = tempLocation;
		tempLocation = location;
		boundingRectangle.set(location.x - currentFrame.getRegionWidth() / 4,
				location.y, currentFrame.getRegionWidth() / 2,
				currentFrame.getRegionHeight());
	}

	public void reduceJumpCooldown() {
		jumpCooldown -= Gdx.graphics.getDeltaTime();
	}

	public Boolean checkLadderCollision() {

		Rectangle tempCollision = new Rectangle(this.getCollision().x,
				this.getCollision().y + vertVelocity,
				this.getCollision().width, this.getCollision().height
						+ vertVelocity);

		for (Zone z : level.getZones()) {
			if (z instanceof LadderZone) {
				Rectangle r = z.getRectangle();
				// if (Intersector.overlaps(this.getCollision(), r)) {
				if (Intersector.overlaps(tempCollision, r)) {
					return true;
				}
			}
		}

		return false;
	}

	public void applyGravity() {
		this.location.y += vertVelocity;
	}

	public void adjustGravity() {
		if (vertVelocity >= -5.0f)
			vertVelocity += -3f;
		else {
			vertVelocity = -12f;
		}
	}

	enum Direction {
		Left, Right
	}

	public void applySpeed(Direction inputDirection) {
		// I KNOW IT AIN'T PHYSICS KOSHER
		switch (inputDirection) {
		case Left:
			if (horVelocity > speed * -4) {
				horVelocity -= speed / 2;
			}
			break;
		case Right:
			if (horVelocity < speed * 4) {
				horVelocity += speed / 2;
			}
			break;
		}
	}

	public void applyFriction() {
		if (horVelocity > friction && horVelocity > 0) {
			horVelocity -= friction;
		} else if (horVelocity < 0) {
			horVelocity += friction;
		} else if (horVelocity <= friction) {
			horVelocity = 0;
		}
	}

	public void moveDirection(Direction inputDirection) {
		switch (inputDirection) {
		case Left:
			applySpeed(inputDirection);
			break;
		case Right:
			applySpeed(inputDirection);
			break;
		}

	}

	public Boolean checkColliders() {
		Rectangle tempCollision = new Rectangle();
		Boolean canMove = true;

		for (Entity e : level.getCritters()) {
			tempCollision = new Rectangle(this.getCollision().x + horVelocity,
					this.getCollision().y, this.getCollision().width,
					this.getCollision().height);
			if (Intersector.overlaps(tempCollision, e.getCollision())) {
				canMove = false;
				e.handleCollision(this);
				return canMove;
			}
		}
		for (Rectangle r : level.getColliders()) {
			tempCollision = new Rectangle(this.getCollision().x + horVelocity,
					this.getCollision().y, this.getCollision().width,
					this.getCollision().height);
			if (Intersector.overlaps(tempCollision, r)) {
				canMove = false;
				return canMove;
			}
		}
		return canMove;
	}

	public void handleMoving() {

		if (Gdx.input.isKeyPressed(Keys.A)) {

			animationState = AnimationState.Left;
			handleAnimation();
			moveDirection(Direction.Left);
		} else if (Gdx.input.isKeyPressed(Keys.D)) {

			animationState = AnimationState.Right;
			applySpeed(Direction.Right);
			handleAnimation();
		} else {
			applyFriction();
		}
		if (checkColliders()) {
			location.x += horVelocity;
		}

		if (Gdx.input.isKeyPressed(Keys.W)) {
			animationState = AnimationState.Up;
			handleAnimation();
			if (fs == floorState.onLadder && !checkCollision(speed / 2f)) {
				location.y += speed / 2;
			}
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			animationState = AnimationState.Up;
			handleAnimation();
			if (fs == floorState.onLadder && !checkCollision(speed / -2f)) {
				location.y -= speed / 2;
			}
		}

		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			if (fs == floorState.onGround || fs == floorState.onLadder) {
				if (jumpCooldown < 0) {
					jump();
				}
			}
		}

		if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
			if (fs == floorState.onGround || fs == floorState.onLadder) {
				currentState = State.Inventory;
			}
		}

		if (Gdx.input.isKeyJustPressed(Keys.Q)) {
			System.out.println(fs.toString());
		}

	}

	@Override
	public void handleInput() {

		switch (getState()) {
		case Chatting:
			break;
		case Fishing:
			break;
		case Inventory:
			checkInventory();
			break;
		case Moving:
			handleMoving();
			interact();
			break;
		case Trading:
			break;
		default:
			break;
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			writeData();
			System.exit(0);
		}

		if (Gdx.input.isKeyJustPressed(Keys.F9)) {
			level.ms.setDebug();
		}

	}

	public void checkCollision() {
		// Dirty hack
		fs = floorState.inAir;
		if (checkGravityCollision(vertVelocity)) {
			fs = floorState.onGround;
			vertVelocity = 0f;
		}
		if (checkLadderCollision()) {
			fs = floorState.onLadder;
			if (vertVelocity < 0) {
				vertVelocity = 0f;
			}
		}
	}

	public Boolean checkCollision(Float inputVelocity) {
		Rectangle tempCollision = new Rectangle(this.getCollision().x,
				this.getCollision().y + inputVelocity,
				this.getCollision().width, this.getCollision().height
						+ inputVelocity);
		for (Rectangle r : level.getColliders()) {
			if (Intersector.overlaps(tempCollision, r)) {
				// Player hit something
				return true;

			}
		}
		return false;
	}

	public Boolean checkGravityCollision(Float inputVelocity) {
		Rectangle tempCollision = new Rectangle(this.getCollision().x,
				this.getCollision().y + inputVelocity,
				this.getCollision().width, this.getCollision().height
						+ inputVelocity);
		for (Rectangle r : level.getColliders()) {
			if (Intersector.overlaps(tempCollision, r)) {
				// If the player's collision box is greater than the y location
				// of the collider box, the player is above it
				// and therefore must be on the ground
				vertVelocity = 0;
				if (tempCollision.y > r.y) {
					// Player has hit the ground
					return true;
				}
			}
		}

		/*
		 * for(Zone z : level.getZones()) { Rectangle r = z.getRectangle();
		 * if(Intersector.overlaps(tempCollision, r)) { if(tempCollision.y >
		 * r.y) { return true; } }
		 * 
		 * 
		 * }
		 */

		return false;
	}

	public void jump() {
		fs = floorState.inAir;
		vertVelocity = agility + 10f;
		jumpCooldown = 0.1f;
	}

}
