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
public class MultiLineCommentTokenReader extends CommentTokenReader {

	private int index;

	private StringBuffer string;

	private final String commentPrefix;

	private final String commentSuffix;

	public MultiLineCommentTokenReader() {
		commentPrefix = "/*";
		commentSuffix = "*/";
		clear();
	}

	public MultiLineCommentTokenReader(final String prefix, final String suffix) {
		commentPrefix = prefix;
		commentSuffix = suffix;
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
	public int read(final int index, final String data) throws TokenReadException {
		this.index = index;

		string.append(commentPrefix);
		int i = index + commentPrefix.length();

		for (; isGetting(commentSuffix.length(), i, data); i++) {
			final String str = data.substring(i, i + commentSuffix.length());
			if (commentSuffix.equals(str)) {
				string.append(commentSuffix);
				return i + commentSuffix.length();
			}
			string.append(data.charAt(i));
		}
		throw new TokenReadException("MultiLineComment Token reader error.[" + string + "...]", this);
	}

	@Override
	public Token getToken() {
		return new Token(index, string.toString(), this.getClass());
	}
}