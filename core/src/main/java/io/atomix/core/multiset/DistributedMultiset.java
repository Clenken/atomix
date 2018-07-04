/*
 * Copyright 2018-present Open Networking Foundation
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
package io.atomix.core.multiset;

import com.google.common.collect.Multiset;
import io.atomix.core.collection.DistributedCollection;
import io.atomix.core.set.DistributedSet;
import io.atomix.primitive.DistributedPrimitive;
import io.atomix.primitive.PrimitiveManagementService;

import java.util.Spliterator;
import java.util.Spliterators;

/**
 * Distributed multiset.
 */
public interface DistributedMultiset<E> extends DistributedCollection<E>, Multiset<E> {

  @Override
  DistributedSet<E> elementSet();

  @Override
  DistributedSet<Entry<E>> entrySet();

  @Override
  default Spliterator<E> spliterator() {
    return Spliterators.spliterator(this, 0);
  }

  @Override
  AsyncDistributedMultiset<E> async();

  /**
   * Builder for distributed multiset.
   *
   * @param <E> multiset element type
   */
  abstract class Builder<E> extends DistributedPrimitive.Builder<Builder<E>, DistributedMultisetConfig, DistributedMultiset<E>> {
    protected Builder(String name, DistributedMultisetConfig config, PrimitiveManagementService managementService) {
      super(DistributedMultisetType.instance(), name, config, managementService);
    }
  }
}
