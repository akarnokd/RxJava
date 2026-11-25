/*
 * Copyright (c) 2016-present, RxJava Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package io.reactivex.rxjava4.internal.subscriptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import io.reactivex.rxjava4.core.RxJavaTest;
import io.reactivex.rxjava4.testsupport.TestHelper;

@Isolated
public class SubscriptionArbiterIsolatedTest extends RxJavaTest {

    @Test
    public void cancelWhileSwappingRace() {
        for (int i = 0; i < TestHelper.RACE_DEFAULT_LOOPS; i++) {
            SubscriptionArbiter sa = new SubscriptionArbiter(false);

            BooleanSubscription sub1 = new BooleanSubscription();
            BooleanSubscription sub2 = new BooleanSubscription();

            sa.setSubscription(sub1);
            sa.request(1);

            Runnable r1 = () -> sa.setSubscription(sub2);
            Runnable r2 = () -> sa.cancel();

            TestHelper.race(r1, r2);

            assertTrue(sub2.isCancelled(), "sub2 was not cancelled #" + i);
        }
    }
}
