/*
 * Copyright 2017, OpenCensus Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opencensus.trace.propagation;

import static com.google.common.base.Preconditions.checkNotNull;

import io.opencensus.common.ExperimentalApi;
import io.opencensus.trace.SpanContext;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

/**
 * Injects and extracts {@link SpanContext trace identifiers} as text into carriers that travel
 * in-band across process boundaries. Identifiers are often encoded as messaging or RPC request
 * headers.
 *
 * <p>When using http, the carrier of propagated data on both the client (injector) and server
 * (extractor) side is usually an http request. Propagation is usually implemented via library-
 * specific request interceptors, where the client-side injects span identifiers and the server-side
 * extracts them.
 *
 * <p>Example of usage on the client:
 *
 * <pre>{@code
 * private static final Tracer tracer = Tracing.getTracer();
 * private static final TextFormat textFormat = Tracing.getPropagationComponent().getTextFormat();
 * private static final TextFormat.Setter setter = new TextFormat.Setter<HttpURLConnection>() {
 *   public void put(HttpURLConnection carrier, String key, String value, Encodings.B3_FORMAT) {
 *     carrier.setRequestProperty(field, value);
 *   }
 * }
 *
 * void makeHttpRequest() {
 *   Span span = tracer.spanBuilder("Sent.MyRequest").startSpan();
 *   try (Scope s = tracer.withSpan(span)) {
 *     HttpURLConnection connection =
 *         (HttpURLConnection) new URL("http://myserver").openConnection();
 *     textFormat.inject(span.getContext(), connection, httpURLConnectionSetter);
 *     // Send the request, wait for response and maybe set the status if not ok.
 *   }
 *   span.end();  // Can set a status.
 * }
 * }</pre>
 *
 * <p>Example of usage on the server:
 *
 * <pre>{@code
 * private static final Tracer tracer = Tracing.getTracer();
 * private static final TextFormat textFormat = Tracing.getPropagationComponent().getTextFormat();
 * private static final TextFormat.Getter getter = ...;
 *
 * void onRequestReceived() {
 *   SpanContext spanContext = textFormat.extract(carrier, getter, Encodings.ALL);
 *   Span span = tracer.spanBuilderWithRemoteParent("Recv.MyRequest", spanContext).startSpan();
 *   try (Scope s = tracer.withSpan(span)) {
 *     // Handle request and send response back.
 *   }
 *   span.end()
 * }
 * }</pre>
 */
@ExperimentalApi
public abstract class TextFormat {
  private static final NoopTextFormat NOOP_TEXT_FORMAT = new NoopTextFormat();

  public enum Encoding {
    /**
     * If used in the {@link #extract(Object, Getter, Encoding)} API the implementation will try to
     * extract the headers from the carrier in the following order: TBD. If used in the
     * {@link #inject(SpanContext, Object, Setter, Encoding)} then all available
     * encodings will be used.
     */
    ALL,

    // TODO(bdrutu): Add all the supported encodings here.
  }

  /**
   * The propagation fields defined. If your carrier is reused, you should delete the fields here
   * before calling {@link #inject(SpanContext, Object, Setter, Encoding)}.
   *
   * <p>For example, if the carrier is a single-use or immutable request object, you don't need to
   * clear fields as they couldn't have been set before. If it is a mutable, retryable object,
   * successive calls should clear these fields first.
   */
  // The use cases of this are:
  // * allow pre-allocation of fields, especially in systems like gRPC Metadata
  // * allow a single-pass over an iterator (ex OpenTracing has no getter in TextMap)
  public abstract List<String> fields();

  /**
   * Injects the span context downstream. For example, as http headers.
   *
   * @param spanContext possibly not sampled.
   * @param carrier holds propagation fields. For example, an outgoing message or http request.
   * @param setter invoked for each propagation key to add or remove.
   * @param encoding the encoding to be used to inject the {@code SpanContext} to the carrier.
   */
  public abstract <C> void inject(
      SpanContext spanContext, C carrier, Setter<C> setter, Encoding encoding);

  /**
   * Class that allows to be saved as a constant to avoid runtime allocations.
   *
   * @param <C> carrier of propagation fields, such as an http request
   */
  public abstract static class Setter<C> {

    /**
     * Replaces a propagated field with the given value.
     *
     * <p>For example, a setter for an {@link java.net.HttpURLConnection} would be the method
     * reference {@link java.net.HttpURLConnection#addRequestProperty(String, String)}
     *
     * @param carrier holds propagation fields. For example, an outgoing message or http request.
     * @param key the key of the field.
     * @param value the value of the field.
     */
    public abstract void put(C carrier, String key, String value);
  }

  /**
   * Extracts the span context from upstream. For example, as http headers.
   * 
   * <p>If no particular format is known to be always received on from the wire it is recommended
   * to use {@code Encoding.ALL}.
   *
   * @param carrier holds propagation fields. For example, an outgoing message or http request.
   * @param getter invoked for each propagation key to get.
   * @param encoding the encoding to be used to extract the {@code SpanContext} from the carrier.
   * @throws SpanContextParseException if the input is invalid
   */
  public abstract <C> SpanContext extract(C carrier, Getter<C> getter, Encoding encoding)
      throws SpanContextParseException;

  /**
   * Class that allows to be saved as a constant to avoid runtime allocations.
   *
   * @param <C> carrier of propagation fields, such as an http request
   */
  public abstract static class Getter<C> {

    /**
     * Returns the first value of the given propagation {@code key} or returns {@code null}.
     *
     * @param carrier carrier of propagation fields, such as an http request
     * @param key the key of the field.
     * @return the first value of the given propagation {@code key} or returns {@code null}.
     */
    @Nullable
    public abstract String get(C carrier, String key);
  }

  /**
   * Returns the no-op implementation of the {@code TextFormat}.
   *
   * @return the no-op implementation of the {@code TextFormat}.
   */
  static TextFormat getNoopTextFormat() {
    return NOOP_TEXT_FORMAT;
  }

  private static final class NoopTextFormat extends TextFormat {

    private NoopTextFormat() {}

    @Override
    public List<String> fields() {
      return Collections.emptyList();
    }

    @Override
    public <C> void inject(
        SpanContext spanContext, C carrier, Setter<C> setter, Encoding encoding) {
      checkNotNull(spanContext, "spanContext");
      checkNotNull(setter, "setter");
      checkNotNull(encoding, "encoding");
    }

    @Override
    public <C> SpanContext extract(C carrier, Getter<C> getter, Encoding encoding) {
      checkNotNull(getter, "getter");
      checkNotNull(encoding, "encoding");
      return SpanContext.INVALID;
    }
  }
}
