package com.joyjoin.eventservice.controller.dto;


import com.joyjoin.eventservice.model.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetImgUploadUrlResponse {
    Image image;
}
