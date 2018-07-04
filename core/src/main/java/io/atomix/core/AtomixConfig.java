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
package io.atomix.core;

import io.atomix.cluster.ClusterConfig;
import io.atomix.core.profile.Profile;
import io.atomix.primitive.DistributedPrimitive;
import io.atomix.primitive.partition.PartitionGroup;
import io.atomix.utils.config.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Atomix configuration.
 */
public class AtomixConfig implements Config {
  private static final String MANAGEMENT_GROUP_NAME = "system";

  private ClusterConfig cluster = new ClusterConfig();
  private boolean enableShutdownHook;
  private PartitionGroup.Config managementGroup;
  private Map<String, PartitionGroup.Config<?>> partitionGroups = new HashMap<>();
  private Map<String, DistributedPrimitive.Config> primitives = new HashMap<>();
  private List<Profile.Config> profiles = new ArrayList<>();

  /**
   * Returns the cluster configuration.
   *
   * @return the cluster configuration
   */
  public ClusterConfig getClusterConfig() {
    return cluster;
  }

  /**
   * Sets the cluster configuration.
   *
   * @param cluster the cluster configuration
   * @return the Atomix configuration
   */
  public AtomixConfig setClusterConfig(ClusterConfig cluster) {
    this.cluster = cluster;
    return this;
  }

  /**
   * Returns whether to enable the shutdown hook.
   *
   * @return whether to enable the shutdown hook
   */
  public boolean isEnableShutdownHook() {
    return enableShutdownHook;
  }

  /**
   * Sets whether to enable the shutdown hook.
   *
   * @param enableShutdownHook whether to enable the shutdown hook
   * @return the Atomix configuration
   */
  public AtomixConfig setEnableShutdownHook(boolean enableShutdownHook) {
    this.enableShutdownHook = enableShutdownHook;
    return this;
  }

  /**
   * Returns the system management partition group.
   *
   * @return the system management partition group
   */
  public PartitionGroup.Config<?> getManagementGroup() {
    return managementGroup;
  }

  /**
   * Sets the system management partition group.
   *
   * @param managementGroup the system management partition group
   * @return the Atomix configuration
   */
  public AtomixConfig setManagementGroup(PartitionGroup.Config<?> managementGroup) {
    managementGroup.setName(MANAGEMENT_GROUP_NAME);
    this.managementGroup = managementGroup;
    return this;
  }

  /**
   * Returns the partition group configurations.
   *
   * @return the partition group configurations
   */
  public Map<String, PartitionGroup.Config<?>> getPartitionGroups() {
    return partitionGroups;
  }

  /**
   * Sets the partition group configurations.
   *
   * @param partitionGroups the partition group configurations
   * @return the Atomix configuration
   */
  public AtomixConfig setPartitionGroups(Map<String, PartitionGroup.Config<?>> partitionGroups) {
    partitionGroups.forEach((name, group) -> group.setName(name));
    this.partitionGroups = partitionGroups;
    return this;
  }

  /**
   * Adds a partition group configuration.
   *
   * @param partitionGroup the partition group configuration to add
   * @return the Atomix configuration
   */
  public AtomixConfig addPartitionGroup(PartitionGroup.Config partitionGroup) {
    partitionGroups.put(partitionGroup.getName(), partitionGroup);
    return this;
  }

  /**
   * Returns the primitive configurations.
   *
   * @return the primitive configurations
   */
  public Map<String, DistributedPrimitive.Config> getPrimitives() {
    return primitives;
  }

  /**
   * Sets the primitive configurations.
   *
   * @param primitives the primitive configurations
   * @return the primitive configuration holder
   */
  public AtomixConfig setPrimitives(Map<String, DistributedPrimitive.Config> primitives) {
    this.primitives = checkNotNull(primitives);
    return this;
  }

  /**
   * Adds a primitive configuration.
   *
   * @param name   the primitive name
   * @param config the primitive configuration
   * @return the primitive configuration holder
   */
  public AtomixConfig addPrimitive(String name, DistributedPrimitive.Config config) {
    primitives.put(name, config);
    return this;
  }

  /**
   * Returns a primitive configuration.
   *
   * @param name the primitive name
   * @param <C>  the configuration type
   * @return the primitive configuration
   */
  @SuppressWarnings("unchecked")
  public <C extends DistributedPrimitive.Config<C>> C getPrimitive(String name) {
    return (C) primitives.get(name);
  }

  /**
   * Returns the Atomix profile.
   *
   * @return the Atomix profile
   */
  public List<Profile.Config> getProfiles() {
    return profiles;
  }

  /**
   * Sets the Atomix profile.
   *
   * @param profiles the profiles
   * @return the Atomix configuration
   */
  public AtomixConfig setProfiles(List<Profile.Config> profiles) {
    this.profiles = profiles;
    return this;
  }

  /**
   * Adds an Atomix profile.
   *
   * @param profile the profile to add
   * @return the Atomix configuration
   */
  public AtomixConfig addProfile(Profile.Config profile) {
    profiles.add(checkNotNull(profile, "profile cannot be null"));
    return this;
  }
}
