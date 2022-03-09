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

package com.facebook.buck.intellij.ideabuck.visibility;

import com.facebook.buck.intellij.ideabuck.api.BuckTarget;
import com.facebook.buck.intellij.ideabuck.api.BuckTargetPattern;
import com.facebook.buck.intellij.ideabuck.util.BuckVisibilityUtil;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public class BuckVisibilityState {

  public enum VisibleState {
    VISIBLE,
    NOT_VISIBLE,
    UNKNOWN
  }

  @Nullable private final VisibleState mVisibleState;
  @Nullable private final List<BuckTargetPattern> mVisibilities;

  public BuckVisibilityState(@Nullable List<BuckTargetPattern> visibilities) {
    mVisibleState = null;
    mVisibilities = visibilities;
  }

  public BuckVisibilityState(@Nullable VisibleState visibleState) {
    mVisibleState = visibleState;
    mVisibilities = Collections.emptyList();
  }

  public VisibleState getVisibility(@Nullable BuckTarget buckTarget) {
    if (mVisibleState != null) {
      return mVisibleState;
    }
    return BuckVisibilityUtil.isVisibleTo(buckTarget, mVisibilities)
        ? VisibleState.VISIBLE
        : VisibleState.NOT_VISIBLE;
  }
}
