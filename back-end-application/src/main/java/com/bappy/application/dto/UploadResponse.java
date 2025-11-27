package com.bappy.application.dto;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UploadResponse {
    private UUID id;
    private String status;
}
