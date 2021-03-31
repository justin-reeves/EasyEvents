package io.keyword.easyevents.client;

import io.keyword.easyevents.Session;
import io.keyword.easyevents.util.EasyEventsIO;

import java.time.LocalTime;

/**
 * Class Description
 * <p>
 * Created By: Justin Reeves, Pasang Sherpa, Xiaotian Wang (Xander)
 * Created On: 3/26/2021
 */
class EasyEventsClient {

    public static void main(String[] args) {
        EasyEventsIO.displayIntro();
        String result = "";

        while (!result.startsWith("start")) {
            result = EasyEventsIO.prompt(
                    "Use 'start' command to begin event logging, or type 'help' for usage\n",
                    "start.*|help.*",
                    "Please type 'start' or 'help' with or without their respective OPTIONAL tags. See 'help' for details\n");

            if (result.startsWith("help")) {
                EasyEventsIO.displayUsage(result);
            }
        }

        Session session = null;

        // Split start command from any optional flags provided.
        // Flag idx's should be at ary[1] and possibly ary[3]
        // A flag's argument should be the index of ary[flagIdx + i]
        // EX: start -n name -t hh:mm:ss
        // ary[] = {"start", "n name", "t hh:mm:ss"}
        String[] startCommand = result.split("-");

        if (startCommand.length == 1) {
            session.getInstance();
        } else if (startCommand.length == 2) {
            if (startCommand[1].startsWith("n")) {
                session.getInstance(startCommand[1].substring(3));
            } else {
                session.getInstance(LocalTime.parse(startCommand[1].substring(3)));
            }
        } else {
            String name = startCommand[startCommand[1].startsWith("n") ? 1 : 2].substring(3);
            LocalTime time = LocalTime.parse(startCommand[startCommand[1].startsWith("t") ? 1 : 2].substring(3));

            session.getInstance(name, time);
        }

        session.start();
    }
}