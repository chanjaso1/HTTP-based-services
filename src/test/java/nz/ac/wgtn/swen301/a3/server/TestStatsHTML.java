package nz.ac.wgtn.swen301.a3.server;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestStatsHTML {
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
    }

    @Test
    public void testReadableHTML() throws IOException {
        StatsServlet service = new StatsServlet();
        service.doGet(request, response);
        assert response.getStatus() == 200;
        Document document = Jsoup.parse(response.getContentAsString());

        Element table = document.selectFirst("table");
        Iterator<Element> row = table.select("tr").iterator();

        Iterator<Element> col = row.next().select("th").iterator();
        assert col.next().text().equals("logger");
        assert col.next().text().equals("ALL");
        assert col.next().text().equals("TRACE");
        assert col.next().text().equals("DEBUG");
        assert col.next().text().equals("INFO");
        assert col.next().text().equals("WARN");
        assert col.next().text().equals("ERROR");
        assert col.next().text().equals("FATAL");
        assert col.next().text().equals("OFF");
    }


    @Test
    public void testReadableHTML2() throws IOException {
        StatsServlet service = new StatsServlet();
        service.doGet(request, response);
        assert response.getStatus() == 200;


        Document document = Jsoup.parse(response.getContentAsString());
        Element table = document.selectFirst("table");
        Iterator<Element> row = table.select("tr").iterator();
        row.next();

        while (row.hasNext()) {
            Iterator<Element> col = row.next().select("td").iterator();
            assert col.next().text().equals("logger #1");
            int colPosition = 0;

            while (col.hasNext()) {
                colPosition++;
                if (colPosition == 6) assert col.next().text().equals("1");
                else assert col.next().text().equals("0");
            }
        }
    }

    @Test
    public void testReadableHTML3() throws IOException {
        StatsServlet service = new StatsServlet();
        Map<Integer, String> colHeader = new HashMap<>(
                Map.of(0, "logger", 1, "ALL", 2, "TRACE", 3, "DEBUG", 4, "INFO", 5, "WARN",
                        6, "ERROR", 7, "FATAL", 8, "OFF"));


        Persistency.DB.add(new LoggedEvent("b2515ee-g290-5k11-58a6-a238502823",
                "This is a message", "04-05-2011 10:11:00", "main",
                "logger #1", "ERROR", "This is an error!"));

        Persistency.DB.add(new LoggedEvent("xa12-w12s-j23122a-sv1222-j1243521223",
                "This is a message", "04-05-2021 10:01:00", "main",
                "logger #2", "INFO", "This is info!"));

        service.doGet(request, response);
        assert response.getStatus() == 200;

        Document document = Jsoup.parse(response.getContentAsString());
        Element table = document.selectFirst("table");
        Iterator<Element> row = table.select("tr").iterator();

        row.next(); //skip headers
        int rowIndex = 0;
        while (row.hasNext()) {
            Iterator<Element> col = row.next().select("td").iterator();
            String logger = col.next().text();
            assert logger.startsWith("logger #");
            Integer colPosition = 1;
            while (col.hasNext()) {
                String currentCol = col.next().text();
                if (currentCol.equals("1")) assert colHeader.get(colPosition).equals("INFO");
                else if (currentCol.equals("2")) assert colHeader.get(colPosition).equals("ERROR");
                else assert currentCol.equals("0");
                colPosition++;
            }
            rowIndex++;
        }
        assert rowIndex == 2; //there should only be two entries, logger #1 and #2.
    }
}

