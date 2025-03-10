/*
 * Copyright 2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.smallrye.faulttolerance.config.priority;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.temporal.ChronoUnit;

import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import io.smallrye.faulttolerance.FaultToleranceOperations;
import io.smallrye.faulttolerance.config.FaultToleranceOperation;
import io.smallrye.faulttolerance.util.FaultToleranceBasicTest;
import io.smallrye.faulttolerance.util.WithSystemProperty;

@WithSystemProperty(key = "Retry/delay", value = "10")
@FaultToleranceBasicTest
@AddBeanClasses(FaultyService.class)
public class ConfigParameterPriorityTest {
    @Inject
    FaultToleranceOperations ops;

    @Test
    public void testConfig() throws NoSuchMethodException, SecurityException {
        FaultToleranceOperation foo = ops.get(FaultyService.class, FaultyService.class.getMethod("foo"));
        assertThat(foo).isNotNull();
        assertThat(foo.hasRetry()).isTrue();

        Retry fooRetry = foo.getRetry();
        // Global override
        assertThat(fooRetry.delay()).isEqualTo(10L);
        // Method-level
        assertThat(fooRetry.maxRetries()).isEqualTo(2);
        // Default value
        assertThat(fooRetry.delayUnit()).isEqualTo(ChronoUnit.MILLIS);

        FaultToleranceOperation bar = ops.get(FaultyService.class, FaultyService.class.getMethod("bar"));
        assertThat(bar).isNotNull();
        assertThat(bar.hasRetry()).isTrue();

        Retry barRetry = bar.getRetry();
        // Global override
        assertThat(barRetry.delay()).isEqualTo(10L);
        // Class-level
        assertThat(barRetry.maxRetries()).isEqualTo(1);
        // Default value
        assertThat(fooRetry.delayUnit()).isEqualTo(ChronoUnit.MILLIS);
    }
}
