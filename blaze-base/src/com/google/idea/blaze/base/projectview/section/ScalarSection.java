/*
 * Copyright 2016 The Bazel Authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.idea.blaze.base.projectview.section;

/**
 * Scalar value.
 */
public final class ScalarSection<T> extends Section<T> {
  private static final long serialVersionUID = 1L;

  private final T value;

  public ScalarSection(T value) {
    this.value = value;
  }

  public T getValue() {
    return value;
  }

  public static <T> Builder<T> builder(SectionKey<T, ScalarSection<T>> sectionKey) {
    return new Builder<T>(sectionKey);
  }

  public static class Builder<T> extends SectionBuilder<T, ScalarSection<T>> {
    private T value;

    public Builder(SectionKey<T, ScalarSection<T>> sectionKey) {
      super(sectionKey);
    }

    public Builder<T> set(T value) {
      this.value = value;
      return this;
    }

    @Override
    public ScalarSection<T> build() {
      return new ScalarSection<T>(value);
    }
  }
}
