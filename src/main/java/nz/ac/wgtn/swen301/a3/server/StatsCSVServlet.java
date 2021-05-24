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
        HashMap<String, int[]> map = Persistency.mapCountOfLogs();

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=task3b.csv");

        PrintWriter out = response.getWriter();
        out.print("logger\tALL\tTRACE\tDEBUG\tINFO\tWARN\tERROR\tFATAL\tOFF\n"); //create header
        for (String loggedEvent : map.keySet()) {
            out.print(loggedEvent + "\t");
            for (int i = 0; i < 8; i++) {
                out.print(map.get(loggedEvent)[i] + "\t");
            }
            out.print("\n");

        }
        out.close();
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
