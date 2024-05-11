package com.joyjoin.postservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.postservice.controller.dto.CreatePostRequest;
import com.joyjoin.postservice.controller.dto.UpdatePostRequest;
import com.joyjoin.postservice.model.Post;
import com.joyjoin.postservice.modelDto.PostDto;
import com.joyjoin.postservice.modelDto.PostWithUserInfoDto;
import com.joyjoin.postservice.repository.CommentRepository;
import com.joyjoin.postservice.repository.PostRepository;
import com.joyjoin.postservice.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@TestPropertySource(properties = {
		"spring.cloud.discovery.enabled=false",
		"spring.jpa.show-sql=true"
})
class PostServiceApplicationTests {

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
	private ObjectMapper objectMapper;

	@Autowired
	MockMvc mockMvc;

	private Post post;
	@Autowired
	private ModelMapper modelMapper;

	private final String AUTHORIZATION_PREFIX = "Bearer ";

	@BeforeEach
	void setUp() {

		List<UUID> _uuidList = new ArrayList<>();
		_uuidList.add(UUID.randomUUID());

		post = Post.builder()
				.userId(UUID.randomUUID())
				.caption("Test Caption")
				.taggedUsersId(_uuidList)
				.taggedEventId(UUID.randomUUID())
				.taggedUsersId(_uuidList)
				.likedUsersId(_uuidList)
				.build();
	}

	@Test
	void connectionEstablished() {
		assertThat(postgreSQLContainer.isCreated()).isTrue();
		assertThat(postgreSQLContainer.isRunning()).isTrue();
	}

	@Test
	void getAllPostsForUserId() throws Exception {
		given(postService.getAllPostsForUserId(post.getUserId())).willReturn(Arrays.asList(any(PostDto.class)));

		mockMvc.perform(get("/api/posts/user/" + post.getUserId()))
				.andExpect(status().isOk());
	}

	@Test
	void getPostById() throws Exception {
		given(postService.getAllPostsForUserId(post.getId())).willReturn(Arrays.asList(any(PostDto.class)));

		mockMvc.perform(get("/api/posts/user/" + post.getUserId()))
				.andExpect(status().isOk());
	}

	@Test
	void createPost() throws Exception {
		List<UUID> _uuidList = new ArrayList<>();
		_uuidList.add(UUID.randomUUID());

		CreatePostRequest postRequest = CreatePostRequest.builder()
				.userId(UUID.randomUUID())
				.caption("TestCaption2")
				.taggedUsersId(_uuidList)
				.taggedEventId(UUID.randomUUID())
				.build();

		given(postService.createPost(any(Post.class))).willReturn(any(PostDto.class));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/posts/create")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn();
	}

	@Test
	void updatePost() throws Exception {
		List<UUID> _uuidList = new ArrayList<>();
		_uuidList.add(UUID.randomUUID());

		UpdatePostRequest updateRequest = new UpdatePostRequest();

		updateRequest.setCaption("TestCaption1");
		updateRequest.setTaggedUsersId(_uuidList);
		updateRequest.setPostId(post.getId());
		updateRequest.setTaggedEventId(UUID.randomUUID());

		given(postService.updatePost(any(UpdatePostRequest.class))).willReturn(any(PostDto.class));

		mockMvc.perform(patch("/api/posts/update")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateRequest)))
				.andExpect(status().isOk());

	}

}
