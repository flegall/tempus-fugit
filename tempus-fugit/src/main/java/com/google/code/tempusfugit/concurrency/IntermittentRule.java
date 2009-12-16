/*
 * Copyright (c) 2009, tempus-fugit committers
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

package com.google.code.tempusfugit.concurrency;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class IntermittentRule implements MethodRule {

    private final Repeat repeat;

    public IntermittentRule(Repeat repeat) {
        this.repeat = repeat;
    }

    public Statement apply(Statement base, final FrameworkMethod method, final Object target) {
        return new Statement() {
            public void evaluate() throws Throwable {
                if (intermittent(method))
                    for (int i = 0; i < repeat.count; i++, method.invokeExplosively(target)) ;
                else
                    method.invokeExplosively(target);
            }
        };
    }

    private boolean intermittent(FrameworkMethod method) {
        return method.getAnnotation(Intermittent.class) != null;
    }

    static class Repeat {
        private final int count;

        private Repeat(int count) {
            this.count = count;
        }

        public static Repeat repeat(int count) {
            return new Repeat(count);
        }

    }
}