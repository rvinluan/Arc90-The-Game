package arc90.rv;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;

public class ArcGame extends StateBasedGame {

	public static final int MAINGAMESTATE = 0;
    public static final int MENUSTATE     = 1;
    public static final int GAMEOVERSTATE = 2;
    
	public static int guiHeight = 20;
	public static int scaleFactor = 3;
	
	public static Sound nextlevel;
	public static boolean muted;
	 
    public ArcGame() {
        super("Arc90:The Game");
    }
 
    public static void main(String[] args) throws SlickException {
      try {
    	  muted = false;
    	  nextlevel = new Sound("/assets/sounds/nextlevel.wav");
    	      	  
	      AppGameContainer app = new AppGameContainer(new ArcGame());
	      app.setShowFPS(false);
	      app.setVSync(true);
	      app.setDisplayMode(320*scaleFactor, (120+guiHeight)*scaleFactor, false); 
	      app.start();
      } catch (SlickException e) {
    	  e.printStackTrace();
      }
    }
 
    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
    	this.addState(new MenuScreen(MENUSTATE));
        this.addState(new MainGame(MAINGAMESTATE));
        this.addState(new GameOverScreen(GAMEOVERSTATE));

    }

}
