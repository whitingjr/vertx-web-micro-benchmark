package io.vertx.ext.web.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import io.vertx.ext.web.ParsedHeaderValue;

@Warmup(iterations = 20, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Threads(1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = {
    "-XX:+UseBiasedLocking",
    "-XX:BiasedLockingStartupDelay=0",
})
@State(Scope.Thread)
public class HeaderParserBenchmark {

  @Benchmark
  public void baseline() throws Exception {
    consume(HeaderParser.convertToParsedHeaderValues(EMPTY, ParsableHeaderValue::new));
  }

  @Benchmark
  public void plaintext() throws Exception{
    consume(HeaderParser.convertToParsedHeaderValues(PLAINTEXT, ParsableHeaderValue::new));
  }

  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public static void consume(List<ParsedHeaderValue> parts) {
  }

  private static final String EMPTY = "";
  private static final String PLAINTEXT = "text/plain";

}
