package com.joyjoin.eventservice.exception;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EventRegistrationNotFoundException extends RuntimeException {

    @Getter
    private Map<String, String> fields;
    @Getter @Setter
    private List<String> details;

    public EventRegistrationNotFoundException(String resourceName, Map<String, String> fields, List<String> details) {
        super(generateMessage(resourceName, fields));
        this.fields = fields;
        this.details = details;
    }

    private static String generateMessage(String resourceName, Map<String, String> fields) {
        StringBuilder message = new StringBuilder(resourceName + " not found with ");
        fields.forEach((key, value) -> message.append(key + ": '" + value + "', "));
        // Remove the last comma and space
        message.setLength(message.length() - 2);
        return message.toString();
    }
}
