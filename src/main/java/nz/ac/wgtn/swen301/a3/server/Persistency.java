package nz.ac.wgtn.swen301.a3.server;

import java.util.ArrayList;
import java.util.List;

public class Persistency {

    public static List<LoggedEvent> DB = new ArrayList<>();

    public static void randomizeEvent(){
        String id  = java.util.UUID.randomUUID().toString();
        String level = LoggedEvent.levels.get((int) (Math.random() * LoggedEvent.levels.size()));

        Persistency.DB.add(new LoggedEvent(id,"random message","18-10-2021 16:47:18",
                "main", "Logger #" + (int) (Math.random() * 4), level, "some details"));
    }
}
