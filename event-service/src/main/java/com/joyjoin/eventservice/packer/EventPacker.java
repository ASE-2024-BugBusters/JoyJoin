package com.joyjoin.eventservice.packer;

import com.joyjoin.eventservice.model.Event;
import com.joyjoin.eventservice.modelDto.EventDto;
import com.joyjoin.eventservice.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventPacker {
    private final ImageService imageService;
    private final EventImagePacker eventImagePacker;
    private final ModelMapper modelMapper;

    @Autowired
    public EventPacker(ImageService imageService, EventImagePacker eventImagePacker, ModelMapper modelMapper) {
        this.imageService = imageService;
        this.eventImagePacker = eventImagePacker;
        this.modelMapper = modelMapper;
    }

    public EventDto packEvent(Event event) {
        var res = modelMapper.map(event, EventDto.class);
        //
//        EventDto eventDto = new EventDto();
//        eventDto.setTitle(event.getTitle());
//        eventDto.setTime(event.getTime());
//        LocationDto locationDto = new LocationDto();
//        eventDto.setLocation(locationDto);
//        eventDto.setParticipationLimit(event.getParticipationLimit());
//        eventDto.setDescription(event.getDescription());
//
//        Set<Tag> tags = event.getTags();
//        if (tags != null) {
//            eventDto.setTags(tags.stream().map(Enum::name).collect(Collectors.toSet()));
//        }
        //
//        if (event.getImg() != null) {
//            LocalDateTime now = LocalDateTime.now();
//            LocalDateTime expireTime = now.plusDays(1);
//            Duration durationUntilExpire = Duration.between(now, expireTime);
//
//            String imageUrl = imageService.getPreSignedUrlForDownload(
//                    event.getImg().getBucket(),
//                    event.getImg().getKey(),
//                    durationUntilExpire);
//
//            eventDto.setImageUrl(imageUrl);
//        }
        res.setImg(eventImagePacker.packImage(event.getImg(), LocalDateTime.now().plusDays(1)));
        return res;
    }
}

