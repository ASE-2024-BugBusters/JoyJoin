package com.joyjoin.postservice.controller.dto;


import com.joyjoin.postservice.model.Image;
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
