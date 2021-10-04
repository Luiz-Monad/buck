/*
 * Copyright (c) Facebook, Inc. and its affiliates.
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

package com.facebook.buck.android;

import static org.junit.Assert.assertEquals;

import com.facebook.buck.android.build_config.BuildConfigFields;
import com.facebook.buck.core.build.buildable.context.FakeBuildableContext;
import com.facebook.buck.core.build.context.FakeBuildContext;
import com.facebook.buck.core.build.execution.context.StepExecutionContext;
import com.facebook.buck.core.description.impl.DescriptionCache;
import com.facebook.buck.core.filesystems.RelPath;
import com.facebook.buck.core.model.BuildTarget;
import com.facebook.buck.core.model.BuildTargetFactory;
import com.facebook.buck.core.model.impl.BuildTargetPaths;
import com.facebook.buck.core.rules.resolver.impl.TestActionGraphBuilder;
import com.facebook.buck.core.sourcepath.ExplicitBuildTargetSourcePath;
import com.facebook.buck.io.filesystem.ProjectFilesystem;
import com.facebook.buck.io.filesystem.impl.FakeProjectFilesystem;
import com.facebook.buck.io.filesystem.impl.ProjectFilesystemUtils;
import com.facebook.buck.step.Step;
import com.facebook.buck.step.TestExecutionContext;
import com.facebook.buck.step.isolatedsteps.android.GenerateBuildConfigStep;
import com.facebook.buck.testutil.TemporaryPaths;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.junit.Rule;
import org.junit.Test;

/** Unit test for {@link AndroidBuildConfig}. */
public class AndroidBuildConfigTest {
  @Rule public TemporaryPaths tmpFolder = new TemporaryPaths();

  public static final BuildTarget BUILD_TARGET =
      BuildTargetFactory.newInstance("//java/com/example:build_config");
  private final ProjectFilesystem filesystem = new FakeProjectFilesystem();

  @Test
  public void testGetPathToOutput() {
    AndroidBuildConfig buildConfig = createSimpleBuildConfigRule();
    assertEquals(
        ExplicitBuildTargetSourcePath.of(
            BUILD_TARGET,
            BuildTargetPaths.getGenPath(
                filesystem.getBuckPaths(), BUILD_TARGET, "%s__/BuildConfig.java")),
        buildConfig.getSourcePathToOutput());
  }

  @Test
  public void testBuildInternal() {
    AndroidBuildConfig buildConfig = createSimpleBuildConfigRule();
    List<Step> steps =
        buildConfig.getBuildSteps(FakeBuildContext.NOOP_CONTEXT, new FakeBuildableContext());
    Step generateBuildConfigStep = steps.get(4);
    GenerateBuildConfigStep expectedStep =
        new GenerateBuildConfigStep(
            "build_config",
            /* javaPackage */ "com.example",
            /* useConstantExpressions */ false,
            Optional.empty(),
            BuildConfigFields.of(),
            BuildTargetPaths.getGenPath(
                filesystem.getBuckPaths(), BUILD_TARGET, "%s__/BuildConfig.java"));
    assertEquals(expectedStep, generateBuildConfigStep);
  }

  @Test
  public void testGetTypeMethodOfBuilder() {
    assertEquals(
        "android_build_config",
        DescriptionCache.getRuleType(AndroidBuildConfigDescription.class).getName());
  }

  @Test
  public void testGenerateBuildConfigStep() throws Exception {
    Path pathToValues = Paths.get("values.txt");
    ProjectFilesystemUtils.writeLinesToPath(
        tmpFolder.getRoot(),
        ImmutableList.of("boolean DEBUG = false", "String FOO = \"BAR\""),
        pathToValues);

    RelPath outputPath = RelPath.of(Paths.get("output.txt"));
    GenerateBuildConfigStep step =
        new GenerateBuildConfigStep(
            "build_config",
            /* javaPackage */ "com.example",
            /* useConstantExpressions */ false,
            Optional.of(RelPath.of(pathToValues)),
            BuildConfigFields.fromFieldDeclarations(ImmutableList.of("boolean BAR = true")),
            outputPath);

    StepExecutionContext context =
        TestExecutionContext.newBuilder().setRuleCellRoot(tmpFolder.getRoot()).build();
    int exitCode = step.execute(context).getExitCode();
    assertEquals(0, exitCode);
    assertEquals(
        Lists.newArrayList(
            "// Generated by build_config. DO NOT MODIFY.",
            "package com.example;",
            "public class BuildConfig {",
            "  private BuildConfig() {}",
            "  public static final boolean DEBUG = Boolean.parseBoolean(null);",
            "  public static final String FOO = !Boolean.parseBoolean(null) ? \"BAR\" : null;",
            "  public static final boolean BAR = !Boolean.parseBoolean(null);",
            "  public static final boolean IS_EXOPACKAGE = Boolean.parseBoolean(null);",
            "  public static final int EXOPACKAGE_FLAGS = !Boolean.parseBoolean(null) ? 0 : 0;",
            "}"),
        ProjectFilesystemUtils.readLines(tmpFolder.getRoot(), outputPath.getPath()));
  }

  private static AndroidBuildConfig createSimpleBuildConfigRule() {
    // First, create the BuildConfig object.
    return new AndroidBuildConfig(
        BUILD_TARGET,
        new FakeProjectFilesystem(),
        new TestActionGraphBuilder(),
        /* javaPackage */ "com.example",
        /* values */ BuildConfigFields.of(),
        /* valuesFile */ Optional.empty(),
        /* useConstantExpressions */ false,
        /* shouldExecuteInSeparateProcess */ false);
  }

  // TODO(nickpalmer): Add another unit test that passes in a non-trivial DependencyGraph and verify
  // that the resulting set of libraryManifestPaths is computed correctly.
}
