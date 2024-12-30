package com.blazemhan.usersmanagementsystem.controller;

import com.blazemhan.usersmanagementsystem.dto.RequestResponse;
import com.blazemhan.usersmanagementsystem.model.User;
import com.blazemhan.usersmanagementsystem.service.UserManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserMgtController {

    private final UserManagementService userManagementService;

    public UserMgtController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }
    @PostMapping("/auth/register")
    public void  register(@RequestBody RequestResponse reg){
        System.out.println("hi");
        userManagementService.register(reg);

    }
    @GetMapping("/auth/login")
    public ResponseEntity<RequestResponse>  login(@RequestBody RequestResponse loginRequest){
        return ResponseEntity.ok(userManagementService.login(loginRequest));

    }
    @GetMapping("/admin/get-all-users")
    public ResponseEntity<RequestResponse>  getAllUsers(){
        return ResponseEntity.ok(userManagementService.getAllUsers());

    }

    @GetMapping("/admin/get-user/{userId}")
    public ResponseEntity<RequestResponse>  getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userManagementService.getUserById(userId));

    }
    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<RequestResponse>  updateUser(@PathVariable Long userId, @RequestBody User user ){
        return ResponseEntity.ok(userManagementService.updateUser(userId,user));

    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<RequestResponse>  getMyProfile(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        RequestResponse response = userManagementService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<RequestResponse>  deleteUser(@PathVariable Long userId){
        return ResponseEntity.ok(userManagementService.deleteUser(userId));

    }
}
