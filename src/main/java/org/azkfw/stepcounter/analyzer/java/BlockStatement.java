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

/**
 * @author kawakicchi
 */
public class BlockStatement extends Statement {

	private final List<Statement> statements;

	public BlockStatement() {
		statements = new ArrayList<Statement>();
	}

	public void addStatement(final Statement statement) {
		statements.add(statement);
	}

	public List<Statement> getStatements() {
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

		return s.toString();
	}
}
