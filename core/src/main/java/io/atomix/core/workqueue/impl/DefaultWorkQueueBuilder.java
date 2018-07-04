/*
 * Copyright 2017-present Open Networking Foundation
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
package io.atomix.core.workqueue.impl;

import io.atomix.core.workqueue.WorkQueue;
import io.atomix.primitive.PrimitiveManagementService;
import io.atomix.primitive.proxy.ProxyClient;
import io.atomix.primitive.service.ServiceConfig;
import io.atomix.utils.serializer.Serializer;

import java.util.concurrent.CompletableFuture;

/**
 * Default work queue builder implementation.
 */
public class DefaultWorkQueueBuilder<E> extends WorkQueue.Builder<E> {
  public DefaultWorkQueueBuilder(String name, WorkQueue.Config config, PrimitiveManagementService managementService) {
    super(name, config, managementService);
  }

  @Override
  @SuppressWarnings("unchecked")
  public CompletableFuture<WorkQueue<E>> buildAsync() {
    ProxyClient<WorkQueueService> proxy = protocol().newProxy(
        name(),
        primitiveType(),
        WorkQueueService.class,
        new ServiceConfig(),
        managementService.getPartitionService());
    return new WorkQueueProxy(proxy, managementService.getPrimitiveRegistry())
        .connect()
        .thenApply(queue -> {
          Serializer serializer = serializer();
          return new TranscodingAsyncWorkQueue<E, byte[]>(
              queue,
              item -> serializer.encode(item),
              bytes -> serializer.decode(bytes))
              .sync();
        });
  }
}
