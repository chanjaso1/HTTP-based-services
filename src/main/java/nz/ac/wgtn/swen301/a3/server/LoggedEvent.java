package nz.ac.wgtn.swen301.a3.server;

public class LoggedEvent {

    public String id, message, timestamp, thread, logger, errorDetails;

    public enum level {WARN, ERROR, DEBUG, INFO, ALL}

    public LoggedEvent(String id, String message, String timestamp, String thread, String logger, String errorDetails) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.thread = thread;
        this.logger = logger;
        this.errorDetails = errorDetails;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getThread() {
        return thread;
    }

    public String getLogger() {
        return logger;
    }

    public String getErrorDetails() {
        return errorDetails;
    }
}
