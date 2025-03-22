package com.nft.app.dto.response;

import com.nft.app.entity.UserLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserLevelResponse {
    private String id;
    private String name;
    private Long seq;
    private String imageId;

    public CreateUserLevelResponse(UserLevel userLevel) {
        this.id = userLevel.getId();
        this.name = userLevel.getName();
        this.seq = userLevel.getSeq();
        this.imageId = userLevel.getImageId();
    }
}