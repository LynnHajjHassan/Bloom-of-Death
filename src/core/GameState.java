package core;
import java.util.HashSet;
import java.util.Set;

/* GameState class
 * keeps track of the clues the player has found.
 *  You can add more clues as the player progresses */


public class GameState {
    private Set<String> clues;

    public GameState() {
        clues = new HashSet<>();
    }

    // Check if the player has found a clue
    public boolean hasFound(String clue) {
        return clues.contains(clue);
    }

    // Add a clue that the player has found
    public void addClue(String clue) {
        clues.add(clue);
    }
}
