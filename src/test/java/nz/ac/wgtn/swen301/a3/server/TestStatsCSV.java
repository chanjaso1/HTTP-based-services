package nz.ac.wgtn.swen301.a3.server;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestStatsCSV {
    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @Before
    public void init() {
        Persistency.DB.clear();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        //create events
        Persistency.DB.add(new LoggedEvent("d290f1ee-6c54-4b01-90e6-d701748f0851",
                "This is a message", "04-05-2021 10:12:00", "main",
                "logger #1", "ERROR", "This is an error!"));
        Persistency.DB.add(new LoggedEvent("b2515ee-g290-5k11-58a6-a238502823",
                "This is a message", "04-05-2011 10:11:00", "main",
                "logger #1", "ERROR", "This is an error!"));
        Persistency.DB.add(new LoggedEvent("xa12-w12s-j23122a-sv1222-j1243521223",
                "This is a message", "04-05-2021 10:01:00", "main",
                "logger #2", "INFO", "This is info!"));
    }

    @Test
    public void testValidCSVStatus() throws IOException {
        StatsCSVServlet service = new StatsCSVServlet();
        service.doGet(request, response);
        assert response.getStatus() == 200;
    }


    @Test
    public void testValidCSVHeader() throws IOException {
        StatsCSVServlet service = new StatsCSVServlet();
        service.doGet(request, response);
        assert response.getStatus() == 200;
        assert response.getContentAsString().startsWith("logger\tALL\tTRACE\tDEBUG\tINFO\tWARN\tERROR\tFATAL\tOFF\n");
    }

    @Test
    public void testValidCSV() throws IOException {
        StatsCSVServlet service = new StatsCSVServlet();
        service.doGet(request, response);
        assert response.getStatus() == 200;
        assert response.getContentAsString().startsWith("logger\tALL\tTRACE\tDEBUG\tINFO\tWARN\tERROR\tFATAL\tOFF\n");
        String[] lines = response.getContentAsString().split("\n");

        int countOfLogs = 0;
        for (int i = 1; i < lines.length; i++) {      //for every line in the content
            String[] line = lines[i].split("\t");
            for (int j = 1; j < line.length; j++) {   //for every token in the line
                assert Integer.parseInt(line[j]) >= 0;  //check that each count is not negative
                countOfLogs += Integer.parseInt(line[j]);
            }
        }
        assert Persistency.DB.size() == countOfLogs;
    }

    @Test
    public void testValidCSV2() throws IOException {
        StatsCSVServlet service = new StatsCSVServlet();
        Map<Integer, String> colHeader = new HashMap<>(
                Map.of(0, "logger", 1, "ALL", 2, "TRACE", 3, "DEBUG", 4, "INFO", 5, "WARN",
                        6, "ERROR", 7, "FATAL", 8, "OFF"));

        Persistency.DB.add(new LoggedEvent("xa1312-w12s-j23a22a-sv1222-j12436398223",
                "This is a message", "04-05-2000 10:01:00", "main",
                "logger #2", "TRACE", "This is a trace log!"));

        service.doGet(request, response);
        assert response.getStatus() == 200;
        assert response.getContentAsString().startsWith("logger\tALL\tTRACE\tDEBUG\tINFO\tWARN\tERROR\tFATAL\tOFF\n");
        String[] lines = response.getContentAsString().split("\n");

        int countOfLogs = 0;
        for (int i = 1; i < lines.length; i++) {      //for every line in the content
            String[] line = lines[i].split("\t");
            assert line[0].startsWith("logger #");
            for (int j = 1; j < line.length; j++) {   //for every token in the line
                assert Integer.parseInt(line[j]) >= 0;  //check that each count is not negative
                countOfLogs += Integer.parseInt(line[j]);
                if (Integer.parseInt(line[j]) == 1 && i == 1 && j == 4) assert colHeader.get(j).equals("INFO");
                else if (Integer.parseInt(line[j]) == 1 && i == 1 && j == 2) assert colHeader.get(j).equals("TRACE");
                else if (Integer.parseInt(line[j]) == 2 && i == 2) assert colHeader.get(j).equals("ERROR");
            }
        }
        assert Persistency.DB.size() == countOfLogs;
    }
}
