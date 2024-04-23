package com.whatsoeversky.test.service;

import com.whatsoeversky.test.request.AdminAddUserRequest;
import com.whatsoeversky.test.support.Result;

public interface UserInfoService {
    Result<Void> adminAddUser(AdminAddUserRequest request);

    Result<String> getUserResource(String resource);
}
