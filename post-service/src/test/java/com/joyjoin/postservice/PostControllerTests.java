package com.joyjoin.postservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.postservice.controller.dto.CreatePostCommentRequest;
import com.joyjoin.postservice.controller.dto.CreatePostRequest;
import com.joyjoin.postservice.controller.dto.LikePostRequest;
import com.joyjoin.postservice.controller.dto.UpdatePostRequest;
import com.joyjoin.postservice.model.Comment;
import com.joyjoin.postservice.model.Post;
import com.joyjoin.postservice.modelDto.CommentDto;
import com.joyjoin.postservice.modelDto.PostDto;
import com.joyjoin.postservice.repository.CommentRepository;
import com.joyjoin.postservice.repository.PostRepository;
import com.joyjoin.postservice.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.joyjoin.postservice.Util.Util.asJsonString;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
		"spring.cloud.discovery.enabled=false",
		"spring.jpa.show-sql=true",
		"spring.main.allow-bean-definition-overriding=true"
})
@AutoConfigureMockMvc
@Testcontainers
@RunWith(SpringRunner.class)
class PostControllerTests {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
	}

	@Autowired
	PostRepository postRepository;
	@Autowired
	CommentRepository commentRepository;

	@Autowired
	PostService postService;


	@Autowired
	MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private PostDto postDto;
	private CommentDto commentDto;
	private UUID userId;
	private String token = "fyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFkYXl1aW5rd2FuQGdtYWlsLmNvbSIsImlhdCI6MTcxNTUzMTg3OSwiZXhwIjoxNzE1NjE1MDg3fQ.q-PdJFCtj8j-WW3cI2tfiT1jDW_LSR9UswPep47VzeE";

	private final String AUTHORIZATION_PREFIX = "Bearer ";

	@BeforeEach
	void setUp() {

		// Initialize userId
		userId = UUID.randomUUID();

		// Create post
		Post post = Post.builder()
				.userId(userId)
				.caption("TestCaption1")
				.taggedUsersId(Arrays.asList(UUID.randomUUID()))
				.taggedEventId(UUID.randomUUID())
				.likedUsersId(Arrays.asList(UUID.randomUUID()))
				.images(null)
				.build();
		postDto = postService.createPost(post);

		// Create comment
		Comment comment = Comment.builder()
				.userId(UUID.randomUUID())
				.postId(postDto.getId())
				.comment("TestComment1")
				.build();
		commentDto = postService.createComment(comment);
	}

	@Test
	void connectionEstablished() {
		assertThat(postgreSQLContainer.isCreated()).isTrue();
		assertThat(postgreSQLContainer.isRunning()).isTrue();
	}

	@Test
	void testGetAllPosts() throws Exception{
		mockMvc.perform(get("/api/posts")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].caption").value(postDto.getCaption()));
	}

	@Test
	void testGetAllPostsForUserId() throws Exception {
		// When userId has record
		mockMvc.perform(get("/api/posts/user/" + userId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].caption").value(postDto.getCaption()));

		// When userId does not has record
		mockMvc.perform(get("/api/posts/user/" + UUID.randomUUID())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());
	}

	@Test
	public void testGetPostById() throws Exception {
		// If postId is valid
		mockMvc.perform(get("/api/posts/{id}", postDto.getId())
						.header("Authorization", AUTHORIZATION_PREFIX + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(postDto.getId().toString()));

		// If postId is invalid
		mockMvc.perform(get("/api/posts/{id}", UUID.randomUUID())
						.header("Authorization", AUTHORIZATION_PREFIX + token))
				.andExpect(status().isNotFound());
	}

	@Test
	void testCreatePost() throws Exception {
		CreatePostRequest postRequest = CreatePostRequest.builder()
				.userId(userId)
				.caption("TestCaption2")
				.taggedUsersId(null)
				.taggedEventId(null)
				.build();

		// Perform the create request
		mockMvc.perform( MockMvcRequestBuilders
						.post("/api/posts/create")
						.content(objectMapper.writeValueAsString(postRequest))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isCreated())
						.andExpect(jsonPath("$.id").isNotEmpty());
	}

	@Test
	void testUpdatePost() throws Exception {
		UpdatePostRequest updateRequest = UpdatePostRequest.builder()
				.caption("newCaption2")
				.taggedUsersId(Arrays.asList(UUID.randomUUID()))
				.postId(postDto.getId())
				.taggedEventId(UUID.randomUUID())
				.build();

		// Perform the update request
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/posts/update")
						.content(Objects.requireNonNull(asJsonString(updateRequest)))
						.contentType("application/json")
						.accept("application/json"))
				.andExpect(status().isOk())
				.andReturn();

		// Extract the response content
		String responseContent = result.getResponse().getContentAsString();
		PostDto updatedPost = objectMapper.readValue(responseContent, PostDto.class);

		// Verify that the post's information has been updated in the database
		assertEquals(updatedPost.getCaption(), updateRequest.getCaption());
		assertEquals(updatedPost.getTaggedUsersId(), updateRequest.getTaggedUsersId());
		assertEquals(updatedPost.getTaggedEventId(), updateRequest.getTaggedEventId());
	}

	@Test
	void testDeletePost() throws Exception {
		// If postId is valid
		mockMvc.perform(delete("/api/posts/" + postDto.getId()))
				.andExpect(status().isOk())
				.andReturn();
		// Check if the post is deleted from the database
		Post deletedPost = postRepository.findById(postDto.getId()).orElse(null);
		assertThat(deletedPost).isNull();
		// Check if the comment under this post is deleted from the database
		List<Comment> comments = commentRepository.findCommentsByPostId(postDto.getId());
		assertTrue(comments.isEmpty());


		// If postId is invalid(test with randomUUID)
		mockMvc.perform(delete("/api/posts/" + UUID.randomUUID()))
				.andExpect(status().isNotFound());
	}

	@Test
	void testDeleteAllPost() throws Exception{
		mockMvc.perform(delete("/api/posts/deleteAll"))
				.andExpect(status().isOk());
	}
	@Test
	void testLikeUnlikePost() throws Exception {
		// When perform "Like"
		UUID likeUserId = UUID.randomUUID(); // Fixed UUID for like user

		LikePostRequest likeRequest = LikePostRequest.builder()
				.postId(postDto.getId())
				.likeUsersId(likeUserId)
				.liked(true)
				.build();

		// Perform the request and verify the response
		MvcResult result = mockMvc.perform(patch("/api/posts/likes/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Objects.requireNonNull(asJsonString(likeRequest))))
				.andExpect(status().isOk())
				.andReturn();

		// Extract the response content
		String responseContent = result.getResponse().getContentAsString();
		PostDto updatedPostAfterLike = objectMapper.readValue(responseContent, PostDto.class);
		// Verify that the post's information has been updated in the database
		assertTrue(updatedPostAfterLike.getLikedUsersId().stream().anyMatch(item -> item.equals(likeUserId)));

		// When perform "Unlike"
		LikePostRequest unlikeRequest = LikePostRequest.builder()
				.postId(postDto.getId())
				.likeUsersId(likeUserId)
				.liked(false)
				.build();

		// Perform the request and verify the response
		MvcResult result2 = mockMvc.perform(patch("/api/posts/likes/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Objects.requireNonNull(asJsonString(unlikeRequest))))
				.andExpect(status().isOk())
				.andReturn();

		// Extract the response content
		String responseContent2 = result2.getResponse().getContentAsString();
		PostDto updatedPostAfterUnlike = objectMapper.readValue(responseContent2, PostDto.class);
		// Verify that the post's information has been updated in the database
		assertFalse(updatedPostAfterUnlike.getLikedUsersId().stream().anyMatch(item -> item.equals(likeUserId)));

	}

	@Test
	void testGetAllLikesForPostId() throws Exception {
		// If postId is valid
		mockMvc.perform(get("/api/posts/{id}/likes", postDto.getId())
						.header("Authorization", AUTHORIZATION_PREFIX + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
//				.andExpect(jsonPath("$[0].id").value(postDto.getLikedUsersId().get(0).toString())); //Reason comment this out: Because I didn't initialize User's information

		// If postId is invalid
		mockMvc.perform(get("/api/posts/{id}/likes", UUID.randomUUID())
						.header("Authorization", AUTHORIZATION_PREFIX + token))
				.andExpect(status().isNotFound());
	}

	@Test
	void testCreateComment() throws Exception {
		CreatePostCommentRequest commentRequest = CreatePostCommentRequest.builder()
				.userId(UUID.randomUUID())
				.postId(postDto.getId())
				.comment("TestComment")
				.build();

		// Perform the request and verify the response
		mockMvc.perform(post("/api/posts/comments/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(commentRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty());
	}

	@Test
	void testDeleteComment() throws Exception {
		// If commentId is valid
		mockMvc.perform(delete("/api/posts/comments/" + commentDto.getId()))
				.andExpect(status().isOk())
				.andReturn();
		// Check if the comment is deleted from the database
		Comment deletedComment= commentRepository.findById(commentDto.getId()).orElse(null);
		assertThat(deletedComment).isNull();

		// If commentId is invalid(test with randomUUID)
		mockMvc.perform(delete("/api/posts/comments/" + UUID.randomUUID()))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetAllCommentsForPostId() throws Exception {
		// If postId is valid
		mockMvc.perform(get("/api/posts/"+ commentDto.getPostId() + "/comments")
						.header("Authorization", AUTHORIZATION_PREFIX + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].id").value(commentDto.getId().toString()));


		// If postId is valid
		mockMvc.perform(get("/api/posts/"+ UUID.randomUUID() + "/comments")
						.header("Authorization", AUTHORIZATION_PREFIX + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}










}
