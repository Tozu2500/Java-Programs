package com.example.myapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public Map<String, Object> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", true);
        response.put("status", status);
        response.put("message", message != null ? message : "An error occurred");
        response.put("help", "Visit http://localhost:8080/ for API documentation");
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == 404) {
                response.put("details", "The endpoint you requested does not exist");
            }
        }
        
        return response;
    }
}
