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
package org.azkfw.stepcounter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.azkfw.stepcounter.reader.BlankTokenReader;
import org.azkfw.stepcounter.reader.CommentTokenReader;
import org.azkfw.stepcounter.reader.MultiLineCommentTokenReader;
import org.azkfw.stepcounter.reader.MultiTokenReader;
import org.azkfw.stepcounter.reader.ReturnLineTokenReader;
import org.azkfw.stepcounter.reader.SingleLineCommentTokenReader;
import org.azkfw.stepcounter.reader.StringTokenReader;
import org.azkfw.stepcounter.scanner.DefaultTokenScanner;
import org.azkfw.stepcounter.scanner.TokenScannerEvent;
import org.azkfw.stepcounter.scanner.TokenScannerListener;
import org.azkfw.stepcounter.selector.MultiFileSelector;
import org.azkfw.stepcounter.selector.PatternFileSelector;
import org.azkfw.stepcounter.token.Token;

/**
 * @author kawakicchi
 */
public class StepCounter {

	public static void main(final String[] args) {
		StepCounter stepcounter = new StepCounter();
		stepcounter.count(new File(args[0]));
	}

	public void count(final File file) {
		final MultiTokenReader.Builder builder = MultiTokenReader.Builder.newInstance();
		builder.addReader(new SingleLineCommentTokenReader());
		builder.addReader(new MultiLineCommentTokenReader());
		builder.addReader(new StringTokenReader());
		builder.addReader(new ReturnLineTokenReader());
		builder.addReader(new BlankTokenReader());

		final PatternFileSelector selector1 = new PatternFileSelector();
		selector1.setDir(file);
		selector1.addInclude("**/*.java");

		MultiFileSelector selecter = new MultiFileSelector();
		selecter.addSelector(selector1);

		DefaultTokenScanner scanner = new DefaultTokenScanner(builder.build(), selecter);

		List<Token> tokens = new ArrayList<Token>();

		scanner.addTokenScannerListener(new TokenScannerListener() {

			@Override
			public void tokenScannerStarted(TokenScannerEvent event) {
			}

			@Override
			public void tokenScannerStartedFile(File file, TokenScannerEvent event) {
				tokens.clear();
			}

			@Override
			public void tokenScannerFinished(TokenScannerEvent event) {
			}

			@Override
			public void tokenScannerFinishedFile(File file, TokenScannerEvent event) {
				boolean enableFlag = false;
				int cntEnableLine = 0;
				int cntTotalLine = 0;

				StringBuffer line = new StringBuffer();
				for (Token token : tokens) {

					if (null != token.getReader() && BlankTokenReader.class.isAssignableFrom(token.getReader())) {
						line.append(token.getWord());
						continue;
					}
					if (null != token.getReader() && CommentTokenReader.class.isAssignableFrom(token.getReader())) {
						continue;
					}

					if (null != token.getReader() && ReturnLineTokenReader.class.isAssignableFrom(token.getReader())) {
						cntTotalLine++;
						if (enableFlag) {
							// System.out.println(line);
							cntEnableLine++;
						}
						line = new StringBuffer();
						enableFlag = false;
						continue;
					}

					enableFlag = true;

					line.append(token.getWord());
				}
				if (enableFlag) {
					cntEnableLine++;
				}

				System.out.println(String.format("有効行 %d(%.1f%%) %s", cntEnableLine, (((float) cntEnableLine) * 100.f / ((float) cntTotalLine)),
						file.getAbsolutePath()));
			}

			@Override
			public void tokenScannerFindToken(Token token, File file, TokenScannerEvent event) {
				tokens.add(token);
			}
		});
		scanner.scan();

	}

}
