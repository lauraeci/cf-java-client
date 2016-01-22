/*
 * Copyright 2013-2016 the original author or authors.
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

package org.cloudfoundry;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.domains.DomainResource;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.util.v2.Resources;
import org.cloudfoundry.utils.test.TestSubscriber;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Mono;
import reactor.fn.Predicate;
import reactor.fn.tuple.Tuple2;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.cloudfoundry.operations.util.Tuples.function;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IntegrationTestConfiguration.class)
public abstract class AbstractIntegrationTest {

    private final Logger logger = LoggerFactory.getLogger("test");

    @Rule
    public final TestName testName = new TestName();

    private final TestSubscriber<?> testSubscriber = new TestSubscriber<>()
            .setScanningLoggerName(() -> String.format("%s.%s", this.getClass().getSimpleName(), AbstractIntegrationTest.this.testName.getMethodName()))
            .setPerformanceLoggerName(() -> String.format("%s.%s", this.getClass().getSimpleName(), AbstractIntegrationTest.this.testName.getMethodName()));

    @Autowired
    protected CloudFoundryClient cloudFoundryClient;

    @Autowired
    protected CloudFoundryOperations cloudFoundryOperations;

    @Autowired
    protected Mono<String> organizationId;

    @Autowired
    protected Mono<String> spaceId;

    @Autowired
    protected Mono<String> stackId;

    @Value("${test.space}")
    protected String testSpaceName;

    @Value("${test.username}")
    protected String testUsername;

    @Autowired
    private Predicate<DomainResource> domainsPredicate;

    @Before
    public final void cleanup() throws Exception {
        Mono
                .when(this.organizationId, this.spaceId)
                .flatMap(function((organizationId, spaceId) -> {
                    return CloudFoundryCleaner.clean(this.cloudFoundryClient,
                            r -> true,
                            this.domainsPredicate,
                            r -> !organizationId.equals(Resources.getId(r)),
                            r -> true,
                            r -> !spaceId.equals(Resources.getId(r)));
                }))
                .doOnSubscribe(s -> this.logger.debug(">> CLEANUP <<"))
                .doOnComplete(() -> this.logger.debug("<< CLEANUP >>"))
                .after()
                .get();

    }

    @After
    public final void verify() throws InterruptedException {
        this.testSubscriber.verify(1, MINUTES);
    }

    protected final <T> void assertTupleEquality(Tuple2<T, T> tuple) {
        T actual = tuple.t1;
        T expected = tuple.t2;

        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    protected <T> TestSubscriber<T> testSubscriber() {
        return (TestSubscriber<T>) this.testSubscriber;
    }

}
