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
package org.azkfw.stepcounter.token;

import org.azkfw.stepcounter.reader.TokenReader;

/**
 * @author kawakicchi
 */
public class Token {

	private int index;

	private String word;

	private Class<? extends TokenReader> reader;

	public Token(final int index, final String word) {
		this.index = index;
		this.word = word;
		this.reader = null;
	}

	public Token(final int index, final String word, final Class<? extends TokenReader> reader) {
		this.index = index;
		this.word = word;
		this.reader = reader;
	}

	public int getIndex() {
		return index;
	}

	public String getWord() {
		return word;
	}

	public Class<? extends TokenReader> getReader() {
		return reader;
	}

	@Override
	public String toString() {
		return word;
	}
}
