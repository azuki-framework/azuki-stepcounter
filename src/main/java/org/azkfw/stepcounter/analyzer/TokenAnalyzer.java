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
package org.azkfw.stepcounter.analyzer;

import static org.azkfw.stepcounter.utils.AzukiUtil.isBlank;
import static org.azkfw.stepcounter.utils.AzukiUtil.isNotBlank;
import static org.azkfw.stepcounter.utils.AzukiUtil.isNotNull;
import static org.azkfw.stepcounter.utils.AzukiUtil.isNull;

import java.util.ArrayList;
import java.util.List;

import org.azkfw.stepcounter.reader.BlankTokenReader;
import org.azkfw.stepcounter.reader.MultiLineCommentTokenReader;
import org.azkfw.stepcounter.reader.ReturnLineTokenReader;
import org.azkfw.stepcounter.reader.SeparatorTokenReader;
import org.azkfw.stepcounter.reader.SingleLineCommentTokenReader;
import org.azkfw.stepcounter.reader.StringTokenReader;
import org.azkfw.stepcounter.reader.TokenReadException;
import org.azkfw.stepcounter.reader.TokenReader;
import org.azkfw.stepcounter.scanner.TokenScanner;
import org.azkfw.stepcounter.token.Token;

/**
 * @author Kawakicchi
 * @deprecated {@link TokenScanner}
 */
public class TokenAnalyzer {

	public List<Token> analyze(final String data) throws TokenReadException {
		List<Token> tokens = new ArrayList<Token>();

		final List<TokenReader> tokers = new ArrayList<TokenReader>();
		tokers.add(new SingleLineCommentTokenReader());
		tokers.add(new MultiLineCommentTokenReader());
		tokers.add(new StringTokenReader());
		tokers.add(new ReturnLineTokenReader());
		tokers.add(new SeparatorTokenReader());
		tokers.add(new BlankTokenReader());

		StringBuffer buffer = new StringBuffer();
		int bufferIndex = 0; // トー化に該当しない最初の位置を保持

		TokenReader activeToker = null;
		for (int i = 0; i < data.length(); i++) {
			if (isNull(activeToker)) {

				for (TokenReader toker : tokers) {
					if (toker.is(i, data)) {
						activeToker = toker;
						activeToker.clear();
						break;
					}
				}

				if (isNotNull(activeToker)) {
					if (isNotBlank(buffer)) {
						tokens.add(new Token(bufferIndex, buffer.toString()));
						buffer = new StringBuffer();
					}
				}

			}

			if (isNotNull(activeToker)) {
				int j = activeToker.read(i, data);

				i = j - 1;

				tokens.add(activeToker.getToken());

				activeToker = null;
			} else {
				if (isBlank(buffer)) {
					bufferIndex = i;
				}
				buffer.append(data.charAt(i));
			}

		}
		if (isNotBlank(buffer)) {
			tokens.add(new Token(bufferIndex, buffer.toString()));
		}

		return tokens;
	}

}
