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
package io.atomix.core.tree.impl;

import io.atomix.core.tree.AsyncAtomicDocumentTree;
import io.atomix.core.tree.AtomicDocumentTree;
import io.atomix.primitive.PrimitiveManagementService;
import io.atomix.primitive.proxy.ProxyClient;
import io.atomix.primitive.service.ServiceConfig;
import io.atomix.utils.serializer.Serializer;

import java.util.concurrent.CompletableFuture;

/**
 * Default {@link AsyncAtomicDocumentTree} builder.
 *
 * @param <V> type for document tree value
 */
public class DefaultAtomicDocumentTreeBuilder<V> extends AtomicDocumentTree.Builder<V> {
  public DefaultAtomicDocumentTreeBuilder(String name, AtomicDocumentTree.Config config, PrimitiveManagementService managementService) {
    super(name, config, managementService);
  }

  @Override
  @SuppressWarnings("unchecked")
  public CompletableFuture<AtomicDocumentTree<V>> buildAsync() {
    ProxyClient<DocumentTreeService> proxy = protocol().newProxy(
        name(),
        primitiveType(),
        DocumentTreeService.class,
        new ServiceConfig(),
        managementService.getPartitionService());
    return new AtomicDocumentTreeProxy(proxy, managementService.getPrimitiveRegistry())
        .connect()
        .thenApply(tree -> {
          Serializer serializer = serializer();
          return new TranscodingAsyncAtomicDocumentTree<V, byte[]>(
              tree,
              key -> serializer.encode(key),
              bytes -> serializer.decode(bytes))
              .sync();
        });
  }
}