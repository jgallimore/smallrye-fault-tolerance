package io.smallrye.faulttolerance.core.rate.limit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.smallrye.faulttolerance.core.stopwatch.TestStopwatch;

public class SmoothWindowTest {
    private TestStopwatch stopwatch;

    @BeforeEach
    public void setUp() {
        stopwatch = new TestStopwatch();
    }

    @Test
    public void scenario1() {
        TimeWindow window = new SmoothWindow(stopwatch, 2, 100, 0);

        // 0
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(50);

        // 50
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(100);

        // 100
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();
    }

    @Test
    public void scenario2() {
        TimeWindow window = new SmoothWindow(stopwatch, 2, 100, 0);

        // 0
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(50);

        // 50
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(120);

        // 120
        assertThat(window.record()).isTrue();

        stopwatch.setCurrentValue(190);

        // 190
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(210);

        // 210
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(290);

        // 290
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();
    }

    @Test
    public void scenario3() {
        TimeWindow window = new SmoothWindow(stopwatch, 4, 100, 0);

        // 0
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(50);

        // 50
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(100);

        // 100
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(200);

        // 200
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();
    }

    @Test
    public void scenario4() {
        TimeWindow window = new SmoothWindow(stopwatch, 4, 100, 0);

        // 0
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(50);

        // 50
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(75);

        // 75
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(100);

        // 100
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(200);

        // 200
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();
    }

    @Test
    public void scenario5() {
        TimeWindow window = new SmoothWindow(stopwatch, 4, 100, 5);

        // 0
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(10);

        // 10
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(13);

        // 13
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(50);

        // 50
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(100);

        // 100
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(120);

        // 120
        assertThat(window.record()).isTrue();

        stopwatch.setCurrentValue(130);

        // 130
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();
    }

    @Test
    public void scenario6() {
        TimeWindow window = new SmoothWindow(stopwatch, 2, 100, 0);

        // 0
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(550);

        // 550
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(1050);

        // 1050
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isTrue();
        assertThat(window.record()).isFalse();
    }

    @Test
    public void scenario7() {
        TimeWindow window = new SmoothWindow(stopwatch, 4, 100, 5);

        // 0
        assertThat(window.record()).isTrue();

        stopwatch.setCurrentValue(25);

        // 25
        assertThat(window.record()).isTrue();

        stopwatch.setCurrentValue(28);

        // 28
        assertThat(window.record()).isFalse();

        stopwatch.setCurrentValue(50);

        // 50
        assertThat(window.record()).isTrue();

        stopwatch.setCurrentValue(100);

        // 100
        assertThat(window.record()).isTrue();

        stopwatch.setCurrentValue(123);

        // 123
        assertThat(window.record()).isTrue();

        stopwatch.setCurrentValue(130);

        // 130
        assertThat(window.record()).isTrue();
    }
}
