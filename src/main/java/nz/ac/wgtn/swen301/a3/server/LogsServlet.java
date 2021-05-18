package nz.ac.wgtn.swen301.a3.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

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
        System.out.println(l);
        System.out.println(level);
        if(!check(level, l)) response.setStatus(400);
        else {
            int limit = Integer.parseInt(l);

            List<LoggedEvent> results = filter(level);
            Collections.sort(results);

            if(limit < results.size()) results = results.subList(0, limit);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            out.println(gson.toJson(results));
            response.setStatus(200);
        }
        out.close();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        StringBuilder JSONString = new StringBuilder();
        try {
            BufferedReader reader = req.getReader();
            String line = "";
            while((line = reader.readLine()) != null)  JSONString.append(line);
        }catch (IOException ignored){}

        resp.setContentType("application/json");
        LoggedEvent event = gson.fromJson(JSONString.toString(), LoggedEvent.class);
        try {
            assert LoggedEvent.levels.contains(event.getLevel());
        }catch (AssertionError e) {
            e.printStackTrace();
            return;
        }
        Persistency.DB.add(event);
    }

    public List<LoggedEvent> filter(String level){
        List<LoggedEvent> results = new ArrayList<>();
        for (LoggedEvent event : Persistency.DB) {
            if(event.getLevel().equals(level) || level.equals("ALL")) results.add(event);
        }

        return results;

    }
    public boolean check(String level, String limit){
        try{

            assert LoggedEvent.levels.contains(level);
          //  if(LoggedEvent.levels.contains(level)) throw new AssertionError("Level: " + level + " is not a level");
            int lim = Integer.parseInt(limit);
            assert lim >= 0 && lim < Persistency.DB.size();

        } catch (AssertionError e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        Persistency.DB.clear();
    }
}
