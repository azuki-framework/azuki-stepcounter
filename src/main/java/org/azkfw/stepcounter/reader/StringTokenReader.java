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

public class StringTokenReader implements TokenReader {

	private int index;

	private StringBuffer string;

	public StringTokenReader() {
		clear();
	}

	public void clear() {
		index = -1;
		string = new StringBuffer();
	}

	public boolean is(final int index, final String data) {
		if (data.length() > index) {

			char c1 = data.charAt(index);

			if ('"' == c1) {
				return true;
			}
		}
		return false;
	}

	public int read(final int index, final String data) {
		this.index = index;

		string.append('"');

		int i = index + 1;
		for (; i < data.length(); i++) {
			char c1 = data.charAt(i);
			if ('\\' == c1) {

				if (i + 1 < data.length()) {
					char c2 = data.charAt(i + 1);

					if ('\\' == c2) {
						i++;
						string.append(c1);
						string.append(c2);
					} else if ('"' == c2) {
						string.append(c1);
						string.append(c2);
						return i + 2;
					}
					string.append(c1);

				} else {
					string.append(c1);
				}

			} else if ('"' == c1) {
				string.append(c1);
				return i + 1;
			} else {
				string.append(c1);
			}
		}
		return i;
	}

	public Token getToken() {
		return new Token(index, string.toString(), this.getClass());
	}
}