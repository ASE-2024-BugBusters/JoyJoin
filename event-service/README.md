# Event Service Microservice

## Overview

The Event Service microservice manages all aspects of event handling within our application, including creation, modification, retrieval, deletion, registration, and rating of events.

## Features

- Create, update, and delete events.
- Register and unregister for events.
- Retrieve event details, including by filters like title, date, and location.
- Manage event images through AWS S3.
- Handle event ratings.

## Prerequisites

- Java 8 or higher
- Maven for dependency management

## External Dependencies

### Database

The database is used for persisting application data including user information, transaction records, and other operational data critical for service functionality.

Database configurations are specified in the `application.yml` file. By default, the service expects a PostgreSQL database named `eventService` running at port `5434`. The default username and password can be found in the configuration file.

### Amazon S3 Storage

Amazon S3 is used for storing large files, such as event images uploaded by users, that are part of the application’s data.

S3 configurations are specified in the `application.yml` file as well.

Interactions with S3 utilized a stand-alone library named `libs3`, which can be found at `/path/to/JoyJoin/libs3/`.

## Installation

To set up the Event Service Microservice on your local development machine, follow these steps:

1. Navigate to the service directory:
   ```shell
   cd /path/to/JoyJoin/event-service/
   ```
2. Start the service:
   ```shell
   mvn spring-boot:run
   ```

## Components

### Controllers

#### Event Controller
Handles all HTTP requests related to events, including CRUD operations and registrations. Endpoints are documented with JavaDoc for clarity and ease of use.

### Models

- **Event**: Represents the primary entity for an event.
- **Event Registration**: Manages registration data with `registrationId`, `eventId`, and `userId` as key identifiers.
- **Event Registration Count**: Updates and checks event participation counts.
- **Image Ref/Image Url**: Manages image storage and retrieval details.
- **Rating**: Manages event rating data.
- **Location/Tag**: Represents detailed components of an event such as location and tags.

### DTOs (Data Transfer Objects)

- **EventDto**: Facilitates event data transfer between server and clients.
- **EventRegistrationDto**: Manages data transfer for event registrations.
- **LocationDto**: Carries detailed location information within an event.
- **PostEventRequest**: Structures request data for creating events.
- **UpdateEventRequest**: Structures request data for updating events.

### Services

- **EventService**: Manages event lifecycle, including creation, updates, and retrieval using various filters.
- **EventRegistrationService**: Handles event registrations and unregistrations, ensuring no duplicate registrations.
- **ImageService**: Integrates with AWS S3 to manage image storage and retrieval.

### Repositories

- **EventRepository**, **EventRegistrationRepository**, **EventRatingRepository**: Interfaces for database operations related to events, registrations, and ratings.
- **EventSpecifications**: Supports advanced filtering functions in the service layer.

### Utilities

- **Event Packer**: Converts between Event entities and EventDto objects.
- **Image Packer**: Manages conversions between ImageRef lists and actual images.

### Exceptions

- **GlobalExceptionHandler**: Provides consistent error handling across the microservice.
- **DuplicateRegistrationException**, **EventCreationException**: Handle specific scenarios related to event handling.

### Security

Configures web security features of the microservice, including CORS rules.


## API Documentation

This section details the API endpoints provided by the Event Service Microservice, including descriptions of their functionalities, HTTP methods, request bodies, and responses.

### Event Operations

#### Create Event
- **Endpoint:** `POST /api/events/create`
- **Description:** Creates a new event based on the provided event details.
- **Request Body:** `PostEventRequest` containing event details such as title, time, location, etc.
- **Response:** Returns the created `EventDto` with a `201 CREATED` status.

#### Get All Events
- **Endpoint:** `GET /api/events/all`
- **Description:** Retrieves all currently active events.
- **Response:** Returns a list of `EventDto`.

#### Get Event by ID
- **Endpoint:** `GET /api/events/{eventId}`
- **Description:** Fetches details of a specific event by its ID.
- **Response:** Returns an `EventDto` if found; otherwise, a `404 NOT FOUND` status.

#### Update Event
- **Endpoint:** `PATCH /api/events/{eventId}`
- **Description:** Updates an event's details based on the provided data.
- **Request Body:** `UpdateEventRequest` containing updated fields for the event.
- **Response:** Returns the updated `EventDto`.

#### Delete Event
- **Endpoint:** `DELETE /api/events/{eventId}`
- **Description:** Deletes an event by its ID.
- **Response:** Returns the deleted `EventDto`.

### Event Registration Operations

#### Register to Event
- **Endpoint:** `POST /api/events/{eventId}/register/{userId}`
- **Description:** Registers a user to an event using their respective UUIDs.
- **Response:** Returns the `EventRegistrationDto` after registration.

#### Unregister from Event
- **Endpoint:** `DELETE /api/events/{eventId}/remove/{userId}`
- **Description:** Unregisters a user from an event.
- **Response:** Returns the updated `EventRegistrationDto` after unregistration.

#### List Event Participants
- **Endpoint:** `GET /api/events/{eventId}/participants`
- **Description:** Retrieves a list of participants' UUIDs for a specified event.
- **Response:** Returns a list of UUIDs representing the participants.

#### Get Valid Events for User
- **Endpoint:** `GET /api/events/valid/{userId}`
- **Description:** Retrieves a list of attended events which are still valid (not expired) for a specified user.
- **Path Variable:** `userId` - The UUID of the user for whom to query valid events.
- **Response:** Returns a list of `EventDto` representing all valid events that the user has registered for or attended.

#### Get Expired Events for User
- **Endpoint:** `GET /api/events/expired/{userId}`
- **Description:** Retrieves a list of attended events that have expired for a specified user.
- **Path Variable:** `userId` - The UUID of the user for whom to query expired events.
- **Response:** Returns a list of `EventDto` representing all expired events that the user has registered for or attended.

### Event Image Management

#### Get Upload Image URL
- **Endpoint:** `GET /api/events/get_upload_image_url`
- **Description:** Generates a pre-signed URL for uploading images with a 30-minute expiry.
- **Response:** Returns a `GetImgUploadUrlResponse` containing the URL and expiration details.

#### Update Event Images
- **Endpoint:** `PATCH /api/events/{eventId}/images`
- **Description:** Updates the images associated with an event.
- **Request Body:** `UpdateEventRequest` containing new image details for the event.
- **Response:** Returns the updated `EventDto` with new images.

### Event Rating Operations

#### Submit Rating
- **Endpoint:** `POST /api/events/rating`
- **Description:** Submits a new rating for an event.
- **Request Body:** `Rating`
- **Response:** Returns the saved `Rating`.

#### Get Ratings by Event ID
- **Endpoint:** `GET /api/events/rating/{eventId}`
- **Description:** Retrieves all ratings for a specific event identified by its UUID.
- **Response:** Returns a list of `Rating`.

#### Get All Ratings
- **Endpoint:** `GET /api/events/rating`
- **Description:** Retrieves ratings for all events.
- **Response:** Returns a list of all `Rating`.