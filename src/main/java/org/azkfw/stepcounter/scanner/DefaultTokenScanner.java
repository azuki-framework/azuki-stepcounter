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
package org.azkfw.stepcounter.scanner;

import static org.azkfw.stepcounter.utils.AzukiUtil.isBlank;
import static org.azkfw.stepcounter.utils.AzukiUtil.isNotBlank;

import java.io.File;
import java.io.IOException;

import org.azkfw.stepcounter.reader.TokenReadException;
import org.azkfw.stepcounter.reader.TokenReader;
import org.azkfw.stepcounter.selector.FileSelector;
import org.azkfw.stepcounter.selector.FileSelectorEvent;
import org.azkfw.stepcounter.selector.FileSelectorListener;
import org.azkfw.stepcounter.token.Token;
import org.azkfw.stepcounter.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kawakicchi
 */
public class DefaultTokenScanner extends AbstractTokenScanner {

	private static final Logger logger = LoggerFactory.getLogger(DefaultTokenScanner.class);

	private final FileSelector selector;
	private final TokenReader reader;

	public DefaultTokenScanner(final TokenReader reader, final FileSelector selector) {
		this.reader = reader;
		this.selector = selector;
	}

	@Override
	protected final void doScan() {
		selector.addSelectorListener(new FileSelectorListener() {
			@Override
			public void selectorFindFile(File file, FileSelectorEvent e) {

				try {
					callStartedFile(file);

					logger.debug("Scan -> {}", file.getAbsolutePath());
					String data = FileUtils.readFileToString(file);
					doScan(file, data);

				} catch (TokenReadException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					callFinishedFile(file);
				}
			}
		});
		selector.select();
	}

	private void doScan(final File file, final String data) throws TokenReadException {

		StringBuffer buffer = new StringBuffer();
		int bufferIndex = 0; // トー化に該当しない最初の位置を保持
		boolean active = false;
		for (int i = 0; i < data.length(); i++) {
			if (!active) {
				active = reader.is(i, data);
				if (active) {
					if (isNotBlank(buffer)) {
						callFindToken(new Token(bufferIndex, buffer.toString()), file);
						buffer = new StringBuffer();
					}
				}
			}

			if (active) {
				int j = reader.read(i, data);
				i = j - 1;

				callFindToken(reader.getToken(), file);

				active = false;
			} else {
				if (isBlank(buffer)) {
					bufferIndex = i;
				}
				buffer.append(data.charAt(i));
			}

		}

		if (isNotBlank(buffer)) {
			callFindToken(new Token(bufferIndex, buffer.toString()), file);
		}
	}

}
