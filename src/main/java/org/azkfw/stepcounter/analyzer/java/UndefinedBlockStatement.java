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
package org.azkfw.stepcounter.analyzer.java;

import java.util.ArrayList;
import java.util.List;

import org.azkfw.stepcounter.token.Token;
import org.azkfw.stepcounter.utils.AzukiUtil;

/**
 * @author kawakicchi
 */
public class UndefinedBlockStatement extends UndefinedStatement {

	private final List<UndefinedStatement> statements;

	private final String blockPrefix;
	private final String blockSuffix;

	private Token blockPrefixToken;
	private Token blockSuffixToken;

	public UndefinedBlockStatement(final String prefix, final String suffix) {
		statements = new ArrayList<UndefinedStatement>();
		blockPrefix = prefix;
		blockSuffix = suffix;
		blockPrefixToken = null;
		blockSuffixToken = null;
	}

	public Token getPrefixBlockToken() {
		return blockPrefixToken;
	}

	public Token getSuffixBlockToken() {
		return blockSuffixToken;
	}

	public void setBlockToken(final Token prefix, final Token suffix) {
		blockPrefixToken = prefix;
		blockSuffixToken = suffix;
	}

	public boolean isSuffixToken(final Token token) {
		return (AzukiUtil.isEqual(token.getWord(), blockSuffix));
	}

	public void addStatement(final UndefinedStatement statement) {
		statements.add(statement);
	}

	public List<UndefinedStatement> getStatements() {
		return statements;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();

		statements.forEach(stat -> {
			if (0 < s.length()) {
				s.append("\n");
			}
			s.append(stat.toString());
		});

		if (AzukiUtil.isNotNull(blockPrefixToken)) {
			s.insert(0, blockPrefixToken.getWord());
			if (AzukiUtil.isNotEmpty(statements)) {
				s.insert(1, "\n");
			}
		}
		if (AzukiUtil.isNotNull(blockSuffixToken)) {
			if (AzukiUtil.isNotEmpty(statements)) {
				s.append("\n");
			}
			s.append(blockSuffixToken.getWord());
		}

		return s.toString();
	}
}
