package nz.ac.wgtn.swen301.a3.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LogsServlet extends HttpServlet {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public LogsServlet() {
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String l = request.getParameter("limit");
        String level = request.getParameter("level");
        if (!check(level, l) ) response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        else {
            int limit = Integer.parseInt(l);
            List<LoggedEvent> results = filter(level);
            Collections.sort(results);

            if (limit < results.size()) results = results.subList(0, limit);

            out.println(gson.toJson(results));

            response.setStatus(HttpServletResponse.SC_OK);
        }
        out.close();
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        StringBuilder JSONString = new StringBuilder();
        try {
            BufferedReader reader = request.getReader();

            String line;
            while (  (line = reader.readLine())   != null) JSONString.append(line);

            response.setContentType("application/json");
            LoggedEvent event = gson.fromJson(JSONString.toString(), LoggedEvent.class);

            assert event != null;
            assert LoggedEvent.levels.contains(event.getLevel());
            for(LoggedEvent loggedEvent : Persistency.DB){
                if(loggedEvent.getId().equals(event.getId())){
                    response.setStatus(HttpServletResponse.SC_CONFLICT);        //duplicate id
                    return;
                }
            }

            Persistency.DB.add(event);
            response.setStatus(HttpServletResponse.SC_CREATED);                //valid object
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);                //invalid object
        }
    }

    public List<LoggedEvent> filter(String level) {
        List<String> chosenLevels = LoggedEvent.levels.stream().filter(e -> LoggedEvent.levels.indexOf(e) >= LoggedEvent.levels.indexOf(level)).collect(Collectors.toList());
        List<LoggedEvent> results = new ArrayList<>();
        for (LoggedEvent event : Persistency.DB) {
            if (chosenLevels.contains(event.getLevel())) results.add(event);
        }
        return results;

    }

    public boolean check(String level, String limit) {
        try {
            assert LoggedEvent.levels.contains(level);
            int lim = Integer.parseInt(limit);
            assert lim >= 0;

        } catch (AssertionError e) {
            return false;
        }
        return true;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        Persistency.DB.clear();
    }
}
