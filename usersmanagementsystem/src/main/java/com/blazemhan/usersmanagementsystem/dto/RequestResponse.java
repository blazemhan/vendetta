package com.blazemhan.usersmanagementsystem.dto;

import com.blazemhan.usersmanagementsystem.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestResponse {


    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String email;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String city;
    private String role;
    private String password;
    private User user;
    private List<User> userList;
}
