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
package org.azkfw.stepcounter.utils;

import java.util.Collection;

/**
 * @author kawakicchi
 */
public class AzukiUtil {

	public static boolean isNull(final Object object) {
		return (null == object);
	}

	public static boolean isNotNull(final Object object) {
		return !(isNull(object));
	}

	public static boolean isBlank(final String string) {
		return (null == string || 0 == string.length());
	}

	public static boolean isNotBlank(final String string) {
		return !(isBlank(string));
	}

	public static boolean isBlank(final StringBuffer sb) {
		return (null == sb || 0 == sb.length());
	}

	public static boolean isNotBlank(final StringBuffer sb) {
		return !(isBlank(sb));
	}

	public static boolean isEmpty(final Collection<?> collection) {
		return (null == collection || 0 == collection.size());
	}

	public static boolean isNotEmpty(final Collection<?> collection) {
		return !(isEmpty(collection));
	}

	public static boolean isAllNotNull(final Object... objects) {
		for (Object object : objects) {
			if (isNull(object)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAllNull(final Object... objects) {
		for (Object object : objects) {
			if (isNotNull(object)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAnyNull(final Object... objects) {
		for (Object object : objects) {
			if (isNull(object)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEqual(final String string1, final String string2) {
		if (isAnyNull(string1, string2)) {
			return false;
		}
		return string1.equals(string2);
	}

	public static boolean isNotEqual(final String string1, final String string2) {
		return !(isEqual(string1, string2));
	}

	public static boolean isAnyEqual(final String string, final String... strings) {
		for (String s : strings) {
			if (isEqual(string, s)) {
				return true;
			}
		}
		return false;
	}
}
