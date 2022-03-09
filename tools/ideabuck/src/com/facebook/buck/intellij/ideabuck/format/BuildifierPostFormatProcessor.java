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

package com.facebook.buck.intellij.ideabuck.format;

import com.facebook.buck.intellij.ideabuck.lang.BuckFile;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.impl.source.codeStyle.PostFormatProcessor;
import org.jetbrains.annotations.NotNull;

/** Makes {@code buildifer} available as an external formatter. */
public class BuildifierPostFormatProcessor implements PostFormatProcessor {

  private static final Logger LOGGER = Logger.getInstance(BuildifierPostFormatProcessor.class);

  @Override
  public PsiElement processElement(
      @NotNull PsiElement source, @NotNull CodeStyleSettings codeStyleSettings) {
    if (source instanceof BuckFile) {
      processText((BuckFile) source, source.getTextRange(), codeStyleSettings);
    }
    return source;
  }

  @NotNull
  @Override
  public TextRange processText(
      @NotNull PsiFile source,
      @NotNull TextRange rangeToReformat,
      @NotNull CodeStyleSettings codeStyleSettings) {
    if (source instanceof BuckFile) {
      BuildifierUtil.doReformat(source);
      return source.getTextRange();
    }
    return rangeToReformat;
  }
}
