import java.util.*;

/**
 * Main application entry point for the game collection.
 * Launches the game menu system and handles user input.
 */
public class App {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            GameMenu menu = new GameMenu(in);
            menu.runGameLoop();
        }
    }
}
