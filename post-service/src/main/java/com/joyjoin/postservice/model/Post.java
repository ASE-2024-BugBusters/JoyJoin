package com.joyjoin.postservice.model;


import com.joyjoin.postservice.model.template.DefaultProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "post")
@Entity
public class Post extends DefaultProperties {
    private String tag;
}
