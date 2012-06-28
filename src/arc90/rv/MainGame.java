package arc90.rv;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.*;
import java.util.*;

public class MainGame extends BasicGameState {
	
	Player rob;
	Zombie jim, jon;
	Room map;
	Image gui;
	ArrayList<Entity> entities;
	
	Image staticBar;
	Image healthBar;
	Image walkSheet;
	Image idleSheet;
	Image attackSheet;
	Image hurtSheet;
	
	Sound soundtrack;
		
	public static int guiHeight = 20;
	public static int scaleFactor = 3;
	
	int stateID = -1;
	
	boolean nextmap;
	boolean dead;
	int mapIndex;
	Room[] allMaps = Room.values();

    public MainGame( int stateID ) {
        this.stateID = stateID;
    }
    
    public int getID() {
    	return this.stateID;
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
	   	walkSheet = new Image("/assets/rob_walk.png", false, Image.FILTER_NEAREST);
	   	idleSheet = new Image("/assets/rob_idle.png", false, Image.FILTER_NEAREST);
	   	attackSheet = new Image("/assets/rob_attack.png", false, Image.FILTER_NEAREST);
	   	hurtSheet = new Image("/assets/rob_hurt.png", false, Image.FILTER_NEAREST);
	   	
   	 	gui = new Image("/assets/gui.png", false, Image.FILTER_NEAREST);
   	
   		staticBar = new Image("/assets/static-bar.gif", false, Image.FILTER_NEAREST);
   		healthBar = new Image("/assets/health-bar.gif", false, Image.FILTER_NEAREST);
   		
   		soundtrack = new Sound("/assets/sounds/EpsilonBMS6.wav");
   		soundtrack.loop(1, ArcGame.muted ? 0 : 0.6f);
    	
    	map = Room.ELEVATOR_BANK;
    	
    	rob = new Player(walkSheet, idleSheet, attackSheet, hurtSheet);
    	rob.setCoords(map.offsetLeft + 160, 32 + guiHeight);

    	entities = new ArrayList<Entity>();
    	entities.add(rob);
    	
    	try {
			for(int i = 0; i < map.numZombies; i++) {
				Zombie z = new Zombie(map.tiles.length*16 - 50*i, 35);
				entities.add(z);
			}
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	nextmap = false;
    	mapIndex = 0;
    	
    	dead = false;
    }
    
    public void enter(GameContainer container, StateBasedGame game) {
    	
    	rob.setCoords(map.offsetLeft, 32 + guiHeight);
    	
    	if(map == Room.ELEVATOR_BANK) {
    		rob.setCoords(map.offsetLeft + 160, 32 + guiHeight);
    	}
    	
    	entities = new ArrayList<Entity>();
    	entities.add(rob);
   	
    	try {
			for(int i = 0; i < map.numZombies; i++) {
				Zombie z = new Zombie(map.tiles.length*16 - 50*i, 35);
				entities.add(z);
			}
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	nextmap = false;

    }
    
    public void leave(GameContainer container, StateBasedGame sbg) {
    	if(dead) {
    		mapIndex = -1;
    		rob.setHealth(100);
    		entities.clear();
    		dead = false;
    	}
    	
    	mapIndex++;
    	
    	if(mapIndex >= allMaps.length) {
    		mapIndex = 0;
    	}
    	
    	map = allMaps[mapIndex];

    	rob.setCoords(map.offsetLeft, 32 + guiHeight);
    	if(map == Room.ELEVATOR_BANK) {
    		rob.setCoords(map.offsetLeft + 160, 32 + guiHeight);
    	}
    	rob.setElectricity(0);
    	rob.setState(Entity.IDLE);
    	
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {
 
    	for(int i = 0; i < entities.size(); i++) {
    		entities.get(i).update(entities, map);
    	}
    	
    	if(soundtrack.playing() && ArcGame.muted) {
    		soundtrack.stop();
    	} else if(!soundtrack.playing() && !ArcGame.muted) {
    		soundtrack.loop(1, 0.6f);
    	}
    	
    	if(nextmap) {
    		sbg.enterState(stateID, new FadeOutTransition(), new FadeInTransition());
    		nextmap = false;
    	}
    	
    	if(rob.getHealth() <= 0) {
    		//rob.setHealth(100);
    		dead = true;
    		sbg.enterState(2, new FadeOutTransition(), new FadeInTransition());
    	}
    	
     }
    
    @Override
    public void keyPressed(int key, char c){
    	if(key == Input.KEY_SPACE) {
    		if(rob.getState() != Character.ATTACKING) {
    			try {
					rob.attack(entities);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	} else if(key == Input.KEY_UP) {
    		if(rob.isHere(map.tiles.length, (int)(rob.coordinates.y - guiHeight) / 16, map.offsetLeft)) {
    			ArcGame.nextlevel.play(1, 1.6f);
    			nextmap = true;
    		}
    	} else if(key == Input.KEY_M) {
    		ArcGame.muted = !ArcGame.muted;
    	}
    }
    
    public void mousePressed(int button, int x, int y) {
    	if(rob.isHere((x - map.offsetLeft)/16, y/16, map.offsetLeft)) {
    		System.out.println("true");
    	} else {
    		System.out.println("nope");
    	}
    
    }
    
    public void keyReleased(int key, char c) {
    	switch(key) {
    	case Input.KEY_LEFT:
    		rob.stopMoving();
    	break;
    	case Input.KEY_RIGHT: 
    		rob.stopMoving();
    	break;
    	}
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException {
    	
    	int hbarposition = 45;
    	int sbarposition = 208;
    	
    	g.scale(scaleFactor, scaleFactor);
    	gui.draw();
    	
    	for(int i = 0; i < rob.getHealth(); i++) {
    		healthBar.draw(hbarposition + i, 7);
    	}
    	for(int i = 0; i < (int)rob.getElectricity(); i++) {
    		staticBar.draw(sbarposition + i, 7);
    	}
  	
    	
    	//map.render(0,guiHeight);
    	
    	map.bg.draw(map.offsetLeft, guiHeight);
    	
    	
    	for(int i = 0; i < entities.size(); i++) {
    		entities.get(i).render();
    		
    		if(entities.get(i) instanceof Shot) {
    		}
    		
    		//g.draw(entities.get(i).getAABB());
    	}
    	
    	if(map.fg != null) {
    		map.fg.draw(map.offsetLeft, guiHeight);
    	}
    	
    	g.setColor(Color.black);
    	g.fillRect(0, guiHeight, map.offsetLeft, 140);
   	
    }

}
