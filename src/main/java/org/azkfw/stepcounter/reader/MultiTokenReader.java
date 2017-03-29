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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.azkfw.stepcounter.token.Token;

/**
 * @author kawakicchi
 */
public class MultiTokenReader implements TokenReader {

	private final List<TokenReader> readers;
	private TokenReader activeReader = null;

	private MultiTokenReader(final Collection<? extends TokenReader> readers) {
		this.readers = new ArrayList<TokenReader>(readers);
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean is(int index, String data) {
		for (TokenReader reader : readers) {
			if (reader.is(index, data)) {
				activeReader = reader;
				activeReader.clear();
				return true;
			}
		}
		return false;
	}

	@Override
	public int read(int index, String data) throws TokenReadException {
		return activeReader.read(index, data);
	}

	@Override
	public Token getToken() {
		return activeReader.getToken();
	}

	public static class Builder {
		private final List<TokenReader> readers;

		private Builder() {
			readers = new ArrayList<TokenReader>();
		}

		public static Builder newInstance() {
			return new Builder();
		}

		public void addReader(final TokenReader reader) {
			readers.add(reader);
		}

		public void addReaders(final Collection<? extends TokenReader> readers) {
			this.readers.addAll(readers);
		}

		public MultiTokenReader build() {
			return new MultiTokenReader(readers);
		}
	}
}
