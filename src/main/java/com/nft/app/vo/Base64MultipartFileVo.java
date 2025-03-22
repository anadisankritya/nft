package com.nft.app.vo;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;

public class Base64MultipartFileVo implements MultipartFile {

    private final byte[] fileContent;
    private final String name;
    private final String contentType;

    public Base64MultipartFileVo(String base64, String name, String contentType) {
        this.fileContent = Base64.getDecoder().decode(base64);
        this.name = name;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return name;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public byte[] getBytes() {
        return fileContent;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(fileContent);
        }
    }
}
