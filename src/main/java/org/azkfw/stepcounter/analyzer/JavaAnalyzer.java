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
package org.azkfw.stepcounter.analyzer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.azkfw.stepcounter.analyzer.java.BlockStatement;
import org.azkfw.stepcounter.analyzer.java.ClassStatement;
import org.azkfw.stepcounter.analyzer.java.CommentStatement;
import org.azkfw.stepcounter.analyzer.java.FieldStatement;
import org.azkfw.stepcounter.analyzer.java.ForStatement;
import org.azkfw.stepcounter.analyzer.java.IfStatement;
import org.azkfw.stepcounter.analyzer.java.ImportStatement;
import org.azkfw.stepcounter.analyzer.java.JavaStatement;
import org.azkfw.stepcounter.analyzer.java.PackageStatement;
import org.azkfw.stepcounter.analyzer.java.Statement;
import org.azkfw.stepcounter.analyzer.java.UndefinedBlockStatement;
import org.azkfw.stepcounter.analyzer.java.UndefinedStatement;
import org.azkfw.stepcounter.reader.BlankTokenReader;
import org.azkfw.stepcounter.reader.CommentTokenReader;
import org.azkfw.stepcounter.reader.MultiLineCommentTokenReader;
import org.azkfw.stepcounter.reader.MultiTokenReader;
import org.azkfw.stepcounter.reader.ReturnLineTokenReader;
import org.azkfw.stepcounter.reader.SeparatorTokenReader;
import org.azkfw.stepcounter.reader.SingleLineCommentTokenReader;
import org.azkfw.stepcounter.reader.StringTokenReader;
import org.azkfw.stepcounter.scanner.DefaultTokenScanner;
import org.azkfw.stepcounter.scanner.TokenScannerEvent;
import org.azkfw.stepcounter.scanner.TokenScannerListener;
import org.azkfw.stepcounter.selector.MultiFileSelector;
import org.azkfw.stepcounter.selector.PatternFileSelector;
import org.azkfw.stepcounter.token.Token;
import org.azkfw.stepcounter.utils.AzukiUtil;

/**
 * @author kawakicchi
 */
public class JavaAnalyzer<T> extends HashMap<String, String> implements Serializable, TestInterface<String> {

	/** serialVersionUID */
	private static final long serialVersionUID = 633522360444496657L;

	public static void main(final String[] args) {
		JavaAnalyzer ana = new JavaAnalyzer();
		ana.start(new File(args[0]));
	}

	@Override
	public String test(/* コメント */) {
		// TODO Auto-generated method stub
		return null;
	}

	public JavaAnalyzer() {

	}

	public void start(final File srcFile) {
		final MultiTokenReader.Builder builder = MultiTokenReader.Builder.newInstance();
		builder.addReader(new SingleLineCommentTokenReader());
		builder.addReader(new MultiLineCommentTokenReader());
		builder.addReader(new StringTokenReader());
		builder.addReader(new ReturnLineTokenReader());
		builder.addReader(new SeparatorTokenReader());
		builder.addReader(new BlankTokenReader());

		final PatternFileSelector selector1 = new PatternFileSelector();
		selector1.setDir(srcFile);
		//selector1.addInclude("**/*.java");
		selector1.addInclude("**/JavaAnalyzer.java");
		//selector1.addInclude("**/SampleClass.java");

		MultiFileSelector selecter = new MultiFileSelector();
		selecter.addSelector(selector1);

		DefaultTokenScanner scanner = new DefaultTokenScanner(builder.build(), selecter);

		Map<File, List<Token>> fileTokens = new HashMap<File, List<Token>>();

		scanner.addTokenScannerListener(new TokenScannerListener() {
			private List<Token> tokens;

			@Override
			public void tokenScannerStarted(TokenScannerEvent event) {
			}

			@Override
			public void tokenScannerStartedFile(File file, TokenScannerEvent event) {
				tokens = new ArrayList<Token>();
			}

			@Override
			public void tokenScannerFinished(TokenScannerEvent event) {
			}

			@Override
			public void tokenScannerFinishedFile(File file, TokenScannerEvent event) {
				fileTokens.put(file, tokens);
			}

			@Override
			public void tokenScannerFindToken(Token token, File file, TokenScannerEvent event) {
				tokens.add(token);
			}
		});
		scanner.scan();

		fileTokens.forEach((file, tokens) -> {
			stepcount(file, tokens);
			analyze(file, tokens);
		});
	}

