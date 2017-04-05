package pacman.entries.pacman;

import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.*;
import static pacman.game.Constants.DELAY;
import pacman.game.Constants.*;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class Monty extends Controller<MOVE> {

	private Legacy ghosts = new Legacy();
	private MOVE bestMove = MOVE.NEUTRAL;
	private Random rnd = new Random();
	private int numRollouts = 50;
	private int moveTime = 0;
	private double totalScore = 0;
	private long startTime = 0;

	public MOVE getMove(Game game, long timeDue) {
		startTime = System.currentTimeMillis();
		MOVE[] moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		double bestAverage = 0;
		if (game.isJunction(game.getPacmanCurrentNodeIndex()) ) {
			for (MOVE move : moves) {
				Game copy = game.copy();
				for (int i = 0; i < numRollouts; i++) {
					totalScore += rollout(copy, move);
				}
				System.out.println(move + ": Average: " + totalScore / numRollouts + ", Score: " + totalScore
						+ ", Rollouts: " + numRollouts);
				if (bestAverage < (totalScore / numRollouts)) {
					bestAverage = (totalScore / numRollouts);
					bestMove = move;
				}
				totalScore = 0;
			}
		} else {
			return getRandomMove(game);
		}
		moveTime = (int) (System.currentTimeMillis() - startTime);
		System.out.println("Best Average: " + bestAverage + ", Move: " + bestMove + ", Time: " + moveTime);
		return bestMove;
	}

	private int rollout(Game game, MOVE move) {
		Game copy = game.copy();
		copy.advanceGame(move, ghosts.getMove(copy, -1));
		while (!copy.wasPacManEaten()) {
			copy.advanceGame(getRandomMove(copy), ghosts.getMove(copy, -1));
		}
//		System.out.println("Score: " + copy.getScore());
		return copy.getScore();
	}
	
	private MOVE getRandomMove(Game game) {
		MOVE[] nRMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
	return nRMoves[rnd.nextInt(nRMoves.length)];
	}

//	copy.updatePacMan(moves[rnd.nextInt(moves.length)]);
//	copy.updateGhosts(ghosts.getMove(copy, System.currentTimeMillis() + DELAY));
//	copy.updateGame();
//	 }
	
//	 private int rollout(Game game) {
//	 Legacy ghosts = new Legacy();
//	 while(!game.wasPacManEaten()){
//	 MOVE[] moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(),
//	 game.getPacmanLastMoveMade());
//	 game.advanceGame(moves[rnd.nextInt(moves.length)], ghosts.getMove(game,
//	 System.currentTimeMillis() + DELAY));
//	 }
//	 return game.getScore();
//	 }

}