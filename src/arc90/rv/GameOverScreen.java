package arc90.rv;

import org.newdawn.slick.Image;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.*;

public class GameOverScreen extends BasicGameState {

	int stateID = -1;
	Animation menu;
	int fd;
	boolean pressedAKey;
	
	public GameOverScreen(int id) {
		stateID = id;
	}
	
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		fd = 700;
		pressedAKey = false;
		
		menu = new Animation();
		menu.addFrame(new Image("/assets/gameover.png", false, Image.FILTER_NEAREST), fd);
		menu.addFrame(new Image("/assets/gameover.png", false, Image.FILTER_NEAREST), fd);
		
	}
	
	public void keyPressed(int key, char c) {
		pressedAKey = true;
	}
	
	public void enter(GameContainer container, StateBasedGame game) {
	}
	
	public void leave(GameContainer container, StateBasedGame game) {
		pressedAKey = false;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub
		g.scale(ArcGame.scaleFactor, ArcGame.scaleFactor);
		menu.draw(0,0);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		if(pressedAKey) {
			ArcGame.nextlevel.play(1, 2);
			game.enterState(1, new FadeOutTransition(), new FadeInTransition());
		}
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return stateID;
	}

}
