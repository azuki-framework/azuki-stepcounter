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
import java.util.Collection;
import java.util.List;

import org.azkfw.stepcounter.token.Token;

/**
 * @author kawakicchi
 */
public abstract class Statement {

	private List<Token> tokens;

	public Statement() {
		tokens = new ArrayList<Token>();
	}

	public Statement(final Token token) {
		tokens = new ArrayList<Token>();
		addToken(token);
	}

	public Statement(final Collection<? extends Token> tokens) {
		this.tokens = new ArrayList<Token>();
		tokens.forEach(token -> addToken(token));
	}

	public void addToken(final Token token) {
		tokens.add(token);
	}

	public List<Token> getTokens() {
		return tokens;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		tokens.forEach(token -> {
			if (0 < s.length()) {
				s.append(" ");
			}
			s.append(token.getWord());
		});
		return s.toString();
	}
}
