package io.keyword.easyevents;

import java.time.LocalTime;

/**
 * Class Description
 * <p>
 * Created By: Justin
 * Created On: 3/31/2021
 */
public class SessionFactory {

    // FIELDS


    // CONSTRUCTORS
    private SessionFactory() {
    }

    // BUSINESS METHODS

    /**
     * This method parses a 'start' command string and returns a Session instance
     * with the desired settings based on flag usage
     *
     * @param startString The 'start' command to parse. Should contain 'start' with or without OPTIONAL
     *                    '-n' or '-t' flags and their respective values
     * @return Returns a Session with values set respective to flag usage.
     */
    public static Session getSession(String startString) throws EventConstructorInvalidInputException {
        boolean commandHasNameFlag = startString.contains(" -n ");
        boolean commandHasTimeFlag = startString.contains(" -t ");

        Session session;

        if (commandHasTimeFlag) {
            int timeIdx = startString.indexOf("-t") + 3; // + 3 moves idx to first "h" for a flag "-t hh:mm:ss"
            String timeString = startString.substring(timeIdx, timeIdx + "hh:mm:ss".length());
            LocalTime time = LocalTime.parse(timeString);

            session = Session.getInstance(time);
        } else {
            session = Session.getInstance();
        }

        if (commandHasNameFlag) {
            int timeFlagIdx = startString.indexOf("-t");
            int nameIdx = startString.indexOf("-n") + 3; // + 3 moves idx to first char in name for a flag "-n [name]"
            String name = startString.substring(
                    nameIdx,
                    nameIdx < timeFlagIdx ?
                            timeFlagIdx - 1 :
                            startString.length()).replace('"', ' ').trim();

            session.setSessionName(name);
        }

        return session;
    }

    // ACCESSOR METHODS


    // HELPER METHODS


    // OVERRIDES
}