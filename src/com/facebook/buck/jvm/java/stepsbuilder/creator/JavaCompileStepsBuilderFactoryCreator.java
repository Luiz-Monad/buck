/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
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

package com.facebook.buck.jvm.java.stepsbuilder.creator;

import com.facebook.buck.jvm.cd.CompileStepsBuilderFactory;
import com.facebook.buck.jvm.cd.DefaultCompileStepsBuilderFactory;
import com.facebook.buck.jvm.cd.params.CDParams;
import com.facebook.buck.jvm.java.CompileToJarStepFactory;
import com.facebook.buck.jvm.java.DaemonJavacToJarStepFactory;
import com.facebook.buck.jvm.java.stepsbuilder.javacd.JavaCDStepsBuilderFactory;

/** Creator that creates an appropriate {@link CompileStepsBuilderFactory}. */
public class JavaCompileStepsBuilderFactoryCreator {

  private JavaCompileStepsBuilderFactoryCreator() {}

  /** Returns specific implementation of {@link CompileStepsBuilderFactory}. */
  public static <T extends CompileToJarStepFactory.ExtraParams>
      CompileStepsBuilderFactory createFactory(
          CompileToJarStepFactory<T> configuredCompiler, CDParams cdParams) {

    if (configuredCompiler.supportsCompilationDaemon()) {
      DaemonJavacToJarStepFactory daemonJavacToJarStepFactory =
          (DaemonJavacToJarStepFactory) configuredCompiler;
      return new JavaCDStepsBuilderFactory(daemonJavacToJarStepFactory, cdParams);
    }

    return new DefaultCompileStepsBuilderFactory<>(configuredCompiler);
  }
}
