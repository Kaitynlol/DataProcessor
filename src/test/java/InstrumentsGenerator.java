import static model.InstrumentType.INSTRUMENT1;
import static model.InstrumentType.INSTRUMENT2;
import static model.InstrumentType.INSTRUMENT3;
import static service.DateParser.DATE_FORMATTER;

import java.math.BigDecimal;
import java.time.LocalDate;
import model.InstrumentDto;
import reactor.core.publisher.Flux;

public class InstrumentsGenerator {


  public static Flux<InstrumentDto> fixedList() {
    InstrumentDto instrument1 = InstrumentDto
        .success(INSTRUMENT1.name(), LocalDate.parse("11-Mar-1996", DATE_FORMATTER),
            BigDecimal.valueOf(10l));
    InstrumentDto instrument2 = InstrumentDto
        .success(INSTRUMENT2.name(), LocalDate.parse("11-Nov-2014", DATE_FORMATTER),
            BigDecimal.valueOf(10l));
    InstrumentDto instrument3 = InstrumentDto
        .success(INSTRUMENT3.name(), LocalDate.parse("11-Mar-2010", DATE_FORMATTER),
            BigDecimal.valueOf(10l));
    InstrumentDto instrument4 = InstrumentDto
        .success("INSTRUMENT4", LocalDate.parse("11-Mar-2009", DATE_FORMATTER),
            BigDecimal.valueOf(10l));
    return Flux.just(instrument1, instrument2, instrument3,instrument4);
  }

}
