package com.celonis.challenge.model;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CounterTask extends Task {

  private Integer x;
  private Integer y;
}
