package com.datastat.model;

import lombok.Data;

@Data
public class PullsDetailsParmas {
  String org;
  String repo;
  String sig;
  String state;
  String ref;
  String author;
  String sort;
  String direction;
  String label;
  String exclusion;
  String search;
  Integer page;
  Integer per_page;

  public String toString() {

    return org + repo + sig + state + ref + author + sort + direction + label + exclusion + search + page + per_page;
  }
}
