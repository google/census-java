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

package io.opencensus.exporter.stats.stackdriver;

import com.google.api.MonitoredResource;
import com.google.auth.Credentials;
import com.google.auto.value.AutoValue;
import io.opencensus.common.Duration;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Configurations for {@link StackdriverStatsExporter}.
 *
 * @since 0.11
 */
@AutoValue
@Immutable
public abstract class StackdriverStatsConfiguration {

  StackdriverStatsConfiguration() {}

  /**
   * Returns the {@link Credentials}.
   *
   * @return the {@code Credentials}.
   * @since 0.11
   */
  @Nullable
  public abstract Credentials getCredentials();

  /**
   * Returns the project id.
   *
   * @return the project id.
   * @since 0.11
   */
  @Nullable
  public abstract String getProjectId();

  /**
   * Returns the export interval between pushes to StackDriver.
   *
   * @return the export interval.
   * @since 0.11
   */
  @Nullable
  public abstract Duration getExportInterval();

  /**
   * Returns the Stackdriver {@link MonitoredResource}.
   *
   * @return the {@code MonitoredResource}.
   * @since 0.11
   */
  @Nullable
  public abstract MonitoredResource getMonitoredResource();

  /**
   * Returns {@code true} when you want to force update existing {@link
   * com.google.api.MetricDescriptor}, returns {@code false} or {@code null} otherwise.
   *
   * <p>Force update means delete the existing {@link com.google.api.MetricDescriptor} and create a
   * new one.
   *
   * @return {@code true} when you want to force update existing {@link
   *     com.google.api.MetricDescriptor}, returns {@code false} or {@code null} otherwise.
   * @since 0.15
   */
  @Nullable
  public abstract Boolean getOverrideExistingMetrics();

  /**
   * Returns a new {@link Builder}.
   *
   * @return a {@code Builder}.
   * @since 0.11
   */
  public static Builder builder() {
    return new AutoValue_StackdriverStatsConfiguration.Builder();
  }

  /**
   * Builder for {@link StackdriverStatsConfiguration}.
   *
   * @since 0.11
   */
  @AutoValue.Builder
  public abstract static class Builder {

    Builder() {}

    /**
     * Sets the given {@link Credentials}.
     *
     * @param credentials the {@code Credentials}.
     * @return this.
     * @since 0.11
     */
    public abstract Builder setCredentials(Credentials credentials);

    /**
     * Sets the given project id.
     *
     * @param projectId the cloud project id.
     * @return this.
     * @since 0.11
     */
    public abstract Builder setProjectId(String projectId);

    /**
     * Sets the export interval.
     *
     * @param exportInterval the export interval between pushes to StackDriver.
     * @return this.
     * @since 0.11
     */
    public abstract Builder setExportInterval(Duration exportInterval);

    /**
     * Sets the {@link MonitoredResource}.
     *
     * @param monitoredResource the Stackdriver {@code MonitoredResource}.
     * @return this.
     * @since 0.11
     */
    public abstract Builder setMonitoredResource(MonitoredResource monitoredResource);

    /**
     * Decides whether to force update existing {@link com.google.api.MetricDescriptor} or not.
     * Force update means delete the existing {@link com.google.api.MetricDescriptor} and create a
     * new one.
     *
     * <p>Note that once a {@link com.google.api.MetricDescriptor} is deleted from Stackdriver, all
     * the {@link com.google.monitoring.v3.TimeSeries} associated with it will also be deleted.
     *
     * @param shouldOverrideExistingMetrics whether to force update existing {@link
     *     com.google.api.MetricDescriptor} or not.
     * @return this
     */
    public abstract Builder setOverrideExistingMetrics(Boolean shouldOverrideExistingMetrics);

    /**
     * Builds a new {@link StackdriverStatsConfiguration} with current settings.
     *
     * @return a {@code StackdriverStatsConfiguration}.
     * @since 0.11
     */
    public abstract StackdriverStatsConfiguration build();
  }
}
