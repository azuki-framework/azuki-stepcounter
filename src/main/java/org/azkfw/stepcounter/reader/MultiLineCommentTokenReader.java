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
package org.azkfw.stepcounter.reader;

import org.azkfw.stepcounter.token.Token;

public class MultiLineCommentTokenReader extends CommentTokenReader {

	private int index;

	private StringBuffer string;

	public MultiLineCommentTokenReader() {
		clear();
	}

	public void clear() {
		index = -1;
		string = new StringBuffer();
	}

	public boolean is(final int index, final String data) {
		if (data.length() > index + 1) {

			char c1 = data.charAt(index);
			char c2 = data.charAt(index + 1);

			if ('/' == c1 && '*' == c2) {
				return true;
			}
		}
		return false;
	}

	public int read(final int index, final String data) {
		this.index = index;

		string.append("/*");

		int i = index + 2;
		for (; i < data.length() - 1; i++) {
			char c1 = data.charAt(i);
			char c2 = data.charAt(i + 1);
			if ('*' == c1 && '/' == c2) {
				string.append(c1);
				string.append(c2);
				return i + 2;
			}
			string.append(c1);
		}
		return i + 1;
	}

	public Token getToken() {
		return new Token(index, string.toString(), this.getClass());
	}
}