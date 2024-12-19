package com.example.SpringSecurityDemo.Entity.model;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class EndTimeRequest {
    @JsonFormat(pattern = "yy/MM/dd HH:mm:ss")
    private LocalDateTime endTime;

    // Getters and Setters
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}

