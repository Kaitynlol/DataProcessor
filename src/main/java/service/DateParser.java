package service;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import model.InstrumentDto;
import org.h2.util.StringUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@Slf4j
public class DateParser {

  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
      .ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
  private static final LocalDate currentDate = LocalDate.parse("19-Dec-2014", DATE_FORMATTER);

  public static Flux<InstrumentDto> fluxStreamFromPath(Path filePath) {
    return fromPath(filePath)
        .flatMap(mapper)
        .filter(onlySuccess)
        .filter(dateNotGreater);
  }

  private static final Function<String, Publisher<? extends InstrumentDto>> mapper = element -> Flux
      .just(parseFrom(element))
      .onErrorReturn(InstrumentDto.error());

  /**
   * Only success parsed values should be returned
   */
  private static final Predicate<InstrumentDto> onlySuccess = InstrumentDto::isOk;

  /**
   * Requirement that date of values should not be greater than current
   */
  private static final Predicate<InstrumentDto> dateNotGreater = instrument -> instrument.getDate()
      .isBefore(currentDate);


  /**
   * <INSTRUMENT_NAME>,<DATE>,<VALUE>
   *
   * @param line income line
   * @return dto
   */
  private static InstrumentDto parseFrom(String line) {
    try {
      List<String> columns = Arrays.stream(line.trim().split("\\s*,\\s*"))
          .filter(v -> !StringUtils.isNullOrEmpty(v)).collect(Collectors.toList());
      if (columns.size() == 3) {
        return InstrumentDto.success(columns.get(0),
            LocalDate.parse(columns.get(1), DATE_FORMATTER),
            new BigDecimal(columns.get(2)));
      }
    } catch (Exception e) {
      return InstrumentDto.error();
    }
    return InstrumentDto.error();
  }

  private static Flux<String> fromPath(Path path) {
    return Flux.using(() -> Files.lines(path),
        Flux::fromStream,
        BaseStream::close
    );
  }


}
