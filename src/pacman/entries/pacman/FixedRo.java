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
public class FixedRo extends Controller<MOVE> {

    private Legacy ghosts = new Legacy();
    private MOVE bestMove = MOVE.NEUTRAL;
    private Random rnd = new Random();
    private int numRollouts = 70;
    private double totalScore = 0;

    public MOVE getMove(Game game, long timeDue) {
        MOVE[] moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
        double bestAverage = 0;
        for (MOVE move : moves) {
            Game gameCopy = game.copy();
            for (int i = 0; i < numRollouts; i++) {
                totalScore += rollout(gameCopy, move, timeDue);
            }
            if (bestAverage < (totalScore / numRollouts)) {
                bestAverage = (totalScore / numRollouts);
                bestMove = move;
            }
            totalScore = 0;
        }
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