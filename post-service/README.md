# Post Service Microservice

## Overview
The Post Service microservice is designed to handle all aspects of post management within our application. This includes creating, modifying, retrieving, and deleting posts, as well as managing relevant likes and comments.
## Components

### Post Controller
The Post Controller handles all incoming HTTP requests related to posts, including operations for creating, updating, retrieving, and deleting post data, as well as managing relevant likes and comments. All endpoints are well-documented with JavaDocs.

### Models
#### Post
Represents a post, serving as the primary entity.
#### Comment
Manages post's comment data. Utilizes a primary key of `id`, with foreign keys for `postId` and `userId`. It interfaces with the CommentRepository for database interactions.
#### Image, ImageRef, ImageUrl
These models manage various aspects of image handling within posts:
- **ImageRef** is used in the post entity and includes fields for the bucket and key.
- **ImageUrl** is generated in the PostDto for easy image retrieval and storage.

### Converters
#### ImageRefListConverter
Converts a list of ImageRef entities to and from database columns.

### Model DTOs (Data Transfer Objects)
#### PostDto
Transfers post data between the server and clients.
#### CommentDto
Transfers post-comment data between the server and clients.
#### PostWithUserInfoDto
Transfers detailed post data, including user and event information, between the server and clients.
#### CommentWithUserInfoDto
Transfers detailed post-comment data, including user information, between the server and clients.
#### EventDto, LocationDto (noncritical)
Used for internal calls to retrieve event data for processing and generate PostWithUserInfoDto.
#### UserDto, InterestTag, ProfileVisibility, Role, RoleEnum (noncritical)
Used for internal calls to retrieve user data for processing and generate PostWithUserInfoDto and CommentWithUserInfoDto.
### Utilities (Packers)
#### Post Packer
Converts between Post entities and PostDto objects.
#### Comment Packer
Converts between Comment entities and CommentDto objects.
#### Image Packer
Converts between lists of ImageRefs and Images.

### Exceptions
#### ResourceNotFoundException
Handles errors related to posts, including scenarios where an invalid postId is searched.

### Repositories
Interfaces such as PostRepository and CommentRepository extend JpaRepository for database operations.

### Web Security
Configures security features of the microservice, including CORS rules.

## Services
### PostService
Handles the creation, updating, and deletion of events, manages image metadata, and manages the creation and deletion of post comments.
### ImageService
Integrates with the s3 package to handle image storage and retrieval.

## Installation and Setup
1. Navigate to the service directory: `cd path/to/post-service`
2. Compile the project: `mvn clean install`
3. Start the service: `mvn spring-boot:run`