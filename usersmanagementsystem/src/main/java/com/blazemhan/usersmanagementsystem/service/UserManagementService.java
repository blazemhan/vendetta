package com.blazemhan.usersmanagementsystem.service;

import com.blazemhan.usersmanagementsystem.dto.RequestResponse;
import com.blazemhan.usersmanagementsystem.model.User;
import com.blazemhan.usersmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class UserManagementService {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager  authenticationManager;
    @Autowired
    public UserManagementService(UserRepository userRepository, JWTUtils jwtUtils, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public RequestResponse register(RequestResponse registrationRequest){
        RequestResponse response = new RequestResponse();

        try{
            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            user.setCity(registrationRequest.getCity());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setName(registrationRequest.getName());
            user.setRole(registrationRequest.getRole());

            System.out.println("saving user");
            User userResult = userRepository.save(user);

            if(userResult.getId()>0){
                response.setUser(userResult);
                response.setMessage("User saved successfully");
                response.setStatusCode(200);
            }

        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public RequestResponse login(RequestResponse loginRequest){
        RequestResponse response = new RequestResponse();

        try{
            authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                    loginRequest.getPassword()));

        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        var jwt = jwtUtils.generateToken(user);
        var refreshToken = jwtUtils.generateRefereshToken(new HashMap<>(),user);
        response.setStatusCode(200);
        response.setToken(jwt);
        response.setMessage("User logged In successfully");
        response.setExpirationTime("24 Hours");

        } catch (Exception e) {
             response.setStatusCode(500);
             response.setMessage(e.getMessage());
        }

        return response;
    }

    public RequestResponse getAllUsers(){
        RequestResponse response = new RequestResponse();

        try{
            List<User> userList = userRepository.findAll();
            if(!userList.isEmpty()) {
                response.setUserList(userList);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("Not found");
            }
            return response;

        } catch (Exception e) {
             response.setStatusCode(500);
             response.setMessage("error occurred "+ e.getMessage());
             return response;
        }
    }

    public RequestResponse getUserById(Long id){
        RequestResponse response = new RequestResponse();

        try{
            User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User Not Found"));
            response.setUser(user);
            response.setStatusCode(200);
            response.setMessage("User with the id "+ id + "found successfully");

        } catch (Exception e) {
             response.setStatusCode(500);
             response.setMessage("Error occurred "+ e.getMessage());
        }
        return response;

    }

    public RequestResponse deleteUser(Long id){
        RequestResponse response = new RequestResponse();

        try{
            Optional<User> optionalUser = userRepository.findById(id);

            if(optionalUser.isPresent()){
                userRepository.deleteById(id);
                response.setStatusCode(200);
                response.setMessage("User deleted successfully");
            } else {
                response.setStatusCode(404);
                response.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
             response.setStatusCode(500);
             response.setMessage("Error occured while deleting user:"+ e.getMessage());
        }
        return response;
    }

    public RequestResponse updateUser(Long id, User updatedUser){

        RequestResponse response = new RequestResponse();
        try{
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()){
                User existingUser = optionalUser.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setRole(updatedUser.getRole());
                existingUser.setCity(updatedUser.getCity());

                User savedUser = userRepository.save(existingUser);
                response.setUser(savedUser);
                response.setStatusCode(200);
                response.setMessage("User updated successfully");
            } else{
                response.setStatusCode(404);
                response.setMessage("User not found");

            }


        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred : "+e.getMessage() );
        }
        return response;
    }

    public RequestResponse getMyInfo(String email){
        RequestResponse response = new RequestResponse();

        try{
            Optional<User>  optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isPresent()){
                response.setUser(optionalUser.get());
                response.setStatusCode(200);
                response.setMessage("successful");

            } else{
                response.setStatusCode(404);
                response.setMessage("User not found");

            }

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred  while getting user info: "+ e.getMessage());

        }
        return response;
    }
}
