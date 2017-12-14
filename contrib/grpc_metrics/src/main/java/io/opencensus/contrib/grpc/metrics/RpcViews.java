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

package io.opencensus.contrib.grpc.metrics;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import io.opencensus.stats.Stats;
import io.opencensus.stats.View;
import io.opencensus.stats.ViewManager;

/** Helper class that allows users to register rpc views easily. */
public final class RpcViews {
  @VisibleForTesting
  static final ImmutableSet<View> RPC_CUMULATIVE_VIEWS_SET =
      ImmutableSet.of(
          RpcViewConstants.RPC_CLIENT_ERROR_COUNT_VIEW,
          RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW,
          RpcViewConstants.RPC_CLIENT_REQUEST_BYTES_VIEW,
          RpcViewConstants.RPC_CLIENT_RESPONSE_BYTES_VIEW,
          RpcViewConstants.RPC_CLIENT_REQUEST_COUNT_VIEW,
          RpcViewConstants.RPC_CLIENT_RESPONSE_COUNT_VIEW,
          RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_REQUEST_BYTES_VIEW,
          RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_RESPONSE_BYTES_VIEW,
          RpcViewConstants.RPC_CLIENT_SERVER_ELAPSED_TIME_VIEW,
          RpcViewConstants.RPC_SERVER_ERROR_COUNT_VIEW,
          RpcViewConstants.RPC_SERVER_SERVER_LATENCY_VIEW,
          RpcViewConstants.RPC_SERVER_SERVER_ELAPSED_TIME_VIEW,
          RpcViewConstants.RPC_SERVER_REQUEST_BYTES_VIEW,
          RpcViewConstants.RPC_SERVER_RESPONSE_BYTES_VIEW,
          RpcViewConstants.RPC_SERVER_REQUEST_COUNT_VIEW,
          RpcViewConstants.RPC_SERVER_RESPONSE_COUNT_VIEW,
          RpcViewConstants.RPC_SERVER_UNCOMPRESSED_REQUEST_BYTES_VIEW,
          RpcViewConstants.RPC_SERVER_UNCOMPRESSED_RESPONSE_BYTES_VIEW);

  @VisibleForTesting
  static final ImmutableSet<View> RPC_INTERVAL_VIEWS_SET =
      ImmutableSet.of(
          RpcViewConstants.RPC_CLIENT_ERROR_COUNT_MINUTE_VIEW,
          RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_MINUTE_VIEW,
          RpcViewConstants.RPC_CLIENT_REQUEST_BYTES_MINUTE_VIEW,
          RpcViewConstants.RPC_CLIENT_RESPONSE_BYTES_MINUTE_VIEW,
          RpcViewConstants.RPC_CLIENT_REQUEST_COUNT_MINUTE_VIEW,
          RpcViewConstants.RPC_CLIENT_RESPONSE_COUNT_MINUTE_VIEW,
          RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_REQUEST_BYTES_MINUTE_VIEW,
          RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_RESPONSE_BYTES_MINUTE_VIEW,
          RpcViewConstants.RPC_CLIENT_SERVER_ELAPSED_TIME_MINUTE_VIEW,
          RpcViewConstants.RPC_SERVER_ERROR_COUNT_MINUTE_VIEW,
          RpcViewConstants.RPC_SERVER_SERVER_LATENCY_MINUTE_VIEW,
          RpcViewConstants.RPC_SERVER_SERVER_ELAPSED_TIME_MINUTE_VIEW,
          RpcViewConstants.RPC_SERVER_REQUEST_BYTES_MINUTE_VIEW,
          RpcViewConstants.RPC_SERVER_RESPONSE_BYTES_MINUTE_VIEW,
          RpcViewConstants.RPC_SERVER_REQUEST_COUNT_MINUTE_VIEW,
          RpcViewConstants.RPC_SERVER_RESPONSE_COUNT_MINUTE_VIEW,
          RpcViewConstants.RPC_SERVER_UNCOMPRESSED_REQUEST_BYTES_MINUTE_VIEW,
          RpcViewConstants.RPC_SERVER_UNCOMPRESSED_RESPONSE_BYTES_MINUTE_VIEW,
          RpcViewConstants.RPC_CLIENT_ERROR_COUNT_HOUR_VIEW,
          RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_HOUR_VIEW,
          RpcViewConstants.RPC_CLIENT_REQUEST_BYTES_HOUR_VIEW,
          RpcViewConstants.RPC_CLIENT_RESPONSE_BYTES_HOUR_VIEW,
          RpcViewConstants.RPC_CLIENT_REQUEST_COUNT_HOUR_VIEW,
          RpcViewConstants.RPC_CLIENT_RESPONSE_COUNT_HOUR_VIEW,
          RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_REQUEST_BYTES_HOUR_VIEW,
          RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_RESPONSE_BYTES_HOUR_VIEW,
          RpcViewConstants.RPC_CLIENT_SERVER_ELAPSED_TIME_HOUR_VIEW,
          RpcViewConstants.RPC_SERVER_ERROR_COUNT_HOUR_VIEW,
          RpcViewConstants.RPC_SERVER_SERVER_LATENCY_HOUR_VIEW,
          RpcViewConstants.RPC_SERVER_SERVER_ELAPSED_TIME_HOUR_VIEW,
          RpcViewConstants.RPC_SERVER_REQUEST_BYTES_HOUR_VIEW,
          RpcViewConstants.RPC_SERVER_RESPONSE_BYTES_HOUR_VIEW,
          RpcViewConstants.RPC_SERVER_REQUEST_COUNT_HOUR_VIEW,
          RpcViewConstants.RPC_SERVER_RESPONSE_COUNT_HOUR_VIEW,
          RpcViewConstants.RPC_SERVER_UNCOMPRESSED_REQUEST_BYTES_HOUR_VIEW,
          RpcViewConstants.RPC_SERVER_UNCOMPRESSED_RESPONSE_BYTES_HOUR_VIEW);

  /**
   * Registers all standard cumulative views.
   *
   * <p>This must be called once at the beginning of the process.
   *
   * @since 0.11.0
   */
  public static void registerCumulativeViews() {
    registerCumulativeViews(Stats.getViewManager());
  }

  @VisibleForTesting
  static void registerCumulativeViews(ViewManager viewManager) {
    for (View view : RPC_CUMULATIVE_VIEWS_SET) {
      viewManager.registerView(view);
    }
  }

  /**
   * Registers all standard interval views.
   *
   * <p>This must be called once at the beginning of the process.
   *
   * @since 0.11.0
   */
  public static void registerIntervalViews() {
    registerIntervalViews(Stats.getViewManager());
  }

  @VisibleForTesting
  static void registerIntervalViews(ViewManager viewManager) {
    for (View view : RPC_INTERVAL_VIEWS_SET) {
      viewManager.registerView(view);
    }
  }

  /**
   * Registers all views.
   *
   * <p>This must be called once at the beginning of the process.
   *
   * @since 0.11.0
   */
  public static void registerAllViews() {
    registerAllViews(Stats.getViewManager());
  }

  @VisibleForTesting
  static void registerAllViews(ViewManager viewManager) {
    registerCumulativeViews(viewManager);
    registerIntervalViews(viewManager);
  }

  private RpcViews() {}
}
