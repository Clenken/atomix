/*
 * Copyright 2015-present Open Networking Foundation
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
package io.atomix.core.map;

import io.atomix.primitive.PrimitiveBuilder;
import io.atomix.primitive.PrimitiveManagementService;
import io.atomix.primitive.protocol.PrimitiveProtocol;
import io.atomix.primitive.protocol.ProxyCompatibleBuilder;
import io.atomix.primitive.protocol.ProxyProtocol;

/**
 * Builder for AtomicCounterMap.
 */
public abstract class AtomicCounterMapBuilder<K>
    extends PrimitiveBuilder<AtomicCounterMapBuilder<K>, AtomicCounterMapConfig, AtomicCounterMap<K>>
    implements ProxyCompatibleBuilder<AtomicCounterMapBuilder<K>> {

  protected AtomicCounterMapBuilder(String name, AtomicCounterMapConfig config, PrimitiveManagementService managementService) {
    super(AtomicCounterMapType.instance(), name, config, managementService);
  }

  @Override
  public AtomicCounterMapBuilder<K> withProtocol(ProxyProtocol protocol) {
    return withProtocol((PrimitiveProtocol) protocol);
  }
}
