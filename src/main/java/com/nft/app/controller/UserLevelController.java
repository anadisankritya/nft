package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.request.CreateInvestmentRequest;
import com.nft.app.dto.request.CreateUserLevelRequest;
import com.nft.app.dto.request.UpdateUserLevelRequest;
import com.nft.app.dto.response.CreateUserLevelResponse;
import com.nft.app.service.UserLevelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-level")
public class UserLevelController {

    @Autowired
    private UserLevelService userLevelService;

    @GetMapping("/list")
    public ResponseEntity<NftResponse<List<CreateUserLevelResponse>>> getInvestmentTypes(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("User Levels data",
                        userLevelService.getAllUserLevels(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NftResponse<CreateUserLevelResponse>> getInvestmentTypes(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("Investment data",
                        userLevelService.getUserLevelById(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<NftResponse<String>> createInvestment(
            @Valid @RequestBody CreateUserLevelRequest createInvestmentRequest
    ) {
        userLevelService.createUserLevel(
                createInvestmentRequest
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new NftResponse<>("201","User Created Successfully",null
                        ));
    }

    @PutMapping("/update")
    public ResponseEntity<NftResponse<String>> updateInvestment(@Valid @RequestBody UpdateUserLevelRequest
                                                                            userLevelRequest) {
        userLevelService.updateUserLevel(userLevelRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("User Level Updated Successfully",null
                ));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<NftResponse<Object>> deleteInvestmentType(@PathVariable String id) {
        userLevelService.deleteInvestmentType(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("User Level Deleted Successfully",null
                ));
    }
}