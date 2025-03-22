package com.nft.app.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Document(collection = "userLevels")
public class UserLevel {
    @Id
    private String id;
    private String name;
    private Long seq;
    private String imageId;
    private boolean baseLevel;
    private String checkSum;
}