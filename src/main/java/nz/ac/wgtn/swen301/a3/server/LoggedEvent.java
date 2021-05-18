package nz.ac.wgtn.swen301.a3.server;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class LoggedEvent implements Comparable<LoggedEvent>{

    private String id, message, timestamp, thread, logger, level, errorDetails;


    @Override
    public int compareTo(LoggedEvent other) {
        return other.getTimestamp().compareTo(this.getTimestamp());
    }

    public static ArrayList<String> levels = new ArrayList<>(Arrays.asList("ALL", "TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OFF"));


    public LoggedEvent(String id, String message, String timestamp, String thread, String logger, String level, String errorDetails) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.thread = thread;
        this.logger = logger;
        this.errorDetails = errorDetails;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTimestamp() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date parsedDate = dateFormat.parse(timestamp);
            return new Timestamp(parsedDate.getTime());
        }catch(Exception e){
            return null;
        }
    }

    public String getThread() {
        return thread;
    }

    public String getLogger() {
        return logger;
    }

    public String getLevel() { return level; }

    public String getErrorDetails() {
        return errorDetails;
    }
}
