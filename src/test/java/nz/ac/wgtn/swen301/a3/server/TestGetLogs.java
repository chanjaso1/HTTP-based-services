package nz.ac.wgtn.swen301.a3.server;


import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGetLogs {
    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @Before
    public void init() {
        //reset request, response and db
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        Persistency.DB.clear();

        //create events
        Persistency.DB.add(new LoggedEvent("d290f1ee-6c54-4b01-90e6-d701748f0851",
                "This is a message", "04-05-2021 10:12:00", "main",
                "logger #1", "ERROR", "This is an error!"));
        Persistency.DB.add(new LoggedEvent("dk2920-6cd4-cc23-21e6-s82910329221",
                "This is a message", "04-05-2021 09:10:03", "main",
                "logger #2", "INFO", "This is an informative event"));
        Persistency.DB.add(new LoggedEvent("dk2210-6cd4-cc23-21e6-usn1029m22190",
                "This is a message", "01-07-2019 02:12:00", "main",
                "logger #3", "TRACE", "This is a trace event"));
        Persistency.DB.add(new LoggedEvent("da49-sc21-cc23-2121-usn1cv22hg0",
                "This is a message", "12-08-2008 12:12:12", "main",
                "logger #4", "DEBUG", "This is a DEBUG event"));
        Persistency.DB.add(new LoggedEvent("d149-se21-cc23-aa21-s9025934285",
                "This is a message", "06-10-2018 01:08:04", "main",
                "logger #5", "ERROR", "This is an ERROR event"));
        Persistency.DB.add(new LoggedEvent("d149-se21-cc23-aa21-m9297204812",
                "This is a message", "03-12-2014 01:45:02", "main",
                "logger #6", "WARN", "This is a WARN event"));
        Persistency.DB.add(new LoggedEvent("d149-se21-cc23-aa21-p980412032",
                "This is a message", "14-08-2012 10:54:20", "main",
                "logger #7", "FATAL", "This is a FATAL event"));
        Persistency.DB.add(new LoggedEvent("d126-se11-a223-bb12-x92023142",
                "This is a message", "14-09-2012 12:54:23", "main",
                "logger #8", "FATAL", "This is a FATAL event"));
    }

    @Test
    public void testInValidRequestCode1() throws IOException {

        //valid parameters
        request.setParameter("limit", "-1");
        request.setParameter("level", "ALL");

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInValidRequestCode2() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();

        //valid parameters
        request.setParameter("limit", "2");
        request.setParameter("level", "HIGH");

        MockHttpServletResponse response = new MockHttpServletResponse();
        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInValidRequestCode3() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();

        //valid parameters
        request.setParameter("limit", "2");
        request.setParameter("level", "HIGH");

        MockHttpServletResponse response = new MockHttpServletResponse();
        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        assertEquals(400, response.getStatus());
    }


    @Test
    public void testValidRequestCode() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();

        //valid parameters
        request.setParameter("limit", "2");
        request.setParameter("level", "ALL");

        MockHttpServletResponse response = new MockHttpServletResponse();
        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        assertEquals(200, response.getStatus());
    }

    @Test
    public void testValidRequestCode2() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();

        //valid parameters
        request.setParameter("limit", "99999999");
        request.setParameter("level", "OFF");

        MockHttpServletResponse response = new MockHttpServletResponse();
        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        assertEquals(200, response.getStatus());
    }

    @Test
    public void testValidContentType() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        //valid parameters
        request.setParameter("limit", "2");
        request.setParameter("level", "ALL");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        assertTrue(response.getContentType().startsWith("application/json"));
    }

    @Test
    public void testReturnedValues() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "3");
        request.setParameter("level", "ALL");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        String result = response.getContentAsString();
        String[] events = result.split("},\n");


        assertTrue(events[0].contains("d290f1ee-6c54-4b01-90e6-d701748f0851")); //logger 1
        assertTrue(events[1].contains("dk2920-6cd4-cc23-21e6-s82910329221")); //logger 2
        assertTrue(events[2].contains("dk2210-6cd4-cc23-21e6-usn1029m22190")); //logger 3
        assertEquals(3, events.length);
    }

    @Test
    public void testReturnedValues2() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "3");
        request.setParameter("level", "ERROR");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        String result = response.getContentAsString();
        String[] events = result.split("},\n");


        assertTrue(events[0].contains("d290f1ee-6c54-4b01-90e6-d701748f0851")); //logger 1
        assertTrue(events[1].contains("d149-se21-cc23-aa21-s9025934285")); //logger 5
        assertTrue(events[2].contains("d126-se11-a223-bb12-x92023142"));  //logger 8
        assertEquals(3, events.length);
    }
    @Test
    public void testReturnedValues3() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "10");
        request.setParameter("level", "FATAL");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        String result = response.getContentAsString();
        String[] events = result.split("},\n");

        assertTrue(events[0].contains("d126-se11-a223-bb12-x92023142"));  //logger 8
        assertTrue(events[1].contains("d149-se21-cc23-aa21-p980412032")); //logger 7

        assertEquals(2, events.length);
    }

    @Test
    public void testReturnedValues4() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "99");
        request.setParameter("level", "ALL");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        String result = response.getContentAsString();
        String[] events = result.split("},\n");


        assertTrue(events[0].contains("d290f1ee-6c54-4b01-90e6-d701748f0851")); //logger 1
        assertTrue(events[1].contains("dk2920-6cd4-cc23-21e6-s82910329221")); //logger 2
        assertTrue(events[2].contains("dk2210-6cd4-cc23-21e6-usn1029m22190")); //logger 3
        assertTrue(events[3].contains("d149-se21-cc23-aa21-s9025934285")); // logger 5
        assertTrue(events[4].contains("d149-se21-cc23-aa21-m9297204812")); // logger 6
        assertTrue(events[5].contains("d126-se11-a223-bb12-x92023142"));  //logger 8
        assertTrue(events[6].contains("d149-se21-cc23-aa21-p980412032")); //logger 7
        assertTrue(events[7].contains("da49-sc21-cc23-2121-usn1cv22hg0")); //logger 4

        assertEquals(8, events.length);
    }


    @Test
    public void testInvalidReturnedValues1() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "0");
        request.setParameter("level", "ALL");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        String result = response.getContentAsString();
        String[] events = result.split("},\n");

        assert result.contains("[]");
    }

}
