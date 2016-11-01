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
package org.azkfw.stepcounter.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.azkfw.stepcounter.reader.BlankTokenReader;
import org.azkfw.stepcounter.reader.MultiLineCommentTokenReader;
import org.azkfw.stepcounter.reader.ReturnLineTokenReader;
import org.azkfw.stepcounter.reader.SeparatorTokenReader;
import org.azkfw.stepcounter.reader.SingleLineCommentTokenReader;
import org.azkfw.stepcounter.reader.StringTokenReader;
import org.azkfw.stepcounter.reader.TokenReader;
import org.azkfw.stepcounter.token.Token;

/**
 *
 * @author Kawakicchi
 */
public class TokenAnalyzer {

	public List<Token> analyze(final String data) {
		List<Token> tokens = new ArrayList<Token>();

		List<TokenReader> tokers = new ArrayList<TokenReader>();

		tokers.add(new SingleLineCommentTokenReader());
		tokers.add(new MultiLineCommentTokenReader());
		tokers.add(new StringTokenReader());
		tokers.add(new ReturnLineTokenReader());
		tokers.add(new SeparatorTokenReader());
		tokers.add(new BlankTokenReader());

		StringBuffer buffer = new StringBuffer();
		int bufferIndex = 0;

		TokenReader activeToker = null;
		for (int i = 0; i < data.length(); i++) {
			if (null == activeToker) {
				for (TokenReader toker : tokers) {
					if (toker.is(i, data)) {
						activeToker = toker;
						activeToker.clear();
						break;
					}
				}
				if (null != activeToker) {
					if (0 < buffer.length()) {
						tokens.add(new Token(bufferIndex, buffer.toString()));
						buffer = new StringBuffer();
					}
				}
			}

			if (null != activeToker) {
				int j = activeToker.read(i, data);
				if (-1 != j) {
					i = j - 1;

					tokens.add(activeToker.getToken());

					activeToker = null;
				} else {
					// TODO: error
				}
			} else {
				if (0 == buffer.length()) {
					bufferIndex = i;
				}
				buffer.append(data.charAt(i));
			}

		}
		if (0 < buffer.length()) {
			tokens.add(new Token(bufferIndex, buffer.toString()));
		}

		return tokens;
	}

}
