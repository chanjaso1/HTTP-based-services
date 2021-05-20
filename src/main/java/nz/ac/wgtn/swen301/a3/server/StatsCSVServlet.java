package nz.ac.wgtn.swen301.a3.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class StatsCSVServlet extends HttpServlet {

    public StatsCSVServlet() {
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HashMap<String, int[]> map = new HashMap<>();

        //TODO remove this for later!
        for(int i = 0; i < 50; i++){
            Persistency.randomizeEvent();
        }

        for(LoggedEvent loggedEvent : Persistency.DB){
            if(!map.containsKey(loggedEvent.getLogger())) map.put(loggedEvent.getLogger(), new int[8]);

            map.get(loggedEvent.getLogger())[LoggedEvent.levels.indexOf(loggedEvent.getLevel())] += 1;
        }

        response.setContentType("text/csv");
        PrintWriter out = response.getWriter();
        out.println("logger\tALL\tTRACE\tDEBUG\tINFO\tWARN\tERROR\tFATAL\tOFF"); //create header
        for(String loggedEvent : map.keySet()){
            out.print(loggedEvent+"\t");
            for(int i = 0; i < 8; i++){
                out.print(map.get(loggedEvent)[i]+"\t");
            }
            out.println("");

        }
        out.close();
        response.setStatus(200);
    }
}