	private void stepcount(final File file, List<Token> tokens) {

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

		System.out.println(String.format("有効行 %05d (%.1f%%) %s", cntEnableLine, (((float) cntEnableLine) * 100.f / ((float) cntTotalLine)),
				file.getAbsolutePath()));
	}

	private void analyze(final File file, List<Token> tokens) {
		UndefinedBlockStatement statement = new UndefinedBlockStatement(null, null);

		int result = parseBlockStatement(0, tokens, statement);
		if (result != tokens.size()) {
			System.out.println("Unmatch!!");
		}

		JavaStatement block = new JavaStatement();
		analyzeBlockStatement(null, block, statement);

		System.out.println(block);
	}

	private void analyzeBlockStatement(final Statement pa, final BlockStatement block, final UndefinedBlockStatement parent) {
		int i = 0;
		final List<UndefinedStatement> statements = parent.getStatements();
		for (; i < statements.size();) {
			final UndefinedStatement stat1 = statements.get(i);

			if (stat1 instanceof UndefinedBlockStatement) {
				BlockStatement bl = new BlockStatement();
				analyzeBlockStatement(null, bl, (UndefinedBlockStatement) stat1);
				block.addStatement(bl);
				i++;
				continue;
			}

			final List<Token> tokens = stat1.getTokens();
			final Token token = tokens.get(0);

			if (1 == tokens.size() && null != token.getReader() && CommentTokenReader.class.isAssignableFrom(token.getReader())) {
				block.addStatement(new CommentStatement(token));
				i++;
				continue;
			} else if (null != pa && ClassStatement.class.isAssignableFrom(pa.getClass()) && AzukiUtil.isEqual(";", tokens.get(tokens.size() - 1).getWord())) {
				block.addStatement(new FieldStatement(tokens));
				i++;
				continue;
			} else if (AzukiUtil.isAnyEqual(token.getWord(), "import")) {
				block.addStatement(new ImportStatement(tokens));
				i++;
				continue;

			} else if (AzukiUtil.isAnyEqual(token.getWord(), "package")) {
				block.addStatement(new PackageStatement(tokens));
				i++;
				continue;

			} else if (AzukiUtil.isAnyEqual(token.getWord(), "if", "for")) {
				if (i + 2 >= statements.size()) {
					// XXX: 異常系
					i++;
					continue;
				}

				final UndefinedStatement stat2 = statements.get(i + 1);
				if (!(stat2 instanceof UndefinedBlockStatement) || AzukiUtil.isNotEqual(((UndefinedBlockStatement) stat2).getPrefixBlockToken().getWord(), "(")) {
					// XXX: 異常系
					i++;
					continue;
				}

				final UndefinedStatement stat3 = statements.get(i + 2);
				if (stat3 instanceof UndefinedBlockStatement) {
					if (AzukiUtil.isNotEqual(((UndefinedBlockStatement) stat3).getPrefixBlockToken().getWord(), "{")) {
						// XXX: パターン考慮
						i++;
						continue;
					}
				} else if (!(stat3 instanceof UndefinedStatement)) {
					// XXX: パターン考慮
					i++;
					continue;
				}

				Statement s = null;
				BlockStatement condition = new BlockStatement();
				Statement process = null;
				if (stat3 instanceof UndefinedBlockStatement) {
					process = new BlockStatement();
				} else {
					process = stat3;// TODO: 
				}

				if (AzukiUtil.isAnyEqual(token.getWord(), "if")) {
					s = new IfStatement(condition, process);

				} else if (AzukiUtil.isAnyEqual(token.getWord(), "for")) {
					s = new ForStatement(condition, process);
				}

				analyzeBlockStatement(s, condition, (UndefinedBlockStatement) stat2);
				if (stat3 instanceof UndefinedBlockStatement) {
					analyzeBlockStatement(s, (BlockStatement) process, (UndefinedBlockStatement) stat3);
				}

				block.addStatement(s);

				i += 3;
				continue;
			} else if (isTokenWord("class", tokens)) {
				if (i + 1 >= statements.size()) {
					// XXX: 異常系
					i++;
					continue;
				}

				final UndefinedStatement stat2 = statements.get(i + 1);
				if (!(stat2 instanceof UndefinedBlockStatement) || AzukiUtil.isNotEqual(((UndefinedBlockStatement) stat2).getPrefixBlockToken().getWord(), "{")) {
					// XXX: 異常系
					i++;
					continue;
				}

				Statement def = stat1;
				BlockStatement process = new BlockStatement();
				ClassStatement s = new ClassStatement(def, process);

				analyzeBlockStatement(s, process, (UndefinedBlockStatement) stat2);

				block.addStatement(s);

				i += 2;
				continue;
			} else {

				block.addStatement(stat1);
			}

			i++;
		}
	}

