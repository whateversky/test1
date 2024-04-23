package com.whatsoeversky.test.service.impl;

import com.whatsoeversky.test.entity.UserInfo;
import com.whatsoeversky.test.repository.UserInfoRepository;
import com.whatsoeversky.test.request.AdminAddUserRequest;
import com.whatsoeversky.test.service.UserInfoService;
import com.whatsoeversky.test.support.Result;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoRepository userInfoRepository;

    @Override
    public Result<Void> adminAddUser(AdminAddUserRequest request) {
        if (Objects.isNull(request.getUserId())) {
            return Result.error("userId cannot be null");
        }
        if (CollectionUtils.isEmpty(request.getEndpoint())) {
            return Result.error("endpoint cannot be empty");
        }
        // 请求转对象
        UserInfo userInfo = buildUserInfo(request);
        userInfoRepository.adminAddUser(userInfo);
        return Result.ok();
    }

    private UserInfo buildUserInfo(AdminAddUserRequest request) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(request.getUserId());
        userInfo.setEndpoint(request.getEndpoint());
        return userInfo;
    }

    @Override
    public Result<String> getUserResource(String resource) {
        return Result.ok(resource);
    }
}
