package nz.ac.wgtn.swen301.a3.server;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class TestStatsXLS {

    MockHttpServletRequest request;
    MockHttpServletResponse response;
    ArrayList<String> headers = new ArrayList<>(Arrays.asList("logger", "ALL", "TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OFF"));

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
    public void testValidXLSSheet() throws IOException {
        StatsXLSServlet service = new StatsXLSServlet();
        service.doGet(request, response);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getContentAsByteArray());

        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        assert workbook.getSheetAt(0).getSheetName().equals("stats");
    }

    @Test
    public void testValidXLSHeaders() throws IOException {
        StatsXLSServlet service = new StatsXLSServlet();

        service.doGet(request, response);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getContentAsByteArray());
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        assert workbook.getSheetAt(0).getSheetName().equals("stats");

        Iterator<Row> rowIterator = workbook.getSheetAt(0).iterator();
        Iterator<Cell> cellIterator;

        Cell cell;
        cellIterator = rowIterator.next().iterator();
        int index = 0;
        while (cellIterator.hasNext()) {
            cell = cellIterator.next();
            assert cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(headers.get(index));
            index++;
        }
    }

    @Test
    public void testValidXLS() throws IOException {
        StatsXLSServlet service = new StatsXLSServlet();

        service.doGet(request, response);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getContentAsByteArray());
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        assert workbook.getSheetAt(0).getSheetName().equals("stats");
        int countOfLogs = 0;

        Iterator<Row> rowIterator = workbook.getSheetAt(0).iterator();
        Iterator<Cell> cellIterator;

        Row row;
        int index = 0;
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            Cell cell;
            cellIterator = row.iterator();
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                if (row.getRowNum() == 0) {   //check for valid headers
                    assert cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(headers.get(index));
                } else if (row.getRowNum() >= 1 && cell.getColumnIndex() >= 1) {
                    assert cell.getCellType() == CellType.STRING;
                    assert Integer.parseInt(cell.getStringCellValue()) >= 0;

                    countOfLogs += Integer.parseInt(cell.getStringCellValue());
                } else if (cell.getColumnIndex() == 0) {
                    assert cell.getCellType() == CellType.STRING && cell.getStringCellValue().startsWith("logger #");
                }
                index++;
            }
        }
        assert countOfLogs == Persistency.DB.size();
    }

    @Test
    public void testValidXLS2() throws IOException {
        StatsXLSServlet service = new StatsXLSServlet();
        ArrayList<String> headers = new ArrayList<>(Arrays.asList("logger", "ALL", "TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OFF"));

        Persistency.DB.add(new LoggedEvent("b2515ee-g290-5k11-58a6-a238502823",
                "This is a message", "04-05-2011 10:11:00", "main",
                "logger #1", "ERROR", "This is an error!"));
        Persistency.DB.add(new LoggedEvent("xa1312-w12s-j23a22a-s0022-j009215957",
                "This is a message", "04-05-2000 10:01:00", "main",
                "logger #2", "TRACE", "This is a trace log!"));
        service.doGet(request, response);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getContentAsByteArray());
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        assert workbook.getSheetAt(0).getSheetName().equals("stats");
        int countOfLogs = 0;

        Iterator<Row> rowIterator = workbook.getSheetAt(0).iterator();
        Iterator<Cell> cellIterator;

        Row row;

        int index = 0, rowCount = 0, firstLoggerSum = 0, secondloggerSum = 0;

        while (rowIterator.hasNext()) {
            rowCount++;
            row = rowIterator.next();
            Cell cell;
            cellIterator = row.iterator();
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                if (row.getRowNum() == 0) {   //check for valid headers
                    assert cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(headers.get(index));
                } else if (row.getRowNum() >= 1 && cell.getColumnIndex() >= 1) {
                    int count = Integer.parseInt(cell.getStringCellValue());
                    assert cell.getCellType() == CellType.STRING;
                    assert count >= 0;

                    if (row.getRowNum() == 1) {
                        firstLoggerSum += count;
                    } else if (row.getRowNum() == 2) {
                        secondloggerSum += count;
                    }
                    if (count > 0) {
                        assert headers.get(cell.getColumnIndex()).equals("TRACE") || headers.get(cell.getColumnIndex()).equals("ERROR");
                    }
                    countOfLogs += count;
                } else if (cell.getColumnIndex() == 0) {
                    assert cell.getCellType() == CellType.STRING && cell.getStringCellValue().startsWith("logger #");
                }
                index++;
            }
        }
        assert countOfLogs == Persistency.DB.size() && firstLoggerSum + secondloggerSum == countOfLogs;
        assert rowCount == 3;
    }
}
