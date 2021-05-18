package nz.ac.wgtn.swen301.a3.server;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class LogsServlet extends HttpServlet {


    public LogsServlet() {
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String levels = request.getParameter("level");
        String limit = request.getParameter("limit");

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
        LoggedEvent event = new Gson().fromJson(JSONString.toString(), LoggedEvent.class);

        Persistency.DB.add(event);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        Persistency.DB.clear();
    }
}
