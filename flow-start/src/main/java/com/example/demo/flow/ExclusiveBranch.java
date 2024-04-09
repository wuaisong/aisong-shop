package com.example.demo.flow;

import lombok.Data;

@Data
public class ExclusiveBranch {
    private String id;
    private String condition;
    private ProcessNode process;
}