package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.controllers.examples.Legacy;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.Random;

import static pacman.game.Constants.DELAY;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class FixedRoJuTest extends Controller<MOVE> {

    private MOVE bestMove = MOVE.NEUTRAL;
    private Random rnd = new Random();
    private int numRollouts = 5000;
    private int moveTime = 0;
    private double totalScore = 0;
    private long startTime = 0;

    public MOVE getMove(Game game, long timeDue) {
        startTime = System.currentTimeMillis();
        MOVE[] moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
        double bestAverage = 0;
        if (game.isJunction(game.getPacmanCurrentNodeIndex())) {
            for (MOVE move : moves) {
                for (int i = 0; i < numRollouts; i++) {
                    totalScore += rollout(game, moves);
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
            MOVE[] nonReversingMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(),
                    game.getPacmanLastMoveMade());
            return nonReversingMoves[rnd.nextInt(nonReversingMoves.length)];
        }
        moveTime = (int) (System.currentTimeMillis() - startTime);
        System.out.println("Best Average: " + bestAverage + ", Move: " + bestMove + ", Time: " + moveTime);
        return bestMove;
    }

    private int rollout(Game game, MOVE[] moves) {
        Legacy ghosts = new Legacy();
        game.advanceGame(moves[rnd.nextInt(moves.length)], ghosts.getMove(game, System.currentTimeMillis() + DELAY));
        return game.getScore();
    }
}