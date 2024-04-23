package com.whatsoeversky.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 请先启动服务~~~!!!!!
 */
public class TestUserInfo {
    public static void main(String[] args) throws Exception {
        test1();
        test2();
        test3();
        test4();
    }


    /**
     * 测试角色对url的访问权限（admin访问）
     */
    public static void test1() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 1111);
        map.put("accountName", "jianghui");
        map.put("role", "admin");
        byte[] encode = Base64.getEncoder().encode(new ObjectMapper().writeValueAsString(map).getBytes());
        String header = new String(encode);
        RestTemplate restTemplate = new RestTemplateBuilder()
                .additionalInterceptors((ClientHttpRequestInterceptor) (request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();
                    headers.add("user-info", header);
                    return execution.execute(request, body);
                })
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("userId", 1111L);
        data.put("endpoint", Arrays.asList("aaa", "bbb", "ccc"));
        String s = restTemplate.postForObject("http://localhost:8080/admin/addUser", data, String.class);
        System.out.println(s);
        // admin访问结果： {"code":200,"msg":"success","data":null}
    }

    /**
     * 测试角色对url的访问权限（admin访问）
     */
    public static void test2() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 1111);
        map.put("accountName", "yangting");
        map.put("role", "user");
        byte[] encode = Base64.getEncoder().encode(new ObjectMapper().writeValueAsString(map).getBytes());
        String header = new String(encode);
        RestTemplate restTemplate = new RestTemplateBuilder()
                .additionalInterceptors((ClientHttpRequestInterceptor) (request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();
                    headers.add("user-info", header);
                    return execution.execute(request, body);
                })
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("userId", 1111L);
        data.put("endpoint", Arrays.asList("aaa", "bbb", "ccc"));
        String s = restTemplate.postForObject("http://localhost:8080/admin/addUser", data, String.class);
        System.out.println(s);
        // user访问结果： {"code":500,"msg":"the role cannot access the uri","data":null}
    }

    /**
     * 测试用户对数据的访问权限(1111用户有aaa的数据权限)
     */
    public static void test3() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 1111);
        map.put("accountName", "jianghui");
        map.put("role", "admin");
        byte[] encode = Base64.getEncoder().encode(new ObjectMapper().writeValueAsString(map).getBytes());
        String header = new String(encode);
        RestTemplate restTemplate = new RestTemplateBuilder()
                .additionalInterceptors((ClientHttpRequestInterceptor) (request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();
                    headers.add("user-info", header);
                    return execution.execute(request, body);
                })
                .build();
        String s = restTemplate.getForObject("http://localhost:8080/user/aaa", String.class);
        System.out.println(s);
        // 访问结果： {"code":200,"msg":"success","data":"aaa"}
    }

    /**
     * 测试用户对数据的访问权限(2222用户没有aaa的数据权限)
     */
    public static void test4() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 2222);
        map.put("accountName", "yangting");
        map.put("role", "user");
        byte[] encode = Base64.getEncoder().encode(new ObjectMapper().writeValueAsString(map).getBytes());
        String header = new String(encode);
        RestTemplate restTemplate = new RestTemplateBuilder()
                .additionalInterceptors((ClientHttpRequestInterceptor) (request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();
                    headers.add("user-info", header);
                    return execution.execute(request, body);
                })
                .build();
        String s = restTemplate.getForObject("http://localhost:8080/user/aaa", String.class);
        System.out.println(s);
        // 访问结果： {"code":500,"msg":"resource cannot be accessed","data":null}
    }
}
