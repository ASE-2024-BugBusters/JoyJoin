package com.joyjoin.userservice.controller.dto;

import com.joyjoin.userservice.model.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAvatarUploadUrlResponse {
    Image image;
}
