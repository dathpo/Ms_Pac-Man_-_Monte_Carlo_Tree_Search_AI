package pacman.entries.pacman;

import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class RandomNR extends Controller<MOVE> {
	// Place your game logic here to play the game as Ms Pac-Man
	private Random random = new Random();

	public MOVE getMove(Game game, long timeDue) {
		MOVE[] nonReversingMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(),
				game.getPacmanLastMoveMade());
		return nonReversingMoves[random.nextInt(nonReversingMoves.length)];
	}
}
