/*
 * Copyright 2015 the original author or authors.
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
package io.atomix.coordination.state;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.SerializableTypeResolver;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.serializer.SerializerRegistry;
import io.atomix.copycat.client.Command;

/**
 * Lock commands.
 * <p>
 * This class reserves serializable type IDs {@code 115} through {@code 118}
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public final class LockCommands {

  private LockCommands() {
  }

  /**
   * Abstract lock command.
   */
  public static abstract class LockCommand<V> implements Command<V>, CatalystSerializable {

    @Override
    public ConsistencyLevel consistency() {
      return ConsistencyLevel.LINEARIZABLE;
    }

    @Override
    public CompactionMode compaction() {
      return CompactionMode.QUORUM;
    }

    @Override
    public void writeObject(BufferOutput buffer, Serializer serializer) {
    }

    @Override
    public void readObject(BufferInput buffer, Serializer serializer) {
    }
  }

  /**
   * Lock command.
   */
  public static class Lock extends LockCommand<Void> {
    private long timeout;

    public Lock() {
    }

    public Lock(long timeout) {
      this.timeout = timeout;
    }

    /**
     * Returns the try lock timeout.
     *
     * @return The try lock timeout in milliseconds.
     */
    public long timeout() {
      return timeout;
    }

    @Override
    public CompactionMode compaction() {
      return timeout > 0 ? CompactionMode.SEQUENTIAL : CompactionMode.QUORUM;
    }

    @Override
    public void writeObject(BufferOutput buffer, Serializer serializer) {
      buffer.writeLong(timeout);
    }

    @Override
    public void readObject(BufferInput buffer, Serializer serializer) {
      timeout = buffer.readLong();
    }
  }

  /**
   * Unlock command.
   */
  public static class Unlock extends LockCommand<Void> {
    @Override
    public CompactionMode compaction() {
      return CompactionMode.SEQUENTIAL;
    }
  }

  /**
   * Lock command type resolver.
   */
  public static class TypeResolver implements SerializableTypeResolver {
    @Override
    public void resolve(SerializerRegistry registry) {
      registry.register(Lock.class, -141);
      registry.register(Unlock.class, -142);
    }
  }

}
