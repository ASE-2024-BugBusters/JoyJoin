package com.joyjoin.eventservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.Location;
import com.joyjoin.eventservice.model.Tag;
import com.joyjoin.eventservice.modelDto.*;
import com.joyjoin.eventservice.service.EventRegistrationService;
import com.joyjoin.eventservice.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private EventRegistrationService eventRegistrationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    private EventDto eventDto;
    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setEventId(UUID.randomUUID());
        event.setTitle("Party");
        event.setTime(LocalDateTime.now().plusDays(1));
        event.setLocation(new Location("Vogelin", 12, "Zurich", 8050));
        event.setParticipationLimit(200);
        event.setDescription("This is description.");
        event.setTags(List.of(Tag.Cocktails, Tag.Vegetarianism));
        event.setCreatorId(UUID.randomUUID());

        eventDto = modelMapper.map(event, EventDto.class);
    }

    @Test
    void shouldCreateEventSuccessfully() throws Exception {
        PostEventRequest postRequest = new PostEventRequest();
        postRequest.setTitle("Spring Festival");
        postRequest.setTime(LocalDateTime.now().plusDays(1));
        postRequest.setLocation(new LocationDto("Santis", 10, "Zurich", 8050));
        postRequest.setParticipationLimit(300);
        postRequest.setDescription("This is another description.");
        postRequest.setCreatorId(UUID.randomUUID());

        given(eventService.saveEvent(any(Event.class))).willReturn(eventDto);

        mockMvc.perform(post("/api/events/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(eventDto.getTitle())));
    }

    @Test
    void shouldRetrieveAllEventsSuccessfully() throws Exception {
        given(eventService.getAllEvents()).willReturn(Arrays.asList(eventDto));

        mockMvc.perform(get("/api/events/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(eventDto.getTitle())));
    }

    @Test
    void shouldFetchSingleEventByIdSuccessfully() throws Exception {
        given(eventService.getEventById(eventDto.getEventId())).willReturn(eventDto);

        mockMvc.perform(get("/api/events/" + eventDto.getEventId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(eventDto.getTitle())));
    }

    @Test
    void shouldUpdateEventSuccessfully() throws Exception {
        UpdateEventRequest updateRequest = new UpdateEventRequest();
        updateRequest.setTitle("Updated Spring Festival");
        updateRequest.setTime(LocalDateTime.now().plusDays(2));
        updateRequest.setLocation(new LocationDto("City Park", 7, "Springville", 445400));
        updateRequest.setParticipationLimit(250);
        updateRequest.setDescription("Updated description of the annual community spring festival");

        given(eventService.updateEvent(eq(eventDto.getEventId()), any(Event.class))).willReturn(eventDto);

        mockMvc.perform(patch("/api/events/" + eventDto.getEventId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(eventDto.getTitle())));
    }
    @Test
    void shouldDeleteEventSuccessfully() throws Exception {
        UUID eventId = eventDto.getEventId();
        given(eventService.deleteEvent(eventId)).willReturn(eventDto);

        mockMvc.perform(delete("/api/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(eventDto.getTitle())));
    }
    @Test
    void shouldRegisterUserToEventSuccessfully() throws Exception {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        EventRegistrationDto registrationDto = new EventRegistrationDto();
        registrationDto.setEventId(UUID.fromString(eventId.toString()));
        registrationDto.setUserId(UUID.fromString(userId.toString()));
        given(eventRegistrationService.registerUserToEvent(eventId, userId)).willReturn(registrationDto);

        mockMvc.perform(post("/api/events/" + eventId + "/register/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId", is(eventId.toString())))
                .andExpect(jsonPath("$.userId", is(userId.toString())));
    }
    @Test
    void shouldUnregisterUserFromEventSuccessfully() throws Exception {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        EventRegistrationDto registrationDto = new EventRegistrationDto();
        registrationDto.setEventId(UUID.fromString(eventId.toString()));
        registrationDto.setUserId(UUID.fromString(userId.toString()));

        given(eventRegistrationService.removeUserToEvent(eventId, userId)).willReturn(registrationDto);

        mockMvc.perform(delete("/api/events/" + eventId + "/remove/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId", is(eventId.toString())))
                .andExpect(jsonPath("$.userId", is(userId.toString())));
    }
    @Test
    void shouldFilterEventsSuccessfully() throws Exception {
        List<EventDto> filteredEvents = Arrays.asList(eventDto);
        String title = "Party";
        String city = "Zurich";

        given(eventService.getFilteredEvents(title, city, null, null, false)).willReturn(filteredEvents);

        mockMvc.perform(get("/api/events/filter")
                        .param("title", title)
                        .param("city", city))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(eventDto.getTitle())));
    }
    @Test
    void shouldGetValidEventsByUserIdSuccessfully() throws Exception {
        UUID userId = UUID.randomUUID();
        List<EventDto> validEvents = Arrays.asList(eventDto);

        given(eventRegistrationService.getValidEventsByUserId(userId)).willReturn(validEvents);

        mockMvc.perform(get("/api/events/valid/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(eventDto.getTitle())));
    }

    @Test
    void shouldGetExpiredEventsByUserIdSuccessfully() throws Exception {
        UUID userId = UUID.randomUUID();
        List<EventDto> expiredEvents = Arrays.asList(eventDto);

        given(eventRegistrationService.getExpiredEventsByUserId(userId)).willReturn(expiredEvents);

        mockMvc.perform(get("/api/events/expired/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(eventDto.getTitle())));
    }
}
