/*
 * Copyright 2013-2018 the original author or authors.
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

package org.cloudfoundry.client.v2.routes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cloudfoundry.Nullable;
import org.cloudfoundry.client.v2.domains.Domain;
import org.immutables.value.Value;

/**
 * A route bound to an application
 */
@JsonDeserialize
@Value.Immutable
abstract class _Route {

    /**
     * The domain
     */
    @JsonProperty("domain")
    @Nullable
    abstract Domain getDomain();

    /**
     * The host
     */
    @JsonProperty("host")
    @Nullable
    abstract String getHost();

    /**
     * The id
     */
    @JsonProperty("guid")
    @Nullable
    abstract String getId();

    /**
     * The path
     */
    @JsonProperty("path")
    @Nullable
    abstract String getPath();

    /**
     * The port
     */
    @JsonProperty("port")
    @Nullable
    abstract Integer getPort();

}
