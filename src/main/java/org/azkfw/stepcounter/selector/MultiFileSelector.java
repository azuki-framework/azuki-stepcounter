/**
 * Copyright 2017 Azuki Framework.
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
package org.azkfw.stepcounter.selector;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.azkfw.stepcounter.utils.AzukiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kawakicchi
 */
public final class MultiFileSelector extends AbstractFileSelector {

	public static void main(final String[] args) {

		final PatternFileSelector selector1 = new PatternFileSelector();
		selector1.setDir(new File("."));
		selector1.addInclude("**/*.java");
		selector1.addInclude("**/*.sql");
		selector1.addInclude("**/*.xml");
		selector1.addExclude(".*/**");
		selector1.addExclude("target/**");

		final PatternFileSelector selector2 = new PatternFileSelector();
		selector2.setDir(new File("."));
		selector2.addInclude("**/*.java");
		selector2.addInclude("**/*.sql");
		selector2.addInclude("**/*.xml");
		selector2.addExclude(".*/**");
		selector2.addExclude("target/**");

		final MultiFileSelector selector = new MultiFileSelector();
		selector.addSelector(selector1);
		selector.addSelector(selector2);

		selector.addSelectorListener(new FileSelectorListener() {
			@Override
			public void selectorFindFile(final File file, final FileSelectorEvent e) {
				System.out.println(file.getAbsolutePath());
			}
		});

		selector.select();
	}

	private static final Logger logger = LoggerFactory.getLogger(MultiFileSelector.class);

	private final List<FileSelector> selectors;

	public MultiFileSelector() {
		selectors = new ArrayList<FileSelector>();
	}

	public MultiFileSelector(final Collection<? extends FileSelector> selectors) {
		this.selectors = new ArrayList<FileSelector>();

		if (AzukiUtil.isEmpty(selectors)) {
			selectors.forEach(selector -> addSelector(selector));
		}
	}

	public synchronized void addSelector(final FileSelector selector) {
		logger.debug("Add selector -> {}", selector);

		selector.addSelectorListener(new FileSelectorListener() {
			@Override
			public void selectorFindFile(final File file, final FileSelectorEvent e) {
				callFindFile(file);
			}
		});

		selectors.add(selector);
	}

	@Override
	protected void doSelect() {
		selectors.forEach(selector -> selector.select());
	}

}
