package io.keyword.easyevents.client;

import io.keyword.easyevents.EventConstructorInvalidInputException;
import io.keyword.easyevents.Session;
import io.keyword.easyevents.SessionFactory;
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
        Session session = null;
        while (session == null){
            try {
                String startCommand = EasyEventsIO.promptStart();
                session = SessionFactory.getSession(startCommand);
            }catch (EventConstructorInvalidInputException e){
                System.out.println(e.getMessage());
            }
        }

        session.execute();
    }

}