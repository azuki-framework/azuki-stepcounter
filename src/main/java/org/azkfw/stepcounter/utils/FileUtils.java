/*
 * Licensed to the Apache Software Fimport java.io.File;
import java.io.IOException;

import org.mozilla.universalchardet.UniversalDetector;
th this work for additional information
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
package org.azkfw.stepcounter.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.universalchardet.UniversalDetector;

public class FileUtils {

	public static final String ENCODING_MS932 = "MS932";

	public static final String defaultEncoding = "MS932";

	public static String lastEncoding;

	public static String getFileEncoding(final File file, final String defaultEncoding) {
		String encoding = defaultEncoding;

		java.io.FileInputStream fis = null;
		try {
			fis = new java.io.FileInputStream(file);
			byte[] buf = new byte[4096];
			UniversalDetector detector = new UniversalDetector(null);

			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}

			detector.dataEnd();

			encoding = detector.getDetectedCharset();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return encoding;
	}

	public static String readFileToString(final File file) throws IOException {
		final String encode = getFileEncoding(file, defaultEncoding);

		String text = org.apache.commons.io.FileUtils.readFileToString(file, encode);

		if (text.charAt(0) == 65279) {// UTF-8 marker
			text = text.substring(1);
		}
		return text;
	}

	public static List<String> readFileToStrings(final File file) throws IOException {
		final String encode = getFileEncoding(file, defaultEncoding);

		final List<String> lines = new ArrayList<String>();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
			String line = null;
			while (null != (line = reader.readLine())) {
				lines.add(line);
			}

			if (0 < lines.size() && 0 < lines.get(0).length()) {
				String l = lines.get(0);
				if (l.charAt(0) == 65279) {// UTF-8 marker
					l = l.substring(1);
					lines.set(0, l);
				}
			}

		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		return lines;
	}
}