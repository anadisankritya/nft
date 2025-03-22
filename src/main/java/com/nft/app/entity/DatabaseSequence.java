package com.nft.app.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "databaseSequences")
public class DatabaseSequence {
    @Id
    private String id;
    private long seq;
    private String seqName;
    private boolean status;
}