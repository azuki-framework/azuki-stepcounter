/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatternFileSelector extends AbstractFileSelector {

	public static void main(final String[] args) {

		final PatternFileSelector selector = new PatternFileSelector();
		selector.setDir(new File("."));
		selector.addInclude("**/*.java");
		selector.addInclude("**/*.sql");
		selector.addInclude("**/*.xml");
		selector.addExclude(".*/**");
		selector.addExclude("target/**");

		selector.addSelectorListener(new FileSelectorListener() {
			@Override
			public void selectorFindFile(final File file, final FileSelectorEvent e) {
				System.out.println(file.getAbsolutePath());
			}
		});

		selector.select();
	}

	private static final Logger logger = LoggerFactory.getLogger(PatternFileSelector.class);

	private File dir;

	private String baseDir;

	private String separator;

	private List<Pattern> includes;

	private List<Pattern> excludes;

	public PatternFileSelector() {
		includes = new ArrayList<Pattern>();
		excludes = new ArrayList<Pattern>();

		separator = System.getProperty("file.separator");
	}

	public void setDir(final File dir) {
		this.dir = dir;
	}

	public void addInclude(final String pattern) {
		Pattern ptn = convertPattern(pattern);
		logger.debug("Add include pattern -> " + ptn.pattern());
		includes.add(ptn);
	}

	public void addExclude(final String pattern) {
		Pattern ptn = convertPattern(pattern);
		logger.debug("Add exclude pattern -> " + ptn.pattern());
		excludes.add(ptn);
	}

	@Override
	protected void doSelect() {

		if (dir.isFile()) {
			baseDir = dir.getParentFile().getAbsolutePath();

			doFile(dir);
		} else if (dir.isDirectory()) {
			baseDir = dir.getAbsolutePath();

			doDirectory(dir);
		}

	}

	private void doFile(final File file) {
		String absolutePath = file.getAbsolutePath();
		String relativePath = absolutePath.substring(baseDir.length());

		String rep = separator;
		if ("\\".equals(rep)) {
			rep = "\\\\";
		}
		String path = relativePath.replaceAll(rep, "/");

		boolean match = false;
		if (0 == includes.size()) {
			match = true;
		} else {
			for (Pattern include : includes) {
				if (include.matcher(path).matches()) {
					match = true;
					break;
				}
			}
		}

		if (match) {
			for (Pattern exclude : excludes) {
				if (exclude.matcher(path).matches()) {
					match = false;
					break;
				}
			}
		}

		if (match) {
			callFindFile(file);
		}

	}

	private void doDirectory(final File dir) {

		File[] fs = dir.listFiles();
		for (File f : fs) {
			if (f.isFile()) {
				doFile(f);
			} else if (f.isDirectory()) {
				doDirectory(f);
			}
		}

	}

	private Pattern convertPattern(final String pattern) {
		String ptn = pattern;
		ptn = ptn.replaceAll("\\.", "\\\\.");
		ptn = ptn.replaceAll("\\*\\*", ".{0,}");
		ptn = ptn.replaceAll("\\*", "[^/]{0,}");
		ptn = "^[/]{0,1}" + ptn + "$";
		return Pattern.compile(ptn);
	}
}
