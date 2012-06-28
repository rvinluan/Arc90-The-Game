package arc90.rv;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Player extends Character {
	
	public final int MAX_VELOCITY = 2;
	
	private float electricity;
	private Input i;
	
	public Sound woosh;
	public Sound fail;
	
	public Player(Image walkSpriteSheet, Image idleSpriteSheet, Image attackSpriteSheet, Image hurtSpriteSheet) {
		super(walkSpriteSheet, idleSpriteSheet, attackSpriteSheet, hurtSpriteSheet);
		
		electricity = 0;
		health = 98;
		
		i = new Input((80+MainGame.guiHeight)*MainGame.scaleFactor);
		
		try {
			woosh = new Sound("/assets/sounds/woosh.wav");
			fail = new Sound("/assets/sounds/fail.wav");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	public float getElectricity() {
		return electricity;
	}

	public void setElectricity(float electricity) {
		this.electricity = electricity;
	}
	
	public void attack(ArrayList<Entity> entities) throws SlickException{
		if(electricity < 15) {
			fail.play(1, 0.1f);
			return;
		}
		
		woosh.play(1, 0.7f);
				
		state = Entity.ATTACKING;
		electricity -= 15;
		
		int offset = 21;
		
		if(facing == LEFT) {
			offset = -5;
		}
		
		Shot s;
		s = new Shot((int)coordinates.x + offset, (int)coordinates.y + 20, facing);
		
		
		if(entities.add(s)) {
			//System.out.println("definitely created one");
		}

	}
	
	public boolean damage(int damage) {
		this.health -= damage;
		return this.health <= 0;
	}
	
	public void handleCollisionWith(Entity e) {
		if(e instanceof Zombie) {
			if(e.dead || e.state == Entity.DYING) {
				return;
			}
			
			this.velocity.x = -15;
			this.state = Entity.HURT;
			this.damage(5);
		}
	}
	
	public void update(ArrayList<Entity> entities, Room map) {
		if(state == Entity.HURT || state == Entity.ATTACKING) {
			super.update(entities, map);
			return;
		}
		
		if(i.isKeyDown(Input.KEY_LEFT)) {
			facing = Entity.LEFT;
			state = Entity.MOVING;
			velocity.x -= 0.1;
		}
		if(i.isKeyDown(Input.KEY_RIGHT)) {
			facing = Entity.RIGHT;
			state = Entity.MOVING;
			velocity.x += 0.1;
		}
		if(i.isKeyDown(Input.KEY_SPACE)) {
			if(state == Entity.IDLE) {
				try {
					attack(entities);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if(velocity.x >= MAX_VELOCITY) {
			velocity.x = MAX_VELOCITY;
		}
		
		if(velocity.x <= -(MAX_VELOCITY)) {
			velocity.x = -(MAX_VELOCITY);
		}
		
		if(electricity < 0) {
			electricity = 0;
		}
		if(electricity > 97) {
			electricity = 97;
		}
		
		if(velocity.x != 0) {
			electricity += 0.3;
		}

		super.update(entities, map);
	}
}
