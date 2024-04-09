package com.example.demo.flow;

import lombok.Data;

import java.util.List;

@Data
public class Assignee {
    private List<String> users;
    private String multiMode;
}