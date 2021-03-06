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

import org.azkfw.stepcounter.token.Token;

/**
 * @author kawakicchi
 */
public interface TokenReader {

	public void clear();

	/**
	 * 読み込み可能か判断する。
	 * 
	 * @param index 読み込み開始位置
	 * @param data データ
	 * @return 読み込み可能な場合、<code>true</code>を返す。
	 */
	public boolean is(final int index, final String data);

	/**
	 * 読み込みを行う。
	 * 
	 * @param index 読み込み開始位置
	 * @param data データ
	 * @return 読み込み終了位置
	 * @exception TokenReadException 読み込み中に問題が発生した場合
	 */
	public int read(final int index, final String data) throws TokenReadException;

	public Token getToken();
}