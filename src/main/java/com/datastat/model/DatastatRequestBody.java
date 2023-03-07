package com.datastat.model;

import java.util.ArrayList;
import java.util.HashMap;

public class DatastatRequestBody {
    private ArrayList<String> metrics;
    private HashMap<String, Object> variables;
    private String operation;
    private String filter;
    private long start;
    private long end;

    public ArrayList<String> getMetrics() {
        return metrics;
    }

    public void setMetrics(ArrayList<String> metrics) {
        this.metrics = metrics;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public HashMap<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(HashMap<String, Object> variables) {
        this.variables = variables;
    }
    
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
