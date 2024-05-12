# Event Service Microservice

## Overview
The Event Service microservice is designed to manage all aspects of event handling within our application. This includes the creation, modification, retrieval, deletion, registration, and rating of events.

## Components

### Event Controller
The Event Controller handles all incoming HTTP requests related to events, including operations for creating, updating, retrieving, and deleting event data, as well as managing event registrations. All endpoints are well-documented with JavaDocs.

### Models
#### Event
Represents an event, serving as the primary entity.
#### Event Registration
Manages event registration data. Utilizes a primary key of `registrationId`, with foreign keys for `eventId` and `userId`. It interfaces with the EventRegistrationRepository for database interactions.
#### Event Registration Count
Facilitates quick updates and checks on event participation counts, interacting with the EventRegistrationCountRepository.
#### Image, ImageRef, ImageUrl
These models manage various aspects of image handling within events:
- **ImageRef** is used in the event entity and includes fields for the bucket and key.
- **ImageUrl** is generated in the EventDto for easy image retrieval and storage.
#### Rating
Handles rating data for events and is connected to the EventRatingRepository.
#### Location, Tag
Represent event location details and tags.

### Converters
#### ImageRefListConverter
Converts a list of ImageRef entities to and from database columns.
#### TagsConverter
Converts a list of Tag entities to and from database columns.

### Model DTOs (Data Transfer Objects)
#### EventDto
Transfers event data between the server and clients.
#### EventRegistrationDto
Transfers event registration data between the server and clients.
#### LocationDto
Details location information within an EventDto.
#### PostEventRequest
Structures the request data for creating an event.
#### UpdateEventRequest
Structures the request data for updating an event.

### Utilities (Packers)
#### Event Packer
Converts between Event entities and EventDto objects.
#### Image Packer
Converts between lists of ImageRefs and Images.

### Exceptions
#### GlobalExceptionHandler
Provides a consistent error response format at the global level. Custom exceptions like DuplicateRegistrationException and EventCreationException handle specific event-related scenarios.

### Repositories
Interfaces such as EventRepository, EventRegistrationRepository, and EventRatingRepository extend JpaRepository for database operations. EventSpecifications supports filtering functions in the service layer.

### Web Security
Configures security features of the microservice, including CORS rules.

## Services
### EventService
Handles creation, update, and deletion of events, manages image metadata, and retrieves events using various filters (title, date, location, tags). It also ensures that only valid event ratings are recorded post-event.
### EventRegistrationService
Manages event registrations to prevent duplicates, facilitates unregistrations, and retrieves all event participants.
### EventScheduler
Executes scheduled tasks to update the status of events based on their timing, marking events as expired and updating relevant participation and registration data.
### ImageService
Integrates with the s3 package to handle image storage and retrieval.

## Installation and Setup
1. Navigate to the service directory: `cd path/to/event-service`
2. Compile the project: `mvn clean install`
3. Start the service: `mvn spring-boot:run`