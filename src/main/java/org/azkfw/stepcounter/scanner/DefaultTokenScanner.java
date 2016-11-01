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
package org.azkfw.stepcounter.scanner;

import java.io.File;
import java.io.IOException;
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
import org.azkfw.stepcounter.utils.FileUtils;

public class DefaultTokenScanner extends AbstractTokenScanner {

	private final List<TokenReader> readers;

	private File file;

	public DefaultTokenScanner() {
		readers = new ArrayList<TokenReader>();

		readers.add(new SingleLineCommentTokenReader());
		readers.add(new MultiLineCommentTokenReader());
		readers.add(new StringTokenReader());
		readers.add(new ReturnLineTokenReader());
		readers.add(new SeparatorTokenReader());
		readers.add(new BlankTokenReader());
	}

	public void setFile(final File file) {
		this.file = file;
	}

	@Override
	protected final void doScan() {
		try {
			String data = FileUtils.readFileToString(file);
			doScan(data);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void doScan(final String data) {
		StringBuffer buffer = new StringBuffer();
		int bufferIndex = 0;

		TokenReader activeReader = null;
		for (int i = 0; i < data.length(); i++) {
			if (null == activeReader) {
				for (TokenReader reader : readers) {
					if (reader.is(i, data)) {
						activeReader = reader;
						activeReader.clear();
						break;
					}
				}
				if (null != activeReader) {
					if (0 < buffer.length()) {

						callFindToken(new Token(bufferIndex, buffer.toString()));

						buffer = new StringBuffer();
					}
				}
			}

			if (null != activeReader) {
				int j = activeReader.read(i, data);
				if (-1 != j) {
					i = j - 1;

					callFindToken(activeReader.getToken());

					activeReader = null;
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
			callFindToken(new Token(bufferIndex, buffer.toString()));
		}
	}

}
