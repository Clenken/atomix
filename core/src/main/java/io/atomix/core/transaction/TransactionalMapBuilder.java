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
package io.atomix.core.transaction;

import io.atomix.core.atomic.AtomicMapType;
import io.atomix.primitive.PrimitiveBuilder;
import io.atomix.primitive.PrimitiveManagementService;

/**
 * Transactional map builder.
 */
public abstract class TransactionalMapBuilder<K, V>
    extends PrimitiveBuilder<TransactionalMapBuilder<K, V>, TransactionalMapConfig, TransactionalMap<K, V>> {
  protected TransactionalMapBuilder(String name, TransactionalMapConfig config, PrimitiveManagementService managementService) {
    super(AtomicMapType.instance(), name, config, managementService);
  }
}
