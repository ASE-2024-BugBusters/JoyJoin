package com.joyjoin.eventservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.model.Location;
import com.joyjoin.eventservice.model.Tag;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.modelDto.LocationDto;
import com.joyjoin.eventservice.modelDto.PostEventRequest;
import com.joyjoin.eventservice.modelDto.UpdateEventRequest;
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

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private EventService eventService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    private EventDto eventDto;
//    private Event event;
//
//    @BeforeEach
//    void setUp() {
//        event = new Event();
//        event.setEventId(UUID.randomUUID());
//        event.setTitle("Party");
//        event.setTime(LocalDateTime.now().plusDays(1));
//        event.setLocation(new Location("Vogelin", 12, "Zurich", "8050", "Switzerland"));
//        event.setParticipationLimit(200);
//        event.setDescription("This is description.");
//        event.setTags(List.of(Tag.Cocktails, Tag.Vegetarianism));
//        event.setCreatorId(UUID.randomUUID());
//
//        eventDto = modelMapper.map(event, EventDto.class);
//    }
//
//    @Test
//    void shouldCreateEventSuccessfully() throws Exception {
//        PostEventRequest postRequest = new PostEventRequest();
//        postRequest.setTitle("Spring Festival");
//        postRequest.setTime(LocalDateTime.now().plusDays(1));
//        postRequest.setLocation(new LocationDto("Santis", 10, "Zurich", "8050", "Switzerland"));
//        postRequest.setParticipationLimit(300);
//        postRequest.setDescription("This is another description.");
//        postRequest.setTags(List.of("Cocktails", "Vegetarianism"));
//        postRequest.setCreatorId(UUID.randomUUID());
//
//        given(eventService.saveEvent(any(Event.class))).willReturn(eventDto);
//
//        mockMvc.perform(post("/api/events/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(postRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.title", is(eventDto.getTitle())));
//    }
//
//    @Test
//    void shouldRetrieveAllEventsSuccessfully() throws Exception {
//        given(eventService.getAllEvents()).willReturn(Arrays.asList(eventDto));
//
//        mockMvc.perform(get("/api/events"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].title", is(eventDto.getTitle())));
//    }
//
//    @Test
//    void shouldFetchSingleEventByIdSuccessfully() throws Exception {
//        given(eventService.getEventById(eventDto.getEventId())).willReturn(eventDto);
//
//        mockMvc.perform(get("/api/events/" + eventDto.getEventId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title", is(eventDto.getTitle())));
//    }
//
//    @Test
//    void shouldUpdateEventSuccessfully() throws Exception {
//        UpdateEventRequest updateRequest = new UpdateEventRequest();
//        updateRequest.setTitle("Updated Spring Festival");
//        updateRequest.setTime(LocalDateTime.now().plusDays(2));
//        updateRequest.setLocation(new LocationDto("City Park", 7, "Springville", "445400", "China"));
//        updateRequest.setParticipationLimit(250);
//        updateRequest.setDescription("Updated description of the annual community spring festival");
//
//        given(eventService.updateEvent(eq(eventDto.getEventId()), any(Event.class))).willReturn(eventDto);
//
//        mockMvc.perform(patch("/api/events/" + eventDto.getEventId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title", is(eventDto.getTitle())));
//    }
}
