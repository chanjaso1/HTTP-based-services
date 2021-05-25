package nz.ac.wgtn.swen301.a3.server;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class TestDeleteLogs {
    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @Before
    public void init(){
        Persistency.DB.clear();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testValidDeletion(){
        //create events
        Persistency.DB.add(new LoggedEvent("d290f1ee-6c54-4b01-90e6-d701748f0851",
                "This is a message", "04-05-2021 10:12:00", "main",
                "logger #1", "ERROR", "This is an error!"));
        Persistency.DB.add(new LoggedEvent("dk2920-6cd4-cc23-21e6-s82910329221",
                "This is a message", "04-05-2021 09:10:03", "main",
                "logger #2", "INFO", "This is an informative event"));
        assert Persistency.DB.size() == 2;

        LogsServlet service = new LogsServlet();
        service.doDelete(request, response);

        assert Persistency.DB.isEmpty();
    }

    @Test
    public void testValidDeletion2(){
        assert Persistency.DB.isEmpty();

        LogsServlet service = new LogsServlet();
        service.doDelete(request, response);

        assert Persistency.DB.isEmpty();
    }
}
