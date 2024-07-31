package com.datastat.model;

import lombok.Data;

@Data
public class IssueDetailsParmas {
  String org;
  String repo;
  String sig;
  String state;
  String number;
  String author;
  String assignee;
  String label;
  String exclusion;
  String issue_state;
  String issue_type;
  Integer priority;
  String sort;
  String direction;
  String search;
  Integer page;
  Integer per_page;

  public String toString() {

    return org + repo + sig + state + number + author + assignee + label + exclusion + issue_state + issue_type
        + priority + sort + direction + search + page + per_page;
  }
}
