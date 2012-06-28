package arc90.rv;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public enum Room {
	ELEVATOR_BANK ("/assets/hallwaytest.png", null, 20, 2),
	KITCHEN ("/assets/kitchen_bg.png", "/assets/kitchen_fg.png", 14, 2),
	BOARD_ROOM ("/assets/board_bg.png", "/assets/board_fg.png", 14, 1),
	KINDLING ("/assets/kindling.png", null, 16, 3),
	CENTER ("/assets/center.png", null, 18, 3),
	READABILITY ("/assets/Readability.png", null, 15, 2),
	INSIGHT ("/assets/Insight_bg.png", "/assets/Insight_fg.png", 20, 3),
	LOBBY("/assets/lobby.png", null, 16, 1);
	
	public Image bg;
	public Image fg;
	public int[][] tiles;
	public int offsetLeft;
	
	public int numZombies;
	
	Room(String bgfilename, String fgfilename, int width, int numZombies) {
			try {
				bg = new Image(bgfilename, false, Image.FILTER_NEAREST);
				if(fgfilename != null) {
					fg = new Image(fgfilename, false, Image.FILTER_NEAREST);
				} else {
					fg = null;
				}
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			tiles = new int[width][5];
			this.numZombies = numZombies;
			
			offsetLeft = ((20-width) / 2) * 16;
			
			for(int i = 0; i < tiles.length; i++) {
				for(int j = 0; j < tiles[i].length; j++) {
					tiles[i][j] = 0;
				}
			}
			
	}
	
	public void render(int x, int y) {
		bg.draw(x, y);
		if(fg != null) {
			fg.draw(x, y);
		}
	}

}
