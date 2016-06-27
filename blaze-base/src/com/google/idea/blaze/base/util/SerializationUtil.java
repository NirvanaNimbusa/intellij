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
package com.google.idea.blaze.base.util;

import com.google.common.io.Closeables;
import com.intellij.CommonBundle;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

/**
 * Utils for serialization.
 */
public class SerializationUtil {
  private static final Logger LOG = Logger.getInstance(SerializationUtil.class.getName());

  public static void saveToDisk(@NotNull File file, @NotNull Serializable serializable) throws IOException {
    ensureExists(file.getParentFile());
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      try {
        oos.writeObject(serializable);
      }
      finally {
        Closeables.close(oos, false);
      }
    }
    finally {
      Closeables.close(fos, false);
    }
  }

  @Nullable
  public static Object loadFromDisk(
    @NotNull File file,
    @NotNull final Iterable<ClassLoader> classLoaders) throws IOException {
    try {
      FileInputStream fin = null;
      try {
        if (!file.exists()) {
          return null;
        }
        fin = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fin) {
          @Override
          protected Class<?> resolveClass(ObjectStreamClass desc)
            throws IOException, ClassNotFoundException {
            String name = desc.getName();
            for (ClassLoader loader : classLoaders) {
              try {
                return Class.forName(name, false, loader);
              }
              catch (ClassNotFoundException e) {
                // Ignore - will throw eventually in super
              }
            }
            return super.resolveClass(desc);
          }
        };
        try {
          return (Object) ois.readObject();
        }
        finally {
          Closeables.close(ois, false);
        }
      }
      finally {
        Closeables.close(fin, false);
      }
    }
    catch (ClassNotFoundException e) {
      throw new IOException(e);
    }
    catch (ClassCastException e) {
      throw new IOException(e);
    }
  }

  private static void ensureExists(@NotNull File dir) throws IOException {
    if (!dir.exists() && !dir.mkdirs()) {
      throw new IOException(
        CommonBundle.message("exception.directory.can.not.create", dir.getPath()));
    }
  }
}
