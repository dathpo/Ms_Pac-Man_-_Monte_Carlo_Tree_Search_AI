package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.controllers.examples.Legacy;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.Random;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class FixedRoJuMaxTi extends Controller<MOVE> {

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
//		while (System.currentTimeMillis() <= startTime + 40) {
        if (game.isJunction(game.getPacmanCurrentNodeIndex())) {
//			System.out.println(timeDue - startTime);
//			System.out.println(System.currentTimeMillis() - startTime + 40);
            for (MOVE move : moves) {
                long timePerMove = (timeDue);
//				System.out.println("ST - SCTM: " + (timePerMove));
//				System.out.println("SCTM - ST: " + (startTime + timePerMove));
//				while (System.currentTimeMillis() < startTime + timePerMove) {
                System.out.println(System.currentTimeMillis() - startTime);
                System.out.println(timePerMove);
                Game gameCopy = game.copy();
                for (int i = 0; i < numRollouts; i++) {
                    totalScore += rollout(gameCopy, move, timeDue);
                }
                System.out.println(move + ": Average: " + totalScore / numRollouts + ", Score: " + totalScore
                        + ", Rollouts: " + numRollouts);
                if (bestAverage < (totalScore / numRollouts)) {
                    bestAverage = (totalScore / numRollouts);
                    bestMove = move;
                }
                totalScore = 0;
            }
//			}
        } else {
            return getRandomNRMove(game);
        }
//		}
        moveTime = (int) (System.currentTimeMillis() - startTime);
        System.out.println("Best Average: " + bestAverage + ", Move: " + bestMove + ", Time: " + moveTime);
        System.out.println("Current Score: " + game.getScore());
        return bestMove;
    }

    private int rollout(Game game, MOVE move, long timeDue) {
        Game gameCopy = game.copy();
        gameCopy.advanceGame(move, ghosts.getMove(gameCopy, timeDue));
        while (!gameCopy.wasPacManEaten()) {
            gameCopy.advanceGame(getRandomNRMove(gameCopy), ghosts.getMove(gameCopy, timeDue));
        }
        return gameCopy.getScore();
    }

    private MOVE getRandomNRMove(Game game) {
        MOVE[] nRMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
        return nRMoves[rnd.nextInt(nRMoves.length)];
    }
}