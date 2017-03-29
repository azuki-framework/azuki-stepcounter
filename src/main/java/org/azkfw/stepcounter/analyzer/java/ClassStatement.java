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

/**
 * @author kawakicchi
 */
public class ClassStatement extends Statement {

	private Statement statemant;
	private BlockStatement block;

	public ClassStatement(final Statement statemant, final BlockStatement block) {
		this.statemant = statemant;
		this.block = block;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(statemant.toString());
		s.append(block.toString());
		return s.toString();
	}
}
