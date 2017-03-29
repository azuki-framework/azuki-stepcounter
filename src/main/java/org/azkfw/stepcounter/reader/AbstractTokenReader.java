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

/**
 * @author kawakicchi
 */
public abstract class AbstractTokenReader implements TokenReader {

	protected final boolean isAnyMatch(char chr, char... chrs) {
		for (char c : chrs) {
			if (c == chr) {
				return true;
			}
		}
		return false;
	}

	protected final boolean isGetting(final int size, final int index, final String data) {
		return (data.length() > index + (size - 1));
	}
}
