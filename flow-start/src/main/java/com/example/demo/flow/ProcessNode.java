package com.example.demo.flow;

import lombok.Data;

import java.util.List;

@Data
public class ProcessNode {
    private String id;
    private String name;
    private String type;
    private ProcessNode next;
    private List<ExclusiveBranch> exclusive;
    private Assignee assignee;
    private List<FormPerm> formPerms;
}