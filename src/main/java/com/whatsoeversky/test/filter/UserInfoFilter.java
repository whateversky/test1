package com.whatsoeversky.test.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsoeversky.test.exception.AccessException;
import com.whatsoeversky.test.repository.UserInfoRepository;
import com.whatsoeversky.test.support.Result;
import com.whatsoeversky.test.support.UserInfoContext;
import com.whatsoeversky.test.support.UserInfoToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

@Slf4j
@Component
public class UserInfoFilter extends OncePerRequestFilter {

    @Resource
    private UserInfoRepository userInfoRepository;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 从header中获取信息
            String url = request.getRequestURL().toString();
            String userInfoHeader = request.getHeader("user-info");
            byte[] bytes = Base64.getDecoder().decode(userInfoHeader);
            UserInfoToken userInfoToken = objectMapper.readValue(bytes, UserInfoToken.class);
            log.info("request user info, url: {}, userId: {}, accountName: {}, role: {}",
                    url, userInfoToken.getUserId(), userInfoToken.getAccountName(), userInfoToken.getRole());

            // 校验接口权限
            String requestURI = request.getRequestURI();
            if (!userInfoRepository.isRoleUriAccessible(userInfoToken.getRole(), requestURI)) {
                throw new AccessException("the role cannot access the uri");
            }

            // 存放用户信息
            UserInfoContext.setUserInfoToken(userInfoToken);

            // 执行过滤器链
            filterChain.doFilter(request, response);

            // 删除用户信息
            UserInfoContext.remove();
        } catch (AccessException e) {
            writeResult(response, e.getMessage());
        } catch (Exception e) {
            log.error("error get user info", e);
            writeResult(response, "server error");
        }
    }

    private void writeResult(HttpServletResponse response, String errorMsg) {
        try {
            Result<Object> result = Result.error(errorMsg);
            response.setHeader("Content-Type", "application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString(result));
            writer.flush();
        } catch (Exception e) {
            log.error("error write result", e);
        }
    }
}

