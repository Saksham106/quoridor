
public class Colors {
    public static final String RESET = "\u001B[0m";
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";

    public static final String BLUE_BOLD = "\u001B[1;34m";
    public static final String RED_BOLD = "\u001B[1;31m";

    public static String player1(String text) {
        return BLUE_BOLD + text + RESET;
    }

    public static String player2(String text) {
        return RED_BOLD + text + RESET;
    }
}
