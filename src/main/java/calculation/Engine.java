package calculation;

import com.google.common.collect.Sets;
import java.time.Month;
import java.util.HashSet;
import java.util.function.Function;
import model.InstrumentDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Engine {

  String MEAN_FUNCTION_NAME = "mean";
  String SUM_FUNCTION_NAME = "sum";
  String TOP_NEWEST_SUM_FUNCTION_NAME = "sumTop10";

  /**
   * For <INSTRUMENT>– mean
   */
  Mono<Double> mean(Flux<InstrumentDto> instruments);

  /**
   * For INSTRUMENT3 – sum values
   */
  Mono<Double> sum(Flux<InstrumentDto> instruments);

  /**
   * sum of the newest 10 elements (in terms of the date)
   */
  Mono<Double> sumTop10(Flux<InstrumentDto> instruments);

  static Flux<InstrumentDto> anyOf(Flux<InstrumentDto> instruments,
      Function<Flux<InstrumentDto>, Flux<InstrumentDto>> apply) {
    return instruments.transform(apply);
  }

  static Function<Flux<InstrumentDto>, Flux<InstrumentDto>> instrumentsAndDate(Month month,
      int year, String... args) {
    final HashSet<String> names = Sets
        .newHashSet(args);
    return flux ->
        flux.filter(
            value -> names.contains(value.getName())
                && value.getDate().getMonth() == month
                && value.getDate().getYear() == year);
  }

  static Function<Flux<InstrumentDto>, Flux<InstrumentDto>> in(String... args) {
    final HashSet<String> names = Sets
        .newHashSet(args);
    return flux ->
        flux.filter(
            value -> names.contains(value.getName()));

  }

  static Function<Flux<InstrumentDto>, Flux<InstrumentDto>> notIn(String... args) {
    final HashSet<String> names = Sets
        .newHashSet(args);
    return flux -> flux.filter(value -> !names.contains(value.getName()));
  }
}
