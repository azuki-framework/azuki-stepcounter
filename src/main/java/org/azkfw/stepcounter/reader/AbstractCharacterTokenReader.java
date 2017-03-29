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
package org.azkfw.stepcounter.reader;

import org.azkfw.stepcounter.token.Token;

/**
 * @author kawakicchi
 */
public abstract class AbstractCharacterTokenReader extends AbstractTokenReader {

	private int index;

	private StringBuffer string;

	private final char[] chars;

	public AbstractCharacterTokenReader(final char... chars) {
		this.chars = chars;
		clear();
	}

	@Override
	public final void clear() {
		index = -1;
		string = new StringBuffer();
	}

	@Override
	public final boolean is(final int index, final String data) {
		if (data.length() > index) {
			char c = data.charAt(index);
			if (isAnyMatch(c, chars)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final int read(final int index, final String data) {
		this.index = index;

		char c = data.charAt(index);
		string.append(c);

		return index + 1;
	}

	@Override
	public final Token getToken() {
		return new Token(index, string.toString(), this.getClass());
	}
}
