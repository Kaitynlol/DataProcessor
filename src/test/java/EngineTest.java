import static calculation.Engine.anyOf;
import static calculation.Engine.in;
import static calculation.Engine.instrumentsAndDate;
import static calculation.Engine.notIn;
import static junit.framework.TestCase.assertEquals;
import static model.InstrumentType.INSTRUMENT1;
import static model.InstrumentType.INSTRUMENT2;
import static model.InstrumentType.INSTRUMENT3;

import calculation.Engine;
import calculation.impl.DefaultEngineImpl;
import java.time.Month;
import model.InstrumentDto;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

public class EngineTest {

  public Flux<InstrumentDto> instruments;

  @Before
  public void setUp() {
    instruments = InstrumentsGenerator.fixedList();
  }

  @Test
  public void meanTest() {
    Engine engine = new DefaultEngineImpl();
    final Double meanAll = engine.mean(instruments).block();
    final Double meanInstrument1 = engine.mean(anyOf(instruments, in(INSTRUMENT1.name()))).block();
    final Double meanByInstrumentAndDate = engine
        .mean(anyOf(instruments, instrumentsAndDate(Month.NOVEMBER, 2014, INSTRUMENT2.name())))
        .block();

    assertEquals("Assert that mean return average price from fixed instruments",
        Double.valueOf(40 / 4), meanAll);
    assertEquals("Assert that mean return average price from fixed instruments of <INSTRUMENT1>",
        Double.valueOf(10), meanInstrument1);
    assertEquals(
        "Assert that mean return average price from fixed instruments of <INSTRUMENT2> and Date November 2014",
        Double.valueOf(10), meanByInstrumentAndDate);
  }

  @Test
  public void sumTest() {
    Engine engine = new DefaultEngineImpl();
    final Double sumAll = engine.sum(instruments).block();
    final Double top10 = engine.sumTop10(
        anyOf(instruments, notIn(INSTRUMENT1.name(), INSTRUMENT2.name(), INSTRUMENT3.name())))
        .block();
    assertEquals("Assert that mean return sum price from fixed instruments",
        Double.valueOf(40), sumAll);

    assertEquals(
        "Assert that mean return sum price from top 10 newest instruments not in 1,2,3 prefix",
        Double.valueOf(10), top10);
  }

}
