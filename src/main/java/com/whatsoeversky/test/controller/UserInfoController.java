package com.whatsoeversky.test.controller;

import com.whatsoeversky.test.annotation.ValidateResource;
import com.whatsoeversky.test.request.AdminAddUserRequest;
import com.whatsoeversky.test.service.UserInfoService;
import com.whatsoeversky.test.support.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @PostMapping("/admin/addUser")
    public Result<Void> adminAddUser(@RequestBody AdminAddUserRequest request) {
        return userInfoService.adminAddUser(request);
    }

    @GetMapping("/user/{resource}")
    @ValidateResource(resourceKey = "resource")
    public Result<String> getUserResource(@PathVariable("resource") String resource) {
        return userInfoService.getUserResource(resource);
    }


}
