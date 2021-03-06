/*
 * Copyright (c) 2009-2012, toby weston & tempus-fugit committers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.tempusfugit.temporal;

import org.hamcrest.Matchers;
import org.junit.Test;

import static com.google.code.tempusfugit.temporal.Duration.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public final class DurationTest {

    private static final long HOURS_IN_A_DAY = 24;
    private static final long MINUTES_IN_AN_HOUR = 60;
    private static final long SECONDS_IN_A_MINUTE = 60;
    private static final long MILLIS_IN_A_SECOND = SECONDS.toMillis(1);

    @Test
    public void convertSeconds() {
        Duration duration = seconds(60);
        assertThat(duration.inMillis(), is(60000L));
        assertThat(duration.inSeconds(), is(60L));
        assertThat(duration.inMinutes(), is(1L));
        assertThat(duration.inHours(), is(0L));
        assertThat(duration.inDays(), is(0L));
    }

    @Test
    public void convertSecondsRoundingResult() {
        Duration duration = seconds(95);
        assertThat(duration.inMillis(), is(95000L));
        assertThat(duration.inSeconds(), is(95L));
        assertThat(duration.inMinutes(), is(1L));
        assertThat(duration.inHours(), is(0L));
        assertThat(duration.inDays(), is(0L));
    }

    @Test
    public void convertMinutes() {
        Duration duration = minutes(50);
        assertThat(duration.inMillis(), is(3000000L));
        assertThat(duration.inSeconds(), is(3000L));
        assertThat(duration.inMinutes(), is(50L));
        assertThat(duration.inHours(), is(0L));
        assertThat(duration.inDays(), is(0L));
    }

    @Test
    public void conversionHours() {
        Duration duration = hours(24);
        assertThat(duration.inMillis(), is(86400000L));
        assertThat(duration.inSeconds(), is(86400L));
        assertThat(duration.inMinutes(), is(1440L));
        assertThat(duration.inHours(), is(24L));
        assertThat(duration.inDays(), is(1L));
    }

    @Test
    public void convertDays() {
        Duration duration = Duration.days(10L);
        assertThat(duration.inDays(), is(10L));
        assertThat(duration.inHours(), is(duration.inDays() * HOURS_IN_A_DAY));
        assertThat(duration.inMinutes(), is(duration.inHours() * MINUTES_IN_AN_HOUR));
        assertThat(duration.inSeconds(), is(duration.inMinutes() * SECONDS_IN_A_MINUTE));
        assertThat(duration.inMillis(), is(duration.inSeconds() * MILLIS_IN_A_SECOND));
    }

    @Test
    public void canAddDurations() {
        assertThat(days(1).plus(days(1)), is(equalTo(days(2))));
        assertThat(millis(1).plus(seconds(1)), is(equalTo(millis(1001))));
    }

    @Test
    public void canSubtractDurations() {
        assertThat(days(1).minus(days(1)), is(equalTo(days(0))));
        assertThat(seconds(1).minus(millis(1)), is(equalTo(millis(999))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooManySeconds() {
        seconds(Long.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooManyMillis() {
        millis(Long.MAX_VALUE);
    }

    @Test
    public void sameUnitEquality() {
        assertThat(seconds(10), is(equalTo(seconds(10))));
    }

    @Test
    public void differentUnitEquality() {
        assertThat(millis(10000), is(equalTo(seconds(10))));
        assertThat(seconds(10), is(equalTo(millis(10000))));
    }

    @Test
    public void differntUnitNotEqual() {
        assertThat(millis(1500), is(not(equalTo(seconds(2)))));
    }

    @Test
    public void sameUnitNotEqual() {
        assertThat(seconds(1), is(not(equalTo(seconds(2)))));
    }

    @Test
    public void hashcode() {
        assertThat(seconds(1).hashCode(), is(equalTo(millis(1000).hashCode())));
    }

    @Test
    public void greaterThan() {
        assertThat(seconds(1).greaterThan(seconds(2)), is(false));
        assertThat(seconds(2).greaterThan(seconds(1)), is(true));
        assertThat(seconds(1).greaterThan(seconds(1)), is(false));
        assertThat(seconds(-1).greaterThan(seconds(1)), is(false));
    }

    @Test
    public void lessThan() {
        assertThat(seconds(1).lessThan(seconds(2)), is(true));
        assertThat(seconds(2).lessThan(seconds(1)), is(false));
        assertThat(seconds(1).lessThan(seconds(1)), is(false));
        assertThat(seconds(-1).lessThan(seconds(1)), is(true));
    }

    @Test
    public void equalityNull() {
        assertThat(seconds(1).equals(null), is(false));
    }

    @Test
    public void equalityDifferentClass() {
        assertThat(seconds(10).equals(""), is(false));
    }

    @Test
    public void toStringOnDuration() {
        assertThat(millis(1099).toString(), is(equalTo("Duration 1099 MILLISECONDS")));
        assertThat(seconds(1).toString(), is(equalTo("Duration 1 SECONDS")));
        assertThat(minutes(60).toString(), is(equalTo("Duration 3600 SECONDS")));
        assertThat(hours(24).toString(), is(equalTo("Duration 86400 SECONDS")));
        assertThat(days(1).toString(), is(equalTo("Duration 86400 SECONDS")));
    }

    @Test
    public void shouldCompareToGreater() {
        assertThat(seconds(10).compareTo(seconds(9)), is(1));
    }

    @Test
    public void shouldCompareToLessThan() {
        assertThat(seconds(9).compareTo(seconds(10)), is(-1));
    }

    @Test
    public void shouldCompareToLessEquals() {
        assertThat(seconds(10).compareTo(seconds(10)), is(0));
    }
    
    @Test
    public void shouldDemonstrateCompareToWithAMatcherAndWithout() {
        assertThat(seconds(10), is(Matchers.greaterThan(seconds(9))));
        assertThat(seconds(10).greaterThan(seconds(9)), is(true));
    }
    
}
