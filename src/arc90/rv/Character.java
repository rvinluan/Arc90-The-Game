package arc90.rv;

import org.newdawn.slick.Image;
import java.util.ArrayList;

public class Character extends Entity{

	public static final int MAX_VELOCITY = 2; //px
	
	protected int health;
		
	public Character() {	
	}
	
	public Character(Image walkSpriteSheet, Image idleSpriteSheet, Image attackSpriteSheet, Image hurtSpriteSheet) {
		super(walkSpriteSheet, idleSpriteSheet, attackSpriteSheet, hurtSpriteSheet);
		
		health = 98;
						
	}
	
	
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}

	public void stopMoving() {
		state = Entity.IDLE;
		velocity.x = 0;
	}
	
	public boolean damage(int damage) {
		return false;
	}
	
	public void update(ArrayList<Entity> entities, Room map) {
		
		collisionCheck(entities, map);

		coordinates.add(velocity);
						
		if(facing == Entity.LEFT) {
			aabb.setX(coordinates.x + 5);
		}else if(facing == Entity.RIGHT) {
			aabb.setX(coordinates.x + 5);
		}
		aabb.setY(coordinates.y + 5);
	}
	
	
}
