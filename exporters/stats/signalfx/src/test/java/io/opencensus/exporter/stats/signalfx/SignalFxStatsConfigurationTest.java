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

package io.opencensus.exporter.stats.signalfx;

import static org.junit.Assert.assertEquals;

import io.opencensus.common.Duration;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link SignalFxStatsConfiguration}. */
@RunWith(JUnit4.class)
public class SignalFxStatsConfigurationTest {

  private static final String TEST_TOKEN = "token";

  @Test
  public void testBuildDefaults() {
    SignalFxStatsConfiguration configuration =
        SignalFxStatsConfiguration.builder().setToken(TEST_TOKEN).build();
    assertEquals(TEST_TOKEN, configuration.getToken());
    assertEquals(
        SignalFxStatsConfiguration.DEFAULT_SIGNALFX_ENDPOINT,
        configuration.getIngestEndpoint().toString());
    assertEquals(
        SignalFxStatsConfiguration.DEFAULT_EXPORT_INTERVAL, configuration.getExportInterval());
  }

  @Test
  public void testBuildFields() throws URISyntaxException {
    URI url = new URI("http://example.com");
    Duration duration = Duration.create(5, 0);
    SignalFxStatsConfiguration configuration =
        SignalFxStatsConfiguration.builder()
            .setToken(TEST_TOKEN)
            .setIngestEndpoint(url)
            .setExportInterval(duration)
            .build();
    assertEquals(TEST_TOKEN, configuration.getToken());
    assertEquals(url, configuration.getIngestEndpoint());
    assertEquals(duration, configuration.getExportInterval());
  }

  @Test
  public void testSameConfigurationsAreEqual() {
    SignalFxStatsConfiguration config1 =
        SignalFxStatsConfiguration.builder().setToken(TEST_TOKEN).build();
    SignalFxStatsConfiguration config2 =
        SignalFxStatsConfiguration.builder().setToken(TEST_TOKEN).build();
    assertEquals(config1, config2);
    assertEquals(config1.hashCode(), config2.hashCode());
  }
}
