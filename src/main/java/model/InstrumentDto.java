package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Data
public class InstrumentDto {

  private String name;
  private LocalDate date;
  private BigDecimal price;
  private boolean isOk = true;

  public static InstrumentDto success(String name, LocalDate date, BigDecimal price) {
    InstrumentDto result = new InstrumentDto();
    result.setName(name);
    result.setDate(date);
    result.setPrice(price);
    return result;
  }

  public static InstrumentDto error() {
    InstrumentDto result = new InstrumentDto();
    result.setOk(false);
    return result;
  }

}
