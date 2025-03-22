package com.nft.app.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.nft.app.dto.request.ImageData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    // Service method
    public List<ImageData> getFileDetailsByIds(List<String> imageIds) {
        List<ObjectId> objectIds = imageIds.stream()
                .map(ObjectId::new)
                .collect(Collectors.toList());

        List<GridFSFile> files = gridFsOperations.find(
                Query.query(Criteria.where("_id").in(objectIds))
        ).into(new ArrayList<>());

        return files.stream().map(file -> {
            try(InputStream stream = gridFsOperations.getResource(file).getInputStream()) {
                byte[] bytes = stream.readAllBytes(); // Java 9+ method
                GridFsResource resource = gridFsOperations.getResource(file);
                ImageData imageData = new ImageData();
                if (Objects.nonNull(file.getMetadata()))
                    imageData.setContentType(file.getMetadata().getString("contentType"));
                String image = Base64.getEncoder().encodeToString(bytes);
                imageData.setImage(image);
                imageData.setName(file.getFilename());
                imageData.setImageId(file.getObjectId().toString());
                return imageData;
            } catch (IOException e) {
                throw new RuntimeException("Error processing file: " + file.getFilename(), e);
            }
        }).collect(Collectors.toList());
    }

    // Delete file
    public void deleteFile(String id) {
        gridFsOperations.delete(Query.query(Criteria.where("_id").is(id)));
    }
}