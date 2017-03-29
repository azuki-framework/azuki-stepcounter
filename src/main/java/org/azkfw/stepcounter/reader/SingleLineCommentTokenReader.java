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
public class SingleLineCommentTokenReader extends CommentTokenReader {

	private int index;

	private StringBuffer string;

	private final String commentPrefix;

	public SingleLineCommentTokenReader() {
		commentPrefix = "//";
		clear();
	}

	@Override
	public void clear() {
		index = -1;
		string = new StringBuffer();
	}

	@Override
	public boolean is(final int index, final String data) {
		if (isGetting(commentPrefix.length(), index, data)) {
			final String str = data.substring(index, index + commentPrefix.length());
			if (commentPrefix.equals(str)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int read(final int index, final String data) {
		this.index = index;

		string.append(commentPrefix);
		int i = index + commentPrefix.length();

		for (; i < data.length(); i++) {
			char c = data.charAt(i);
			if (isAnyMatch(c, '\r', '\n')) {
				break;
			}
			string.append(c);
		}
		return i;
	}

	@Override
	public Token getToken() {
		return new Token(index, string.toString(), this.getClass());
	}
}