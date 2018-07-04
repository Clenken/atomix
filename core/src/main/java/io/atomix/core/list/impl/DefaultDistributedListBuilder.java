/*
 * Copyright 2016-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.core.list.impl;

import com.google.common.io.BaseEncoding;
import io.atomix.core.list.AsyncDistributedList;
import io.atomix.core.list.DistributedList;
import io.atomix.primitive.PrimitiveManagementService;
import io.atomix.primitive.proxy.ProxyClient;
import io.atomix.primitive.service.ServiceConfig;
import io.atomix.utils.serializer.Serializer;

import java.util.concurrent.CompletableFuture;

/**
 * Default distributed list builder.
 *
 * @param <E> type for list elements
 */
public class DefaultDistributedListBuilder<E> extends DistributedList.Builder<E> {
  public DefaultDistributedListBuilder(String name, DistributedList.Config config, PrimitiveManagementService managementService) {
    super(name, config, managementService);
  }

  @Override
  @SuppressWarnings("unchecked")
  public CompletableFuture<DistributedList<E>> buildAsync() {
    ProxyClient<DistributedListService> proxy = protocol().newProxy(
        name(),
        primitiveType(),
        DistributedListService.class,
        new ServiceConfig(),
        managementService.getPartitionService());
    return new DistributedListProxy(proxy, managementService.getPrimitiveRegistry())
        .connect()
        .thenApply(rawList -> {
          Serializer serializer = serializer();
          AsyncDistributedList<E> list = new TranscodingAsyncDistributedList<>(
              rawList,
              element -> BaseEncoding.base16().encode(serializer.encode(element)),
              string -> serializer.decode(BaseEncoding.base16().decode(string)));

          if (config.isCacheEnabled()) {
            list = new CachingAsyncDistributedList<>(list, config.getCacheSize());
          }

          if (config.isReadOnly()) {
            list = new UnmodifiableAsyncDistributedList<>(list);
          }
          return list.sync();
        });
  }
}
