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
import java.util.ArrayList;
import java.util.List;

import org.azkfw.stepcounter.token.Token;

/**
 * このクラスは、トークンスキャナ機能を実装するための基底クラスです。
 *
 * @author Kawakicchi
 */
public abstract class AbstractTokenScanner implements TokenScanner, TokenScannerListenerSupport {

	/** イベント情報 */
	private final TokenScannerEvent event;

	/** リスナー */
	private final List<TokenScannerListener> listeners;

	/**
	 * コンストラクタ
	 */
	public AbstractTokenScanner() {
		event = new TokenScannerEvent(this);
		listeners = new ArrayList<TokenScannerListener>();
	}

	@Override
	public synchronized final void addTokenScannerListener(final TokenScannerListener listener) {
		listeners.add(listener);
	}

	@Override
	public synchronized void removeTokenScannerListener(final TokenScannerListener listener) {
		listeners.remove(listener);
	}

	@Override
	public final void scan() {

		try {
			callStarted();

			doScan();

		} finally {
			callFinished();
		}
	}

	/**
	 * イベントリスナーを呼び出す
	 *
	 * @param token
	 */
	protected final void callFindToken(final Token token, final File file) {
		synchronized (listeners) {
			listeners.forEach(listener -> listener.tokenScannerFindToken(token, file, event));
		}
	}

	protected final void callStartedFile(final File file) {
		synchronized (listeners) {
			listeners.forEach(listener -> listener.tokenScannerStartedFile(file, event));
		}
	}

	protected final void callFinishedFile(final File file) {
		synchronized (listeners) {
			listeners.forEach(listener -> listener.tokenScannerFinishedFile(file, event));
		}
	}

	private void callStarted() {
		synchronized (listeners) {
			listeners.forEach(listener -> listener.tokenScannerStarted(event));
		}
	}

	private void callFinished() {
		synchronized (listeners) {
			listeners.forEach(listener -> listener.tokenScannerFinished(event));
		}
	}

	/**
	 * スキャン処理を行う。
	 */
	protected abstract void doScan();
}
