import static junit.framework.TestCase.assertEquals;
import static service.DateParser.DATE_FORMATTER;

import java.time.LocalDate;
import org.junit.Test;

public class DateParserTest {

  @Test
  public void dateFormatter() {
    final LocalDate date = LocalDate.parse("11-Mar-1996", DATE_FORMATTER);
    assertEquals("Date formatter test month <March>", 3, date.getMonth().getValue());
    assertEquals("Date formatter test month <Date>", 11, date.getDayOfMonth());
    assertEquals("Date formatter test month <Year>", 1996, date.getYear());
  }

}
