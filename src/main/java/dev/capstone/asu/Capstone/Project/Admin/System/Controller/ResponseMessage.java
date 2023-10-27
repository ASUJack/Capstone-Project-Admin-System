package dev.capstone.asu.Capstone.Project.Admin.System.Controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Data
public class ResponseMessage {

    private String timestamp;

    private String message;

    private Object entity;


    private ResponseMessage(String timestamp, String message, Object entity)
    {
        this.timestamp = timestamp;
        this.message = message;
        this.entity = entity;
    }

    public static ResponseMessage build(String message, Object entity)
    {
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("yyyy-MM-dd hh:mm:ss")
                        .withZone(ZoneId.from(ZoneOffset.UTC));
        return new ResponseMessage(
                formatter.format(Instant.now()),
                message,
                entity
        );
    }

    public static ResponseMessage build(String message)
    {
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("yyyy-MM-dd hh:mm:ss")
                        .withZone(ZoneId.from(ZoneOffset.UTC));
        return new ResponseMessage(
                formatter.format(Instant.now()),
                message,
                null
        );
    }

}
