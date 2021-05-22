package nz.ac.wgtn.swen301.a3.server;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class StatsXLSServlet extends HttpServlet {

    public StatsXLSServlet() {
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=task3c.xls");

        //Writing to excel was based on: https://blog.knoldus.com/read-and-write-data-from-excel-sheet-using-apache-poi/
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet spreadsheet = workbook.createSheet("stats");

        Map<String, Object[]> stats = new TreeMap<>();
        stats.put("1", new Object[]{"logger", "ALL", "TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OFF"});

        HashMap<String, int[]> map = new HashMap<>();
        for (LoggedEvent loggedEvent : Persistency.DB) {
            if (!map.containsKey(loggedEvent.getLogger())) map.put(loggedEvent.getLogger(), new int[8]);

            map.get(loggedEvent.getLogger())[LoggedEvent.levels.indexOf(loggedEvent.getLevel())] += 1;


        }

        //get all data from database
        int row = 2;
        for (String loggedEvent : map.keySet()) {
            stats.put(String.valueOf(row), new Object[]{loggedEvent, map.get(loggedEvent)[0], map.get(loggedEvent)[1], map.get(loggedEvent)[2],
                    map.get(loggedEvent)[3], map.get(loggedEvent)[4], map.get(loggedEvent)[5], map.get(loggedEvent)[6], map.get(loggedEvent)[7]});
            row++;
        }

        row = 0;
        HSSFRow sheetRow;       //the current row in the stats sheet
        for (String key : stats.keySet()) {
            sheetRow = spreadsheet.createRow(row++);
            Object[] objects = stats.get(key);
            int sheetCell = 0;  //the current col in the stats sheet

            for (Object object : objects) {
                Cell cell = sheetRow.createCell(sheetCell++);
                cell.setCellValue(String.valueOf(object));     //assign the casted object to a cell
            }

        }

        workbook.write(out);
        out.close();
        response.setStatus(200);
    }
}
