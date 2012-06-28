package arc90.rv;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import java.util.ArrayList;

public class Shot extends Entity{
	
	Sound shock;

	public Shot(int x, int y, int facing) throws SlickException{
		
		Image walkSpriteSheet = new Image("/assets/static-ball.png",false,Image.FILTER_NEAREST);
				
		walkLeft = setupAnimation(walkSpriteSheet, 10, 10, true);
		walkRight = setupAnimation(walkSpriteSheet, 10, 10, false);

		shock = new Sound("/assets/sounds/zombieHit.wav");
		
		this.state = MOVING;
		this.facing = facing;
		
		this.coordinates.x = x;
		this.coordinates.y = y;
		
		aabb = new Rectangle(coordinates.x, coordinates.y, 10, 10);
		
	}
	
	public void collisionCheck(ArrayList<Entity> entities, Room map) {
		int moveX = 100;
		Entity collidedWithEntity = null;
		int endRow = 0;
		
		//find which vertical rows i'm in
		int topRow = ((int)aabb.getMinY() - MainGame.guiHeight) / 16;
		int bottomRow = ((int)aabb.getMaxY() - MainGame.guiHeight) / 16;
		
		//System.out.println(topRow);
		//System.out.println(bottomRow);
		
		for(int j = topRow; j <= bottomRow; j++) {
			float furthestDistance = 0;
			boolean collided = false;
			if(facing == Entity.LEFT) {
				for(int i = ((int)aabb.getMinX() - map.offsetLeft) / 16; i >= 0; i--) {
					for(Entity e : entities) {
						if(e.isHere(i, j, map.offsetLeft) && e != this) {
							furthestDistance = this.aabb.getMinX() - e.aabb.getMaxX();
							collided = true;
							collidedWithEntity = e;
							break;
						}
					}
					if(collided) {
						break;
					} else {
						furthestDistance += 16;
					}
				}
			} else if(facing == Entity.RIGHT) {
				for(int i = ((int)aabb.getMaxX() - map.offsetLeft) / 16; i <= map.tiles.length - 1; i++) {
					for(Entity e : entities) {
						if(e.isHere(i, j, map.offsetLeft) & e != this) {
							furthestDistance = e.aabb.getMinX() - this.aabb.getMaxX();
							collided = true;
							collidedWithEntity = e;
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
			
			if (furthestDistance == 0) {
				endRow = j;
			} else {
				endRow = bottomRow;
			}
						
			if(j == topRow || furthestDistance < moveX) {
				moveX = (int)furthestDistance;
			}
		}
		
		if(moveX == 0) {
			Entity e = collidedWithEntity;
			
			if(e != null && e instanceof Zombie) {
				shock.play(0.6f, 0.7f);
				if( ((Character)e).damage(20) ) {
					e.state = Entity.DYING;
				} 
			}
			
			if( e instanceof Zombie || e == null ) {
				entities.remove(this);
			}
			
			return;
		}

		
		//clamp velocity to moveX
		if(velocity.x > moveX && moveX >= 0) {
			velocity.x = moveX;
		} else if(velocity.x < -moveX && moveX <= 0) {
			velocity.x = -moveX;
		}

		
	}
	
	public void update(ArrayList<Entity> entities, Room map) {
		if(facing == RIGHT) {
			velocity.x = 3;
		} else if(facing == LEFT) {
			velocity.x = -3;
		}
		
		collisionCheck(entities, map);
		
		coordinates.add(velocity);
		
		aabb.setX(coordinates.x);
		aabb.setY(coordinates.y);
	}
	
}
