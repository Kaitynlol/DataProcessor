package calculation.impl;

import calculation.Engine;
import java.util.Comparator;
import java.util.PriorityQueue;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.InstrumentDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.math.MathFlux;

@NoArgsConstructor
@Slf4j
public class DefaultEngineImpl implements Engine {

  @Override
  public Mono<Double> mean(Flux<InstrumentDto> instruments) {
    return instruments
        //.parallel()
        // .runOn(Schedulers.parallel())
        .map(InstrumentDto::getPrice)
        .doOnNext(v -> log.debug("mean " + v.toString()))
        .as(MathFlux::averageDouble);
  }

  @Override
  public Mono<Double> sum(Flux<InstrumentDto> instruments) {
    return instruments
        // .parallel()
        // .runOn(Schedulers.parallel())
        .doOnNext(v -> log.debug("sum " + v.toString()))
        .map(InstrumentDto::getPrice)
        .as(MathFlux::sumDouble);
  }

  @Override
  public Mono<Double> sumTop10(Flux<InstrumentDto> instruments) {
    PriorityQueue<InstrumentDto> accumulator = new PriorityQueue<>(
        Comparator.comparing(InstrumentDto::getDate));
    return instruments
        .reduce(accumulator, (acc, price) -> {
          acc.offer(price);
          if (acc.size() > 10) {
            acc.poll();
          }
          return acc;
        })
        .doOnNext(v -> log.debug("SumAll: " + v.toString()))
        .flatMap(q -> Mono.just(q.stream().mapToDouble(v -> v.getPrice().doubleValue()).sum()));
  }
}
