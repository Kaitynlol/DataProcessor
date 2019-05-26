import static calculation.Engine.MEAN_FUNCTION_NAME;
import static calculation.Engine.SUM_FUNCTION_NAME;
import static calculation.Engine.TOP_NEWEST_SUM_FUNCTION_NAME;
import static calculation.Engine.anyOf;
import static calculation.Engine.in;
import static calculation.Engine.instrumentsAndDate;
import static calculation.Engine.notIn;
import static model.InstrumentType.INSTRUMENT1;
import static model.InstrumentType.INSTRUMENT2;
import static model.InstrumentType.INSTRUMENT3;
import static service.DateParser.fluxStreamFromPath;

import calculation.impl.DefaultEngineImpl;
import calculation.Engine;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import lombok.extern.slf4j.Slf4j;
import model.InstrumentDto;
import model.InstrumentType;
import model.ResultValue;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
class DataProcessorApp {

  private static final Path FILE_PATH = Paths.get("src/main/resources/input.txt");
  private static final Engine engine = new DefaultEngineImpl();
  private final static Runtime runtime = Runtime.getRuntime();

  public static void main(String[] args) {

    final Flux<InstrumentDto> instruments = fluxStreamFromPath(FILE_PATH);
    final Mono<ResultValue> mean1 = mapResult(
        engine.mean(anyOf(instruments, in(INSTRUMENT1.name()))), INSTRUMENT1, MEAN_FUNCTION_NAME);

    final Mono<ResultValue> mean2 = mapResult(engine
            .mean(anyOf(instruments, instrumentsAndDate(Month.NOVEMBER, 2014, INSTRUMENT2.name()))),
        INSTRUMENT2, MEAN_FUNCTION_NAME);

    final Mono<ResultValue> sum = mapResult(engine.sum(anyOf(instruments, in(INSTRUMENT3.name()))),
        INSTRUMENT3, SUM_FUNCTION_NAME);

    final Mono<ResultValue> sumTopTen = mapResult(engine.sumTop10(
        anyOf(instruments, notIn(INSTRUMENT1.name(), INSTRUMENT2.name(), INSTRUMENT3.name()))),
        null, TOP_NEWEST_SUM_FUNCTION_NAME);

    Flux.just(mean1, mean2, sum, sumTopTen)
        // .parallel().runOn(Schedulers.parallel())
        .doOnNext(
            v -> v.subscribe(r -> log.info(String.format("Memory in use while reading: %dMB\n",
                (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)) + r))).subscribe()
        .dispose();


  }

  public static Mono<ResultValue> mapResult(Mono<?> result, InstrumentType instrument,
      String functionName) {
    String name = (instrument == null) ? "ANY" : instrument.name();
    return result.map(v -> ResultValue.of(name, functionName, v));
  }


}



