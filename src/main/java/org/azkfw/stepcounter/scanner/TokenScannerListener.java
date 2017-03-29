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

import java.io.File;

import org.azkfw.stepcounter.token.Token;

/**
 * このインターフェースは、トークンスキャナのイベントを定義したリスナーインターフェースです。
 *
 * @author Kawakicchi
 */
public interface TokenScannerListener {

	/**
	 * スキャン開始時に呼び出される。
	 * 
	 * @param event イベント情報
	 */
	public void tokenScannerStarted(TokenScannerEvent event);

	/**
	 * スキャン終了時に呼び出される。
	 * 
	 * @param event イベント情報
	 */
	public void tokenScannerFinished(TokenScannerEvent event);

	/**
	 * スキャン開始時に呼び出される。
	 * 
	 * @param file スキャンファイル
	 * @param event イベント情報
	 */
	public void tokenScannerStartedFile(File file, TokenScannerEvent event);

	/**
	 * スキャン終了時に呼び出される。
	 * 
	 * @param file スキャンファイル
	 * @param event イベント情報
	 */
	public void tokenScannerFinishedFile(File file, TokenScannerEvent event);

	/**
	 * トークンが見つかった場合に呼び出される。
	 *
	 * @param token トークン
	 * @param file スキャンファイル
	 * @param event イベント情報
	 */
	public void tokenScannerFindToken(Token token, File file, TokenScannerEvent event);
}
