package com.whatsoeversky.test.support;

import lombok.Data;

@Data
public class UserInfoToken {
    private Long userId;
    private String accountName;
    private String role;
}
