package com.whatsoeversky.test.request;

import lombok.Data;

import java.util.List;

@Data
public class AdminAddUserRequest {
    private Long userId;
    private List<String> endpoint;
}
