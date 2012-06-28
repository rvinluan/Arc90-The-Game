package arc90.rv;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.*;

import java.util.ArrayList;

public class Entity {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	public static final int MOVING = 3;
	public static final int IDLE = 4;
	public static final int ATTACKING = 5;
	public static final int HURT = 6;
	public static final int DYING = 7;
	
	public int FRAME_DURATION = 55; //ms
	public int MAX_VELOCITY = 2; //px

	protected int facing;
	protected int state;
	protected boolean dead;
	
	protected Animation walkLeft;
	protected Animation walkRight;
	protected Animation idleLeft;
	protected Animation idleRight;
	protected Animation attackLeft;
	protected Animation attackRight;
	protected Animation hurtLeft;
	protected Animation hurtRight;
	protected Animation dyingLeft;
	protected Animation dyingRight;
	
	protected Rectangle aabb;
	
	protected Vector2f coordinates;
	protected Vector2f velocity;

	public Entity() {
		coordinates = new Vector2f(10,32+MainGame.guiHeight);
		velocity = new Vector2f(0,0);
		walkLeft = new Animation();
		idleLeft = new Animation();
		walkRight = new Animation();
		idleRight = new Animation();
		attackRight = new Animation();
		attackLeft = new Animation();
		hurtRight = new Animation();
		hurtLeft = new Animation();
		
		facing = Entity.RIGHT;
		state = Entity.IDLE;
				
		aabb = new Rectangle(coordinates.x, coordinates.y, 16, 16);
	}
	
	public Entity(Image walkSpriteSheet, Image idleSpriteSheet, Image attackSpriteSheet, Image hurtSpriteSheet) {
		this();
		
		walkLeft = setupAnimation(walkSpriteSheet, 32, 64, true);
		walkRight = setupAnimation(walkSpriteSheet, 32, 64, false);
		idleLeft = setupAnimation(idleSpriteSheet, 32, 64, true);
		idleRight = setupAnimation(idleSpriteSheet, 32, 64, false);
		attackLeft = setupAnimation(attackSpriteSheet, 32, 64, true);
		attackRight = setupAnimation(attackSpriteSheet, 32, 64, false);
		hurtLeft = setupAnimation(hurtSpriteSheet, 32, 64, true);
		hurtRight = setupAnimation(hurtSpriteSheet, 32, 64, false);

		aabb = new Rectangle(coordinates.x + 5, coordinates.y + 5, 16, 32);
		
	}
	
	protected Animation setupAnimation(Image spritesheet, int frameWidth, int frameHeight, boolean flipped) {
		Animation a = new Animation();
		
		for(int i = 0; i < (spritesheet.getWidth() / frameWidth); i++) {
			a.addFrame(spritesheet.getSubImage(
						0 + frameWidth*i,
						0,
						frameWidth,
						frameHeight
					).getFlippedCopy(flipped, false), this.FRAME_DURATION);
		}
		
		
		return a;
	}
	
	protected Animation setupAnimation(Image spritesheet, int frameWidth, int frameHeight, boolean flipped, int fd) {
		Animation a = new Animation();
		
		for(int i = 0; i < (spritesheet.getWidth() / frameWidth); i++) {
			a.addFrame(spritesheet.getSubImage(
						0 + frameWidth*i,
						0,
						frameWidth,
						frameHeight
					).getFlippedCopy(flipped, false), fd);
		}
		
		
		return a;	
	}
	
	public Rectangle getAABB() {
		return aabb;
	}
	
	public void setFacing(int f) {
		this.facing = f;
	}
	
	public void setState(int s) {
		this.state = s;
	}
	
	public int getState() {
		return state;
	}
	
	public void setCoords(int cx, int cy) {
		coordinates.x = cx;
		coordinates.y = cy;
	}
	
