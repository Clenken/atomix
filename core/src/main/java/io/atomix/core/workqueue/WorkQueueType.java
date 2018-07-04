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
package io.atomix.core.workqueue;

import io.atomix.core.workqueue.impl.DefaultWorkQueueBuilder;
import io.atomix.core.workqueue.impl.DefaultWorkQueueService;
import io.atomix.core.workqueue.impl.WorkQueueResource;
import io.atomix.primitive.PrimitiveManagementService;
import io.atomix.primitive.PrimitiveType;
import io.atomix.primitive.resource.PrimitiveResource;
import io.atomix.primitive.service.PrimitiveService;
import io.atomix.primitive.service.ServiceConfig;
import io.atomix.utils.serializer.Namespace;
import io.atomix.utils.serializer.Namespaces;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Work queue primitive type.
 */
public class WorkQueueType<E> implements PrimitiveType<WorkQueue.Builder<E>, WorkQueue.Config, WorkQueue<E>> {
  private static final String NAME = "work-queue";
  private static final WorkQueueType INSTANCE = new WorkQueueType();

  /**
   * Returns a new work queue type instance.
   *
   * @param <E> the element type
   * @return a new work queue type
   */
  @SuppressWarnings("unchecked")
  public static <E> WorkQueueType<E> instance() {
    return INSTANCE;
  }

  @Override
  public String name() {
    return NAME;
  }

  @Override
  public Namespace namespace() {
    return Namespace.builder()
        .register(PrimitiveType.super.namespace())
        .nextId(Namespaces.BEGIN_USER_CUSTOM_ID)
        .register(Task.class)
        .register(WorkQueueStats.class)
        .build();
  }

  @Override
  public PrimitiveService newService(ServiceConfig config) {
    return new DefaultWorkQueueService();
  }

  @Override
  @SuppressWarnings("unchecked")
  public PrimitiveResource newResource(WorkQueue<E> primitive) {
    return new WorkQueueResource((AsyncWorkQueue<String>) primitive.async());
  }

  @Override
  public WorkQueue.Config newConfig() {
    return new WorkQueue.Config();
  }

  @Override
  public WorkQueue.Builder<E> newBuilder(String name, WorkQueue.Config config, PrimitiveManagementService managementService) {
    return new DefaultWorkQueueBuilder<>(name, config, managementService);
  }

  @Override
  public String toString() {
    return toStringHelper(this)
        .add("name", name())
        .toString();
  }
}