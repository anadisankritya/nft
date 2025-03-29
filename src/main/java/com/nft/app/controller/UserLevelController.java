package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.request.CreateUserLevelRequest;
import com.nft.app.dto.request.UpdateUserLevelRequest;
import com.nft.app.dto.response.CreateInvestmentResponse;
import com.nft.app.dto.response.CreateUserLevelResponse;
import com.nft.app.dto.response.PageResponse;
import com.nft.app.service.UserLevelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user-level")
public class UserLevelController {

  @Autowired
  private UserLevelService userLevelService;

  
    @GetMapping("/list")
    public ResponseEntity<NftResponse<PageResponse<List<CreateUserLevelResponse>>>> getUserLevels(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("User Levels data",
                        userLevelService.getAllUserLevels(page, size)));
    }

    @GetMapping
    public ResponseEntity<NftResponse<PageResponse<List<CreateUserLevelResponse>>>> getAllInvestmentTypes() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("User data",
                        userLevelService.getAllUserLevels(null, null)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NftResponse<CreateUserLevelResponse>> getUserLevel(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("Investment data",
                        userLevelService.getUserLevelById(id)));
    }

  @PostMapping("/create")
  public ResponseEntity<NftResponse<String>> createUserLevel(
      @Valid @RequestBody CreateUserLevelRequest createInvestmentRequest
  ) {
    userLevelService.createUserLevel(
        createInvestmentRequest
    );
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new NftResponse<>("201", "User Created Successfully", null
        ));
  }

  @PutMapping("/update")
  public ResponseEntity<NftResponse<String>> updateUserLevel(@Valid @RequestBody UpdateUserLevelRequest
                                                                 userLevelRequest) {
    userLevelService.updateUserLevel(userLevelRequest);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new NftResponse<>("User Level Updated Successfully", null
        ));
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<NftResponse<Object>> deleteUserLevel(@PathVariable String id) {
    userLevelService.deleteInvestmentType(id);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new NftResponse<>("User Level Deleted Successfully", null
        ));
  }

  @PatchMapping("/add-rules/{id}")
  public ResponseEntity<NftResponse<Object>> addUserLevelRules(@PathVariable String id,
                                                               @RequestBody List<String> rules) {
    userLevelService.addUserLevelRules(id, rules);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new NftResponse<>("User Level Rules added Successfully", null
        ));
  }
}