	private boolean isTokenWord(String word, List<Token> tokens) {
		for (Token token : tokens) {
			if (AzukiUtil.isEqual(token.getWord(), word)) {
				return true;
			}
		}
		return false;
	}

	private int parseBlockStatement(final int index, final List<Token> tokens, final UndefinedBlockStatement me) {
		int i = index;

		final List<Token> bufTokens = new ArrayList<Token>();
		for (; i < tokens.size();) {
			final Token token = tokens.get(i);
			final String word = token.getWord();

			if (me.isSuffixToken(token)) {
				if (AzukiUtil.isNotEmpty(bufTokens)) {
					me.addStatement(new UndefinedStatement(bufTokens));
					bufTokens.clear();
				}
				me.setBlockToken(tokens.get(index - 1), tokens.get(i));
				return i + 1;
			}

			if (null == token.getReader()) {
			} else if (BlankTokenReader.class.isAssignableFrom(token.getReader())) {
				i++;
				continue;
			} else if (ReturnLineTokenReader.class.isAssignableFrom(token.getReader())) {
				i++;
				continue;
			} else if (CommentTokenReader.class.isAssignableFrom(token.getReader())) {
				if (AzukiUtil.isEmpty(bufTokens)) {
					me.addStatement(new UndefinedStatement(token));
					i++;
					continue;
				}
			} else if (SeparatorTokenReader.class.isAssignableFrom(token.getReader())) {
				if (AzukiUtil.isEqual(word, "(")) {
					if (AzukiUtil.isNotEmpty(bufTokens)) {
						me.addStatement(new UndefinedStatement(bufTokens));
						bufTokens.clear();
					}

					UndefinedBlockStatement child = new UndefinedBlockStatement("(", ")");
					me.addStatement(child);
					int i2 = parseBlockStatement(i + 1, tokens, child);
					i = i2;
					continue;
				} else if (AzukiUtil.isEqual(word, ")")) {
					if (AzukiUtil.isNotEmpty(bufTokens)) {
						me.addStatement(new UndefinedStatement(bufTokens));
						bufTokens.clear();
					}

					return i;
				} else if (AzukiUtil.isEqual(word, "{")) {
					if (AzukiUtil.isNotEmpty(bufTokens)) {
						me.addStatement(new UndefinedStatement(bufTokens));
						bufTokens.clear();
					}

					UndefinedBlockStatement child = new UndefinedBlockStatement("{", "}");
					me.addStatement(child);
					int i2 = parseBlockStatement(i + 1, tokens, child);
					i = i2;
					continue;
				} else if (AzukiUtil.isEqual(word, "}")) {
					if (AzukiUtil.isNotEmpty(bufTokens)) {
						me.addStatement(new UndefinedStatement(bufTokens));
						bufTokens.clear();
					}

					return i;
				}
			}

			if (AzukiUtil.isAnyEqual(word, ";")) {
				bufTokens.add(token);
				me.addStatement(new UndefinedStatement(bufTokens));
				bufTokens.clear();
				i++;
				continue;
			}

			bufTokens.add(token);
			i++;
		}

		if (AzukiUtil.isNotEmpty(bufTokens)) {
			me.addStatement(new UndefinedStatement(bufTokens));
			bufTokens.clear();
		}
		return i;
	}

}
