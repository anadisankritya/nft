package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.request.CreateNFTRequest;
import com.nft.app.dto.response.CreateNFTResponse;
import com.nft.app.dto.response.PageResponse;
import com.nft.app.service.NFTService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nft")
public class NFTUploadController {

    @Autowired
    private NFTService nftService;

    @GetMapping("/list")
    public ResponseEntity<NftResponse<PageResponse<List<CreateNFTResponse>>>> getAllNFT(
            @RequestParam(required = false) String levelId,
            @RequestParam(required = false) String investmentTypeId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("User Levels data",
                        nftService.getAllNFTDetails(investmentTypeId,levelId,
                                page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NftResponse<CreateNFTResponse>> getNFT(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("Investment data",
                        nftService.getNFTDetailsById(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<NftResponse<String>> createNFT(
            @Valid @RequestBody CreateNFTRequest createNFTRequest
    ) {
        nftService.createNFTDetails(
                createNFTRequest
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new NftResponse<>("201","User Created Successfully",null
                        ));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<NftResponse<Object>> deleteNFT(@PathVariable String id) {
        nftService.deleteNFTDetails(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("User Level Deleted Successfully",null
                ));
    }
}