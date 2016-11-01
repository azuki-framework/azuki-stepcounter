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
package org.azkfw.stepcounter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.azkfw.stepcounter.reader.BlankTokenReader;
import org.azkfw.stepcounter.reader.CommentTokenReader;
import org.azkfw.stepcounter.reader.ReturnLineTokenReader;
import org.azkfw.stepcounter.scanner.DefaultTokenScanner;
import org.azkfw.stepcounter.scanner.TokenScannerEvent;
import org.azkfw.stepcounter.scanner.TokenScannerListener;
import org.azkfw.stepcounter.token.Token;

public class StepCounter {

	public static void main(final String[] args) {
		StepCounter stepcounter = new StepCounter();
		stepcounter.count();
	}

	public void count() {
		File file = new File("");

		File file1 = new File("");

		DefaultTokenScanner scanner = new DefaultTokenScanner();
		scanner.setFile(file);

		List<Token> tokens = new ArrayList<Token>();

		scanner.addTokenScannerListener(new TokenScannerListener() {

			@Override
			public void tokenScannerStarted(TokenScannerEvent event) {
			}

			@Override
			public void tokenScannerFinished(TokenScannerEvent event) {
				boolean enableFlag = false;
				int cnt = 0;
				for (Token token : tokens) {

					if (null != token.getReader() && BlankTokenReader.class.isAssignableFrom(token.getReader())) {
						continue;
					}
					if (null != token.getReader() && CommentTokenReader.class.isAssignableFrom(token.getReader())) {
						continue;
					}

					if (null != token.getReader() && ReturnLineTokenReader.class.isAssignableFrom(token.getReader())) {
						if (enableFlag) {
							cnt++;
						}
						enableFlag = false;
						continue;
					}

					enableFlag = true;

					System.out.println(token.getWord());
				}
				if (enableFlag) {
					cnt++;
				}

				System.out.println(String.format("有効行 %d", cnt));
			}

			@Override
			public void tokenScannerFindToken(Token token, TokenScannerEvent event) {
				tokens.add(token);
			}
		});
		scanner.scan();

	}

}
