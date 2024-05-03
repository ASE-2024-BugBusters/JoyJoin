//package com.joyjoin.eventservice.exception;
//
//import lombok.Getter;
//
//import java.util.UUID;
//
//public class DuplicateRegistrationException extends RuntimeException {
//    @Getter
//    private UUID eventId;
//    @Getter
//    private UUID userId;
//
//    public DuplicateRegistrationException(String message, UUID eventId, UUID userId) {
//        super(message + " - EventId: " + eventId.toString() + ", UserId: " + userId.toString());
//        this.eventId = eventId;
//        this.userId = userId;
//    }
//
//}
package com.joyjoin.eventservice.exception;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;
import java.util.Map;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateRegistrationException extends RuntimeException {

    @Getter
    private Map<String, String> fields;
    @Getter @Setter
    private List<String> details;

    public DuplicateRegistrationException(String resourceName, Map<String, String> fields, List<String> details) {
        super(generateMessage(resourceName, fields));
        this.fields = fields;
        this.details = details;
    }

    private static String generateMessage(String resourceName, Map<String, String> fields) {
        StringBuilder message = new StringBuilder(resourceName + " duplicate with ");
        fields.forEach((key, value) -> message.append(key + ": '" + value + "', "));
        // Remove the last comma and space
        message.setLength(message.length() - 2);
        return message.toString();
    }
}