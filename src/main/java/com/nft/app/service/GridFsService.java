package com.nft.app.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GridFsService {

    private final GridFsOperations gridFsOperations;

    // Upload file
    public String uploadFile(MultipartFile file) throws IOException {
        // Add metadata
        BasicDBObject metadata = new BasicDBObject();
        metadata.put("contentType", file.getContentType());
        metadata.put("fileSize", file.getSize());

        ObjectId fileId = gridFsOperations.store(
            file.getInputStream(),
            file.getOriginalFilename(),
            metadata
        );
        return fileId.toString();
    }

    // Download file
    public GridFsResource downloadFile(String filename) {
        GridFSFile file = gridFsOperations.findOne(
            Query.query(Criteria.where("filename").is(filename))
        );
        if (file == null) {
            throw new RuntimeException("File not found");
        }
        return gridFsOperations.getResource(file);
    }

    // Delete file
    public void deleteFile(String id) {
        gridFsOperations.delete(Query.query(Criteria.where("_id").is(id)));
    }
}