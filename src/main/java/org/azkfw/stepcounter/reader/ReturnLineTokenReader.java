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
public class ReturnLineTokenReader extends AbstractTokenReader {

	private int index;

	private StringBuffer string;

	public ReturnLineTokenReader() {
		clear();
	}

	@Override
	public void clear() {
		index = -1;
		string = new StringBuffer();
	}

	@Override
	public boolean is(final int index, final String data) {
		if (data.length() > index) {

			char c = data.charAt(index);
			if (isAnyMatch(c, '\r', '\n')) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int read(final int index, final String data) {
		this.index = index;

		char c1 = data.charAt(index);
		if (index + 1 < data.length()) {
			if ('\r' == c1) {
				char c2 = data.charAt(index + 1);
				if ('\n' == c2) {
					string.append(c1);
					string.append(c2);
					return index + 2;
				}
			}
		}

		string.append(c1);
		return index + 1;
	}

	@Override
	public Token getToken() {
		return new Token(index, string.toString(), this.getClass());
	}
}
