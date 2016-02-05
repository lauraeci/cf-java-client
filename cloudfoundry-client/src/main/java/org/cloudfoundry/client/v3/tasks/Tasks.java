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

package org.cloudfoundry.client.v3.tasks;

import reactor.core.publisher.Mono;

/**
 * Main entry point to the Cloud Foundry Tasks Client API
 */
public interface Tasks {

    /**
     * Makes the <a href="http://v3-apidocs.cloudfoundry.org/#create-a-task">Create A Task</a> request
     *
     * @param request the Create Task request
     * @return the response from the Create Task request
     */
    Mono<CreateTaskResponse> create(CreateTaskRequest request);

    /**
     * Makes the <a href="http://v3-apidocs.cloudfoundry.org/#get-a-task">Get A Task</a> request
     *
     * @param request the Get Task request
     * @return the response from the Get Task request
     */
    Mono<GetTaskResponse> get(GetTaskRequest request);

}