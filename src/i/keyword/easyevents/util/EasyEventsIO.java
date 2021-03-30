package i.keyword.easyevents.util;

import java.util.*;

/**
 * Singleton Static Utility class that provides the IO functionality of EasyEvents
 * <p>
 * Created By: Justin Reeves, Pasang Sherpa, Xiaotian Wang (Xander)
 * Created On: 3/29/2021
 */
public class EasyEventsIO {

    // FIELDS


    // CONSTRUCTORS

    private EasyEventsIO() {
        // All static utility class. Prevent instantiation.
    }

    // BUSINESS METHODS

    /**
     * Displays, to the console, an Easy Events introduction statement
     *  consisting of helpful commands and example usage.
     */
    public static void displayIntro() {

    }

    /**
     * Displays, to the console, a detailed list of special input terms
     *  and their definitions.
     */
    public static void displayUsage() {

    }

    /**
     * Displays, to the console, the closing messages of Easy Events,
     *  including the name of the file containing the logged output data.
     * @param fileName  The name of the file containing logged output data
     *                      including extension, ex. "outputFile.txt"
     */
    public static void displayEnd(String fileName) {

    }

    /**
     * Waits for the user to enter input into the console and returns an array
     *  of Strings delimited by '-' flag indicator
     * @return Map<String> containing key, value pairs which are determined
     *          via splitting user input on the delimiter. The command value
     *          can be accessed via key = "cmd", and flags can be accessed via
     *          key = "[flag indicator]"
     *
     *          Ex: start -n Name Of Session -t 07:30:00
     *
     *          Key         Value
     *          -----       -----------------
     *          "cmd"       "start"
     *          "n"         "Name of Session"
     *          "t"         "07:30:00"
     *
     */
    public static Map<String, String> getUserCommandInput() {
        Scanner in= new Scanner(System.in);
        Map<String, String> input = new HashMap<>();

        return input;
    }

    /**
     * Waits for the user to enter input into the console and returns the
     *  entirety of their input.
     * @return String containing the users input as one String
     *
     */
    public static String getUserInput() {
        Scanner in= new Scanner(System.in);
        String input = "";

        while (in.hasNextLine()) {
            input = input.concat(in.nextLine() + "\n");
        }

        return input;
    }

    /**
     * Wrapper for System.out.print(str)
     */
    public static void print(String msg) {
        System.out.print(msg);
    }

    /**
     * Wrapper for System.out.println(str)
     */
    public static void println(String msg) {
        System.out.println(msg);
    }

    // ACCESSOR METHODS


    // HELPER METHODS


    // OVERRIDES

}