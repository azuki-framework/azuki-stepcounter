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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * このクラスは、ファイル選択機能を定義する基底クラスです。
 *
 * @author Kawakicchi
 */
public abstract class AbstractFileSelector implements FileSelector {

	/** イベント */
	private final FileSelectorEvent event;

	/** リスナー */
	private final List<FileSelectorListener> listeners;

	/** 選択ファイル */
	private final Set<File> files;

	/**
	 * コンストラクタ
	 */
	public AbstractFileSelector() {
		event = new FileSelectorEvent(this);
		listeners = new ArrayList<FileSelectorListener>();
		files = new HashSet<File>();
	}

	@Override
	public final void select() {
		doSelect();
	}

	@Override
	public synchronized final void addSelectorListener(final FileSelectorListener listener) {
		listeners.add(listener);
	}

	/**
	 * ファイル選択
	 * 
	 * @param file
	 */
	protected final void callFindFile(final File file) {
		synchronized (listeners) {

			if (!files.contains(file)) {
				files.add(file);

				for (FileSelectorListener l : listeners) {
					l.selectorFindFile(file, event);
				}

			}
		}
	}

	protected abstract void doSelect();
}
