package com.urbanrides.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AproveCaptainsDataDto {
    private int captainId;
    private List<String> verifiedDocId;
    private List<String> unverifiedDocId;
}
