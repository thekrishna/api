package com.insurance.vector.search.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class DocumentRequest {
    private String content;
    private Map<String, Object> metadata;

    public DocumentRequest() {
    }

    public DocumentRequest(String content, Map<String, Object> metadata) {
        this.content = content;
        this.metadata = metadata;
    }

}
