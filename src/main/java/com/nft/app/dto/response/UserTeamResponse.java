package com.nft.app.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UserTeamResponse {

  private Integer teamSize;
  private List<String> teamMemberNames;

  public UserTeamResponse(List<String> namesList) {
    this.teamSize = namesList.size();
    this.teamMemberNames = namesList;
  }
}
