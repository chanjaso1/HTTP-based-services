package nz.ac.wgtn.swen301.a3.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class StatsServlet extends HttpServlet {

    public StatsServlet() {
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HashMap<String, int[]> map = new HashMap<>();

        for(LoggedEvent loggedEvent : Persistency.DB){
            if(!map.containsKey(loggedEvent.getLogger())) map.put(loggedEvent.getLogger(), new int[8]);
            map.get(loggedEvent.getLogger())[LoggedEvent.levels.indexOf(loggedEvent.getLevel())] += 1;
        }
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("<html>  \n" +
                "<body>  \n" +
                "<table border>  ");




        out.println("<tr><th>logger</th><th>ALL</th><th>TRACE</th><th>DEBUG</th><th>INFO</th><th>WARN</th><th>ERROR</th><th>FATAL</th><th>OFF</th></tr>"); //create header
        for(String loggedEvent : map.keySet()){
            out.print("<tr>");
            out.print("<td>" + loggedEvent + "</td>");
            for(int i = 0; i < 8; i++){
                out.print("<td>"+ map.get(loggedEvent)[i] +"</td>");
            }
            out.println("</tr>");

        }
        out.println("</table>  \n" +
                "</body>  \n" +
                "</html>  ");
        out.close();
        response.setStatus(200);
    }
}
