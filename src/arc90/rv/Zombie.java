package arc90.rv;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class Zombie extends Character{
	
	public float MAX_VELOCITY = 0.2f;
	public int FRAME_DURATION = 200;
	
	public Zombie(int x, int  y) throws SlickException {
		super();

		Image walkSpriteSheet = new Image("/assets/zombie_walk.png",false,Image.FILTER_NEAREST);
		Image idleSpriteSheet = new Image("/assets/zombie_idle.png",false,Image.FILTER_NEAREST);
		Image attackSpriteSheet = new Image("/assets/zombie_idle.png",false,Image.FILTER_NEAREST);
		Image hurtSpriteSheet = new Image("/assets/zombie_hurt.png",false,Image.FILTER_NEAREST);
		Image dyingSpriteSheet = new Image("/assets/zombie_death.png",false,Image.FILTER_NEAREST);
		
		
		walkLeft = setupAnimation(walkSpriteSheet, 32, 64, true);
		walkRight = setupAnimation(walkSpriteSheet, 32, 64, false);
		idleLeft = setupAnimation(idleSpriteSheet, 32, 64, true);
		idleRight = setupAnimation(idleSpriteSheet, 32, 64, false);
		attackLeft = setupAnimation(attackSpriteSheet, 32, 64, true);
		attackRight = setupAnimation(attackSpriteSheet, 32, 64, false);
		hurtLeft = setupAnimation(hurtSpriteSheet, 32, 64, true);
		hurtRight = setupAnimation(hurtSpriteSheet, 32, 64, false);
		dyingLeft = setupAnimation(dyingSpriteSheet, 32, 64, true, 75);
		dyingRight = setupAnimation(dyingSpriteSheet, 32, 64, false, 75);
		
		
		this.coordinates.x = x + 15;
		this.coordinates.y = y + 15;
		aabb = new Rectangle(coordinates.x, coordinates.y, 16, 32);
		
		this.facing = RIGHT;
		
		this.health = 100;
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
	
	public boolean damage(int damage) {
		this.health -= damage;
		this.state = HURT;
		return this.health <= 0;
	}
	
	public void handleCollisionWith(Entity e) {
		if(e instanceof Player) {
		}
	}


	public void update(ArrayList<Entity> entities, Room map) {
		if(state == Entity.IDLE || state == Entity.MOVING) {
			velocity.x = -1;
		}
		
		//System.out.println(state);

		if(velocity.x > 0) {
			state = Entity.MOVING;
			facing = Entity.RIGHT;
		} else if(velocity.x < 0) {
			state = Entity.MOVING;
			facing = Entity.LEFT;
		}
		
		if(velocity.x >= MAX_VELOCITY) {
			velocity.x = MAX_VELOCITY;
		}
		
		if(velocity.x <= -(MAX_VELOCITY)) {
			velocity.x = -(MAX_VELOCITY);
		}

		collisionCheck(entities, map);
		
		coordinates.add(velocity);
		
		if(facing == Entity.LEFT) {
			aabb.setX(coordinates.x + 10);
		}else if(facing == Entity.RIGHT) {
			aabb.setX(coordinates.x + 5);
		}
		aabb.setY(coordinates.y + 5);
		
		if(dead) {
			entities.remove(this);
		}
	}
	
}