	public void collisionCheck(ArrayList<Entity> entities, Room map) {
		int moveX = 100;
		Entity collidedWith = null;
		
		//find which vertical rows i'm in
		int topRow = ((int)aabb.getMinY() - MainGame.guiHeight) / 16;
		int bottomRow = ((int)aabb.getMaxY() - MainGame.guiHeight) / 16;
		
		//System.out.println(topRow);
		//System.out.println(bottomRow);
		
		for(int j = topRow; j <= bottomRow; j++) {
			float furthestDistance = 0;
			boolean collided = false;
			if(velocity.x < 0) {
				for(int i = ((int)aabb.getMinX() - map.offsetLeft) / 16; i >= 0; i--) {
					for(Entity e : entities) {
						if(e.isHere(i, j, map.offsetLeft) && e != this) {
							furthestDistance = this.aabb.getMinX() - e.aabb.getMaxX();
							collided = true;
							collidedWith = e;
							break;
						}
					}
					if(collided) {
						break;
					} else {
						furthestDistance += 16;
					}
				}
			} else if(velocity.x > 0) {
				for(int i = ((int)aabb.getMaxX() - map.offsetLeft) / 16; i <= map.tiles.length - 1; i++) {
					for(Entity e : entities) {
						if(e.isHere(i, j, map.offsetLeft) & e != this) {
							furthestDistance = e.aabb.getMinX() - this.aabb.getMaxX();
							collided = true;
							collidedWith = e;
							break;
						}
					}
					if(collided) {
						break;
					} else {
						furthestDistance += 16;
					}
				}

			} //end right
			
			if(furthestDistance < 0) {
				furthestDistance = 0;
			}
									
			if(j == topRow || furthestDistance < moveX) {
				moveX = (int)furthestDistance;
			}
						
			if ((int)furthestDistance == 0 && collidedWith != null) {
				this.handleCollisionWith(collidedWith);
			}

		}

		
		//clamp velocity to moveX
		if(this instanceof Player)
		if(velocity.x > moveX && moveX >= 0 && facing == Entity.RIGHT) {
			velocity.x = moveX;
		} else if(velocity.x < -moveX && moveX <= 0 && facing == Entity.LEFT) {
			velocity.x = -moveX;
		}

	}
	
	public void handleCollisionWith(Entity e) {
		
	}
	
	public boolean isHere(int x, int y, int offset) {
		Rectangle coord = new Rectangle(x*16 + offset, y*16  + MainGame.guiHeight, 16, 16);
		return this.aabb.intersects(coord);
	}
	
	public void update(ArrayList<Entity> entities, Room map) {
		collisionCheck(entities, map);
		coordinates.add(velocity);
		if(facing == Entity.LEFT) {
			aabb.setX(coordinates.x + 10);
		}else if(facing == Entity.RIGHT) {
			aabb.setX(coordinates.x + 5);
		}
		aabb.setY(coordinates.y + 5);
	}
	
	public void render() {
		int x = (int)coordinates.x;
		int y = (int)coordinates.y;
		
		switch (state) {
		case Entity.MOVING : 
			if(facing == Entity.LEFT) {
				walkLeft.draw(x, y);
			} else if(facing == Entity.RIGHT) {
				walkRight.draw(x, y);
			}
		break;
		case Entity.IDLE :
			if(facing == Entity.LEFT) {
				idleLeft.draw(x, y);
			} else if(facing == Entity.RIGHT) {
				idleRight.draw(x, y);
			}
		break;
		case Entity.ATTACKING :
			if(facing == Entity.LEFT) {
				attackLeft.draw(x,y);
				if(attackLeft.getFrame() == attackLeft.getFrameCount() - 1) {
					attackRight.setCurrentFrame(0);
					this.setState(Entity.IDLE);
				}
			}else if(facing == Entity.RIGHT) {
				attackRight.draw(x,y);
				if(attackRight.getFrame() == attackRight.getFrameCount() - 1) {
					attackRight.setCurrentFrame(0);
					this.setState(Entity.IDLE);
				}

			}
		break;
		case Entity.HURT :
			if(facing == Entity.LEFT) {
				velocity.x = 0;
				hurtLeft.draw(x,y);
				if(hurtLeft.getFrame() == hurtLeft.getFrameCount() - 1) {
					hurtLeft.setCurrentFrame(0);
					this.setState(Entity.IDLE);
				}
			}else if(facing == Entity.RIGHT) {
				velocity.x  = 0;
				hurtRight.draw(x,y);
				if(hurtRight.getFrame() == hurtRight.getFrameCount() - 1) {
					hurtRight.setCurrentFrame(0);
					this.setState(Entity.IDLE);
				}

			}
		break;
		case Entity.DYING :
			if(facing == Entity.LEFT) {
				velocity.x = 0;
				dyingLeft.draw(x,y);
				if(dyingLeft.getFrame() == dyingLeft.getFrameCount() - 1) {
					dyingLeft.setCurrentFrame(0);
					this.setState(Entity.IDLE);
					this.dead = true;
				}
			}else if(facing == Entity.RIGHT) {
				velocity.x  = 0;
				dyingRight.draw(x,y);
				if(dyingRight.getFrame() == dyingRight.getFrameCount() - 1) {
					dyingRight.setCurrentFrame(0);
					this.setState(Entity.IDLE);
					this.dead = true;
				}

			}
		break;

		}
	}
		
}