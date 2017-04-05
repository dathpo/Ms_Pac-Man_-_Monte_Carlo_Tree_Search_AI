package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.List;

import static pacman.game.Constants.DM.PATH;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class Test extends Controller<MOVE> {

	private MOVE myMove = MOVE.NEUTRAL;
	private int index = 3;
	private boolean powerPillsCon = true;

	public MOVE getMove(Game game, long timeDue) {
		// Place your game logic here to play the game as Ms Pac-Man
		int PowerPills[] = game.getPowerPillIndices();

		if (game.getActivePowerPillsIndices().length > 0) {
			int PowerPill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(),
					game.getActivePowerPillsIndices(), PATH);

			myMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), PowerPill, PATH);
			Constants.GHOST closestGhost = findClosestGhost(game);
			int ghostLocation = game.getGhostCurrentNodeIndex(closestGhost);
			// System.out.println(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),ghostLocation));
			int shortestdis = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), ghostLocation);
			if (game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), PowerPill) == 1
					&& areAllGhostsOut(game) == false) {
				// System.out.println("Stalling");

				myMove = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), PowerPill, PATH);

			}

		} else {
			int closest = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(),
					game.getActivePillsIndices(), PATH);
			myMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closest, PATH);
		}

		Constants.GHOST closestGhost = findClosestGhost(game);
		if (game.isGhostEdible(closestGhost) == true) {
			myMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),
					game.getGhostCurrentNodeIndex(closestGhost), PATH);
		}

		return myMove;
	}

	public boolean areAllGhostsOut(Game g) {
		boolean out = true;
		if (g.getGhostLairTime(Constants.GHOST.BLINKY) != 0) {
			out = false;
		}
		if (g.getGhostLairTime(Constants.GHOST.PINKY) != 0) {
			out = false;
		}
		if (g.getGhostLairTime(Constants.GHOST.SUE) != 0) {
			out = false;
		}
		if (g.getGhostLairTime(Constants.GHOST.INKY) != 0) {
			out = false;
		}
		return out;

	}

	public Constants.GHOST findClosestGhost(Game g) {
		g.getPacmanCurrentNodeIndex();
		List<Integer> Ghosts = new ArrayList<>();
		Constants.GHOST list[] = { Constants.GHOST.INKY, Constants.GHOST.PINKY, Constants.GHOST.SUE,
				Constants.GHOST.BLINKY };
		Ghosts.add(g.getGhostCurrentNodeIndex(Constants.GHOST.INKY));//
		Ghosts.add(g.getGhostCurrentNodeIndex(Constants.GHOST.PINKY));
		Ghosts.add(g.getGhostCurrentNodeIndex(Constants.GHOST.SUE));
		Ghosts.add(g.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY));
		Constants.GHOST cloest;
		int shortest = g.getShortestPathDistance(g.getPacmanCurrentNodeIndex(),
				g.getGhostCurrentNodeIndex(Constants.GHOST.INKY));
		int index = 0, best = 0;
		for (int i : Ghosts) {

			if (g.getShortestPathDistance(g.getPacmanCurrentNodeIndex(), i) < shortest && i != -1) {
				shortest = g.getShortestPathDistance(g.getPacmanCurrentNodeIndex(), i);
				best = index;
			}
			index++;
		}

		return list[best];

	}
}