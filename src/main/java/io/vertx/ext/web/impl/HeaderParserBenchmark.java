package io.vertx.ext.web.impl;

import java.util.HashMap;
import java.util.Map;
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

@Warmup(iterations = 20, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Threads(1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = {
})
@State(Scope.Thread)
public class HeaderParserBenchmark {

  @Benchmark
  public void baseline() throws Exception {
    consume(fillParsedHeaders(baselineheaders));
  }

  @Benchmark
  public void plaintext() throws Exception{
     consume(fillParsedHeaders(plaintextheaders));
  }

  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public static void consume(ParsableHeaderValuesContainer container) {
  }

  private ParsableHeaderValuesContainer fillParsedHeaders(Map<String, String> h) {
    String accept = h.get("Accept");
    String acceptCharset = h.get ("Accept-Charset");
    String acceptEncoding = h.get("Accept-Encoding");
    String acceptLanguage = h.get("Accept-Language");
    String contentType = ensureNotNull(h.get("Content-Type"));

    parsedHeaders = new ParsableHeaderValuesContainer(
        HeaderParser.sort(HeaderParser.convertToParsedHeaderValues(accept, ParsableMIMEValue::new)),
        HeaderParser.sort(HeaderParser.convertToParsedHeaderValues(acceptCharset, ParsableHeaderValue::new)),
        HeaderParser.sort(HeaderParser.convertToParsedHeaderValues(acceptEncoding, ParsableHeaderValue::new)),
        HeaderParser.sort(HeaderParser.convertToParsedHeaderValues(acceptLanguage, ParsableLanguageValue::new)),
        new ParsableMIMEValue(contentType)
    );
    return parsedHeaders;
  }

  private String ensureNotNull(String string){
     return string == null ? "" : string;
   }

  private static final Map<String, String> baselineheaders = new HashMap<String, String>();
  private static final Map<String, String> plaintextheaders = new HashMap<String, String>();
  private ParsableHeaderValuesContainer parsedHeaders;

  static {
    plaintextheaders.put("Accept", "text/plain");
  }
}
