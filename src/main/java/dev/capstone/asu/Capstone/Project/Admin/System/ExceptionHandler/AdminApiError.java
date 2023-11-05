package dev.capstone.asu.Capstone.Project.Admin.System.ExceptionHandler;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminApiError {
    private HttpStatus status;
    private String timestamp;
    private String message;
    private String debugMessage;
    private List<AdminApiSubError> subErrors;

    public AdminApiError(HttpStatus status)
    {
        this.status = status;
        this.timestamp = Instant.now().toString();
    }

    public AdminApiError(HttpStatus status, Throwable ex)
    {
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("yyyy-MM-dd hh:mm:ss")
                        .withZone(ZoneId.from(ZoneOffset.UTC));
        this.status = status;
        this.timestamp = formatter.format(Instant.now());
        this.message = "Unexpected Error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public AdminApiError(HttpStatus status, String message, Throwable ex)
    {
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("yyyy-MM-dd hh:mm:ss")
                        .withZone(ZoneId.from(ZoneOffset.UTC));
        this.status = status;
        this.timestamp = formatter.format(Instant.now());
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public AdminApiError(HttpStatus status, Throwable ex, List<AdminApiSubError> subErrors)
    {
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("yyyy-MM-dd hh:mm:ss")
                        .withZone(ZoneId.from(ZoneOffset.UTC));
        this.status = status;
        this.timestamp = formatter.format(Instant.now());
        this.message = ex.getMessage();
        this.debugMessage = ex.getLocalizedMessage();
        this.subErrors = subErrors;
    }

    public AdminApiError(HttpStatus status, String message, List<AdminApiSubError> subErrors)
    {
        this.status = status;
        this.message = message;
        this.subErrors = subErrors;
    }

    public AdminApiError(HttpStatus status, String message, Throwable ex, List<AdminApiSubError> subErrors)
    {
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("yyyy-MM-dd hh:mm:ss")
                        .withZone(ZoneId.from(ZoneOffset.UTC));
        this.status = status;
        this.timestamp = formatter.format(Instant.now());
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
        this.subErrors = subErrors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public List<AdminApiSubError> getSubErrors() {
        return subErrors;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public void setSubErrors(List<AdminApiSubError> subErrors) {
        this.subErrors = subErrors;
    }
}
