package nz.ac.wgtn.swen301.a3.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Persistency {

    public static List<LoggedEvent> DB = new ArrayList<>();

    public static HashMap<String, int[]> mapCountOfLogs(){
        HashMap<String, int[]> map = new HashMap<>();

        for (LoggedEvent loggedEvent : Persistency.DB) {
            if (!map.containsKey(loggedEvent.getLogger())) map.put(loggedEvent.getLogger(), new int[8]);
            map.get(loggedEvent.getLogger())[LoggedEvent.levels.indexOf(loggedEvent.getLevel())] += 1;
        }

        return map;
    }
}
