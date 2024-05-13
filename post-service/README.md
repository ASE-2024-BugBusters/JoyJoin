# Post Service Microservice

## Overview

The Post Service microservice manages all aspects of post handling within our application, including creation, modification, retrieval, deletion, as well as managing relevant likes and comments.

## Features

- Retrieve post details, including relevant likes and comments.
- Create, update, and delete posts.
- Create and delete comments.
- Manage post images through AWS S3.


## Prerequisites

- Java 8 or higher
- Maven for dependency management

## External Dependencies

### Database

The database is used for persisting application data including user information, transaction records, and other operational data critical for service functionality.

Database configurations are specified in the `application.yml` file. By default, the service expects a PostgreSQL database named `postService` running at port `5435`. The default username and password can be found in the configuration file.

### Amazon S3 Storage

Amazon S3 is used for storing large files, such as post images uploaded by users, that are part of the application’s data.

S3 configurations are specified in the `application.yml` file as well.

Interactions with S3 utilized a stand-alone library named `libs3`, which can be found at `/path/to/JoyJoin/libs3/`.

### User Service Microservice

This microservice retrieves user information and integrates it with backend code before sending it back to the client side.
### Event Service Microservice

This microservice retrieves event information and integrates it with backend code before sending it back to the client side.


## Installation

To set up the Post Service Microservice on your local development machine, follow these steps:

1. Navigate to the service directory:
   ```shell
   cd /path/to/JoyJoin/post-service/
   ```
2. Start the service:
   ```shell
   mvn spring-boot:run
   ```

## Components

### Controllers

#### Post Controller
Handles all HTTP requests related to posts (as well as likes and comments), including CRUD operations and registrations. Endpoints are documented with JavaDoc for clarity and ease of use.

### Models

- **Post**: Represents the primary entity for a post.
- **Comment**: Manages post's comment data. Utilizes a primary key of `id`, with foreign keys for `postId` and `userId`. It interfaces with the CommentRepository for database interactions.
- **Image Ref/Image Url**: Manages image storage and retrieval details.

### DTOs (Data Transfer Objects)

- **PostDto**: Facilitates post data transfer between server and clients.
- **CommentDto**: Manages data transfer for post comments.
- **PostWithUserInfoDto**: Similar to PostDto, but carries detailed user and event information.
- **CommentWithUserInfoDto**: Similar to CommentDto, but carries detailed user information.
- **CreatePostRequest**: Structures request data for creating post.
- **UpdatePostRequest**: Structures request data for updating post.
- **CreatePostCommentRequest**: Structures request data for creating post comment.
- **LikePostRequest**: Structures request data for like/unlike a post.
- **EventDto, LocationDto (noncritical)**: Used for internal calls to retrieve event data for processing and generate PostWithUserInfoDto.
- **UserDto, InterestTag, ProfileVisibility, Role, RoleEnum (noncritical)**: Used for internal calls to retrieve user data for processing and generate PostWithUserInfoDto and CommentWithUserInfoDto.


### Services

- **PostService**: Manages post and comment lifecycle, including creation, updates, and retrieval using various filters.
- **ImageService**: Integrates with AWS S3 to manage image storage and retrieval.

### Repositories

- **PostRepository**, **CommentRepository**: Interfaces for database operations related to posts and comments.

### Utilities

- **Post Packer**: Converts between Post entities and PostDto objects.
- **Comment Packer**:  Converts between Comment entities and CommentDto objects.
- **Image Packer**: Manages conversions between ImageRef lists and actual images.

### Exceptions

- **ResourceNotFoundException**: Handles errors related to posts, including scenarios where an invalid postId is searched.

### Security

Configures web security features of the microservice, including CORS rules.


## API Documentation

This section details the API endpoints provided by the Post Service Microservice, including descriptions of their functionalities, HTTP methods, request bodies, and responses.

### Post Operations

#### Get All Posts
- **Endpoint:** `GET /api/posts`
- **Description:** Retrieves all posts.
- **Response:** Returns a list of `PostDto`.

#### Get All Posts for Specific User
- **Endpoint:** `GET /api/posts/user/{id}`
- **Description:** Retrieves all posts for specific user (by userId).
- **Response:** Returns a list of `PostDto`.

#### Get Specific Post Details
- **Endpoint:** `GET /api/posts/{id}`
- **Description:** Retrieves detailed information of specific post (by postId), including user & event information.
- **Response:** Returns a list of `PostWithUserInfoDto`.


#### Create Post
- **Endpoint:** `POST /api/posts/create`
- **Description:** Creates a new post based on the provided post details.
- **Request Body:** `CreatePostRequest` containing post details.
- **Response:** Returns the created `PostDto` with a `201 CREATED` status.

#### Update Post
- **Endpoint:** `PATCH /api/posts/update`
- **Description:** Updates an post's details based on the provided data.
- **Request Body:** `UpdatePostRequest` containing updated fields for the post.
- **Response:** Returns the updated `PostDto`.

#### Delete Post
- **Endpoint:** `DELETE /api/posts/{id}`
- **Description:** Deletes a post by its ID.
- **Response:** String message indicate deletion successful.

#### Delete All Post
- **Endpoint:** `DELETE /api/posts/deleteAll`
- **Description:** Deletes all posts.
- **Response:** String message indicate deletion successful.

### Post's Likes

#### Like/Unlike Post
- **Endpoint:** `PATCH /api/posts/likes/create`
- **Description:** Updates post's like list according to the provided data.
- **Request Body:** `LikePostRequest` containing postId, userId and a boolean represent like/dislike.
- **Response:** Returns the updated `PostDto`.

#### Get All Likes for Specific Post
- **Endpoint:** `GET /api/posts/{id}/likes`
- **Description:** Retrieves all likes for specific post (by postId), including user information.
- **Response:** Returns a list of `UserDto`.

### Post's Comments
#### Create Comment
- **Endpoint:** `POST /api/posts/comments/create`
- **Description:** Creates a new comment and include postId and userId.
- **Request Body:** `CreatePostCommentRequest` containing comment details.
- **Response:** Returns the created `CommentDto` with a `201 CREATED` status.

#### Delete Comment
- **Endpoint:** `DELETE /api/posts/comments/{id}`
- **Description:** Deletes a comment by its ID.
- **Response:** String message indicate deletion successful.

#### Get All Comments for Specific Post
- **Endpoint:** `GET /api/posts/{id}/comments`
- **Description:** Retrieves all comments for specific post (by postId), including user information.
- **Response:** Returns a list of `CommentWithUserInfoDto`.


### Post Image Management

#### Get Upload Image URL
- **Endpoint:** `GET /api/posts/get_upload_image_url`
- **Description:** Generates a pre-signed URL for uploading images with a 30-minute expiry.
- **Response:** Returns a `GetImgUploadUrlResponse` containing the URL and expiration details.
