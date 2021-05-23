package nz.ac.wgtn.swen301.a3.server;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;

public class TestPostLogs {
    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @Before
    public void init() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        Persistency.DB.clear();
    }

    @Test
    public void testValidAddEvent1() {

        String event = "{id='d290f1ee-6c54-4b01-90e6-d701748f0851', message='String message', " +
                "timestamp='18-10-2021 16:47:18', thread='a', logger='mainClass', level='INFO', " +
                "errorDetails='error details'}";

        request.setContentType("application/json");
        request.setContent(event.getBytes());

        LogsServlet service = new LogsServlet();
        service.doPost(request, response);

        assertEquals(201, response.getStatus());
        assert Persistency.DB.size() == 1;

    }

    @Test
    public void testValidAddEvent2() {
        String event = "{id='d290f1ee-6c54-4b01-90e6-d701748f0851', message='String message', " +
                "timestamp='18-10-2021 16:47:18', thread='main', logger='mainClass', level='ALL', " +
                "errorDetails='error details'}";

        String secondEvent = "{id='a92943ee-6254-4s01-90cc-d7092931851', message='String message', " +
                "timestamp='13-11-2023 11:32:00', thread='main', logger='mainClass', level='FATAL', " +
                "errorDetails='error details'}";

        request.setContentType("application/json");
        request.setContent(event.getBytes());

        LogsServlet service = new LogsServlet();

        service.doPost(request, response);
        assertEquals(201, response.getStatus());

        request.setContentType("application/json");
        request.setContent(secondEvent.getBytes());

        service.doPost(request, response);
        assertEquals(201, response.getStatus());

        assert Persistency.DB.size() == 2;
    }

    @Test
    public void testInvalidAddEvent1() {
        String event = "{id='d290f1ee-6c54-4b01-90e6-d701748f0851', message='String message', " +
                "timestamp='18-10-2021 16:47:18', thread='a', logger='mainClass', level='OFF', " +
                "errorDetails='error details', timeTypeLong=1609127238}";

        request.setContentType("application/json");
        request.setContent(event.getBytes());

        LogsServlet service = new LogsServlet();

        service.doPost(request, response);
        assertEquals(201, response.getStatus());

        request.setContentType("application/json");
        request.setContent(event.getBytes());

        service.doPost(request, response);
        assertEquals(409, response.getStatus());
    }

    @Test
    public void testInvalidAddEvent2() {
        String event = "Object";

        request.setContentType("application/json");
        request.setContent(event.getBytes());

        LogsServlet service = new LogsServlet();
        service.doPost(request, response);

        assertEquals(400, response.getStatus());
    }
}
