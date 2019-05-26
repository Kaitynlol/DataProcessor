package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(staticName = "of")
@Data
public class ResultValue<T> {

  private String instrument;
  private String function;
  private T data;

}
