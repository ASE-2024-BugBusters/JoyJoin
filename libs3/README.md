# JoyJoin S3 Library

This Java library provides easy-to-use interfaces to interact with AWS S3 for storing and retrieving images securely using pre-signed URLs.

## Features

- Generate pre-signed URLs for uploading images to S3.
- Generate pre-signed URLs for downloading images from S3.

## Prerequisites

- Java 8 or higher
- Maven for dependency management

## Installation

To include this library in your Java project, add the following dependency to your project's `pom.xml` file:

```xml
<dependency>
    <groupId>com.joyjoin</groupId>
    <artifactId>libs3</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Usage

### Configuring S3 Access

Create an instance of `S3Config` and set your AWS credentials:

```java
S3Config config = new S3Config();
config.setAccessKey("your-access-key");
config.setSecretKey("your-secret-key");
config.setEndpoint("https://s3.your-region.amazonaws.com");
config.setRegion("your-region");
```

### Creating an ImageService Instance

Using the configured `S3Config`, create an instance of `ImageService`:

```java
ImageService imageService = new ImageService(config);
```

### Generating a Pre-Signed URL for Upload

To generate a pre-signed URL for uploading an image to a specific bucket and key:

```java
String bucket = "your-bucket-name";
String key = "your-image-key";
String uploadUrl = imageService.getPreSignedUrlForUpload(bucket, key);
// then make PUT request with image data in the body to uploadUrl
```

### Generating a Pre-Signed URL for Download

Similarly, to generate a pre-signed URL for downloading an image:

```java
String downloadUrl = imageService.getPreSignedUrlForDownload(bucket, key, Duration.ofMinutes(10));
// then make HTTP GET request to downloadUrl to retrieve image data
```

## Springboot Integration

To seamlessly integrate the JoyJoin S3 Library with a Springboot application, you can extend the `S3Config` class using Springboot's configuration properties mechanism. This approach allows you to conveniently configure your S3 settings through your application's properties file.

### Configuring S3 in Springboot

Create a new configuration class that extends `S3Config` and annotate it with `@Configuration` and `@ConfigurationProperties` to automatically bind the properties prefixed with `s3`:

```java
@Configuration
@ConfigurationProperties(prefix = "s3")
@Getter
@Setter
public class S3Config extends com.joyjoin.libs3.S3Config {
}
```

### Application Properties

Add the necessary S3 configuration properties to your `application.properties` file:

```properties
# application.properties
s3.accessKey=your-access-key
s3.secretKey=your-secret-key
s3.endpoint=https://s3.your-region.amazonaws.com
s3.region=your-region
```

Using this setup, Springboot will automatically populate the `S3Config` object with the S3 configuration specified in your properties file, simplifying the management of your environment-specific configurations.

### Using `ImageService`

To use the `ImageService` within your Springboot application, you can extend the service and leverage the `@Autowired` annotation to inject the necessary configuration. Here is an example of how to properly integrate and configure `ImageService`:

```java
import org.springframework.stereotype.Service;

@Service
public class ImageService extends com.joyjoin.libs3.ImageService {    
    @Autowired
    public ImageService(S3Config config) {
        super(config);
    }
}
```

This integration method ensures that your AWS S3 configurations are managed centrally through Springboot’s powerful configuration management framework.

## Development

### Publish

Use the following command to publish updated version to GitHub Package:

```shell
mvn deploy
```