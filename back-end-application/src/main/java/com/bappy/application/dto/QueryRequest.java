package com.bappy.application.dto;


import lombok.Data;

import java.util.List;

@Data
public class QueryRequest {
    private String query;
    private Integer topK = 5;
    private List<String> documentIds;
}
