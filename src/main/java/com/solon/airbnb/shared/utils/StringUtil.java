/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solon.airbnb.shared.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;





public abstract class StringUtil {
	static Logger log = LoggerFactory.getLogger(StringUtil.class);

    private static final long serialVersionUID = 1L;
    private static StringBuffer strb;
    private static StringCharacterIterator sci;
    
    private static final String[] EMPTY_STRING_ARRAY = {};

	private static final String FOLDER_SEPARATOR = "/";

	private static final char FOLDER_SEPARATOR_CHAR = '/';

	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	private static final String TOP_PATH = "..";

	private static final String CURRENT_PATH = ".";

	private static final char EXTENSION_SEPARATOR = '.';

	

    public static String FormatDouble(double input, String format) {
        NumberFormat formatter = new DecimalFormat(format);
        return formatter.format(input);
    }

    public static String FormatDecimal(BigDecimal input, String format) {
        NumberFormat formatter = new DecimalFormat(format);
        return formatter.format(input);
    }

    public static String FormatLong(long input, String format) {
        NumberFormat formatter = new DecimalFormat(format);
        return formatter.format(input);
    }

    public static String FormatInt(int input, String format) {
        NumberFormat formatter = new DecimalFormat(format);
        return formatter.format(input);
    }

    private static String convert_charset(String inputString, String inputCharSet, String outputCharSet) {
        String returnString = "";
        // Create the encoder and decoder for inputCharSet
        Charset charset_input = Charset.forName(inputCharSet.trim());
        Charset charset_output = Charset.forName(outputCharSet.trim());
        CharsetEncoder encoder = charset_input.newEncoder();
        CharsetDecoder decoder = charset_output.newDecoder();
        try {
            // Convert a string to inputCharSet bytes in a ByteBuffer
            // The new ByteBuffer is ready to be read.
            ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(inputString));
            // Convert inputCharSet bytes in a ByteBuffer to an outputCharSet character ByteBuffer and then to a string.
            // The new ByteBuffer is ready to be read.
            CharBuffer cbuf = decoder.decode(bbuf);
            returnString = cbuf.toString();
        } catch (CharacterCodingException e) {
            returnString = inputString;
            //e.printStackTrace();
        }
        return returnString;
    }

    public static String convert(String inputString, String inputCharSet, String outputCharSet) {
        if (inputString == null) {
            return inputString;
        }
        String returnString = "";
        StringBuilder sbtext = new StringBuilder();
        int len = inputString.length();
        for (int t = 0; t < len; t++) {
            String srofc = inputString.substring(t, t + 1);
            sbtext.append(convert_charset(srofc, inputCharSet, outputCharSet));
        }
        returnString = sbtext.toString();

        return returnString;
    }

    public static String replaceAll(String str, String pattern, String replace) {
        //This is a better replaceAll than the String.replaceAll()
        //the native replaceAll function does not work poperly....
        //try "100.00".replaceAll(".00","") it will give empty string ( "" )
        int s = 0;
        int e = 0;
        StringBuilder result = new StringBuilder();

        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

    public static String replaceFilenameInvalidSysmbols(String filename) {
        String retValue = filename;
        retValue = replaceAll(retValue, "\\", " ");
        retValue = replaceAll(retValue, "/", " ");
        retValue = replaceAll(retValue, ":", " ");
        retValue = replaceAll(retValue, "*", " ");
        retValue = replaceAll(retValue, "?", " ");
        retValue = replaceAll(retValue, "<", " ");
        retValue = replaceAll(retValue, ">", " ");
        retValue = replaceAll(retValue, "|", " ");
        return retValue;
    }

    public String join(String[] inputArray, String delimeter) {
        StringBuilder newString = new StringBuilder();
        for (String inputArray1 : inputArray) {
            newString.append(inputArray1);
            newString.append(delimeter);
        }
        return newString.toString();
    }

    public static int countOccurences(String str, String pattern) {
        //This is a better replaceAll than the String.replaceAll()
        //the native replaceAll function does not work poperly....
        //try "100.00".replaceAll(".00","") it will give empty string ( "" )
        int s = 0;
        int e = 0;
        int counts = 0;

        while ((e = str.indexOf(pattern, s)) >= 0) {
            //result.append(str.substring(s, e));
            //result.append(replace);
            s = e + pattern.length();
            counts++;
        }

        return counts;
    }

    /**
     * method to left pad a string with a given string to a given size. This
     * method will repeat the padder string as many times as is necessary until
     * the exact specified size is reached. If the specified size is less than
     * the size of the original string then the original string is returned
     * unchanged. Example1 - original string "cat", padder string "white", size
     * 8 gives "whitecat". Example2 - original string "cat", padder string
     * "white", size 15 gives "whitewhitewhcat". Example3 - original string
     * "cat", padder string "white", size 2 gives "cat".
     *
     * @return String the newly padded string
     * @param stringToPad The original string
     * @param padder The string to pad onto the original string
     * @param size The required size of the new string
     */
    public static String padLeft(String stringToPad, String padder, int size) {
        if (padder.length() == 0) {
            return stringToPad;
        }
        strb = new StringBuffer(size);
        sci = new StringCharacterIterator(padder);

        while (strb.length() < (size - stringToPad.length())) {
            for (char ch = sci.first(); ch != CharacterIterator.DONE; ch = sci.next()) {
                if (strb.length() < size - stringToPad.length()) {
                    strb.insert(strb.length(), String.valueOf(ch));
                }
            }
        }
        return strb.append(stringToPad).toString();
    }

    /**
     * method to right pad a string with a given string to a given size. This
     * method will repeat the padder string as many times as is necessary until
     * the exact specified size is reached. If the specified size is less than
     * the size of the original string then the original string is returned
     * unchanged. Example1 - original string "cat", padder string "white", size
     * 8 gives "catwhite". Example2 - original string "cat", padder string
     * "white", size 15 gives "catwhitewhitewh". Example3 - original string
     * "cat", padder string "white", size 2 gives "cat".
     *
     * @return String the newly padded string
     * @param stringToPad The original string
     * @param padder The string to pad onto the original string
     * @param size The required size of the new string
     */
    public static String padRight(String stringToPad, String padder, int size) {
        if (padder.length() == 0) {
            return stringToPad;
        }
        strb = new StringBuffer(stringToPad);
        sci = new StringCharacterIterator(padder);

        while (strb.length() < size) {
            for (char ch = sci.first(); ch != CharacterIterator.DONE; ch = sci.next()) {
                if (strb.length() < size) {
                    strb.append(String.valueOf(ch));
                }
            }
        }
        return strb.toString();
    }

    public static String reverseIt(String source) {
        int i, len = source.length();
        StringBuilder dest = new StringBuilder(len);

        for (i = (len - 1); i >= 0; i--) {
            dest.append(source.charAt(i));
        }
        return dest.toString();
    }

    //---------------------------------------------------------------------
    // General convenience methods for working with Strings
    //---------------------------------------------------------------------
    /**
	 * Check that the given {@code CharSequence} is neither {@code null} nor
	 * of length 0.
	 * <p>Note: this method returns {@code true} for a {@code CharSequence}
	 * that purely consists of whitespace.
	 * <p><pre class="code">
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 * @param str the {@code CharSequence} to check (may be {@code null})
	 * @return {@code true} if the {@code CharSequence} is not {@code null} and has length
	 * @see #hasLength(String)
	 * @see #hasText(CharSequence)
	 */
	public static boolean hasLength(@Nullable CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * Check that the given {@code String} is neither {@code null} nor of length 0.
	 * <p>Note: this method returns {@code true} for a {@code String} that
	 * purely consists of whitespace.
	 * @param str the {@code String} to check (may be {@code null})
	 * @return {@code true} if the {@code String} is not {@code null} and has length
	 * @see #hasLength(CharSequence)
	 * @see #hasText(String)
	 */
	public static boolean hasLength(@Nullable String str) {
		return (str != null && !str.isEmpty());
	}
	/**
	 * Check whether the given {@code CharSequence} contains actual <em>text</em>.
	 * <p>More specifically, this method returns {@code true} if the
	 * {@code CharSequence} is not {@code null}, its length is greater than
	 * 0, and it contains at least one non-whitespace character.
	 * <p><pre class="code">
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText("") = false
	 * StringUtils.hasText(" ") = false
	 * StringUtils.hasText("12345") = true
	 * StringUtils.hasText(" 12345 ") = true
	 * </pre>
	 * @param str the {@code CharSequence} to check (may be {@code null})
	 * @return {@code true} if the {@code CharSequence} is not {@code null},
	 * its length is greater than 0, and it does not contain whitespace only
	 * @see #hasText(String)
	 * @see #hasLength(CharSequence)
	 * @see Character#isWhitespace
	 */
	public static boolean hasText(@Nullable CharSequence str) {
		return (str != null && str.length() > 0 && containsText(str));
	}

	/**
	 * Check whether the given {@code String} contains actual <em>text</em>.
	 * <p>More specifically, this method returns {@code true} if the
	 * {@code String} is not {@code null}, its length is greater than 0,
	 * and it contains at least one non-whitespace character.
	 * @param str the {@code String} to check (may be {@code null})
	 * @return {@code true} if the {@code String} is not {@code null}, its
	 * length is greater than 0, and it does not contain whitespace only
	 * @see #hasText(CharSequence)
	 * @see #hasLength(String)
	 * @see Character#isWhitespace
	 */
	public static boolean hasText(@Nullable String str) {
		return (str != null && !str.isEmpty() && containsText(str));
	}
	
	private static boolean containsText(CharSequence str) {
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

    /**
     * Check whether the given CharSequence contains any whitespace characters.
     *
     * @param str the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not empty and contains
     * at least 1 whitespace character
     * @see java.lang.Character#isWhitespace
     */
    public static boolean containsWhitespace(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given String contains any whitespace characters.
     *
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not empty and contains at
     * least 1 whitespace character
     * @see #containsWhitespace(CharSequence)
     */
    public static boolean containsWhitespace(String str) {
        return containsWhitespace((CharSequence) str);
    }

    /**
	 * Trim leading and trailing whitespace from the given {@code String}.
	 * @param str the {@code String} to check
	 * @return the trimmed {@code String}
	 * @see java.lang.Character#isWhitespace
	 */
    public static String trimWhitespace(String str) {
    	if (!hasLength(str)) {
			return str;
		}

		int beginIndex = 0;
		int endIndex = str.length() - 1;

		while (beginIndex <= endIndex && Character.isWhitespace(str.charAt(beginIndex))) {
			beginIndex++;
		}

		while (endIndex > beginIndex && Character.isWhitespace(str.charAt(endIndex))) {
			endIndex--;
		}

		return str.substring(beginIndex, endIndex + 1);
    }

    /**
	 * Trim <i>all</i> whitespace from the given {@code String}:
	 * leading, trailing, and in between characters.
	 * @param str the {@code String} to check
	 * @return the trimmed {@code String}
	 * @see java.lang.Character#isWhitespace
	 */
    public static String trimAllWhitespace(String str) {
    	if (!hasLength(str)) {
			return str;
		}

		int len = str.length();
		StringBuilder sb = new StringBuilder(str.length());
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (!Character.isWhitespace(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
    }

    /**
	 * Trim leading whitespace from the given {@code String}.
	 * @param str the {@code String} to check
	 * @return the trimmed {@code String}
	 * @see java.lang.Character#isWhitespace
	 */
    public static String trimLeadingWhitespace(String str) {
    	if (!hasLength(str)) {
			return str;
		}

		int beginIdx = 0;
		while (beginIdx < str.length() && Character.isWhitespace(str.charAt(beginIdx))) {
			beginIdx++;
		}
		return str.substring(beginIdx);
    }

    /**
	 * Trim trailing whitespace from the given {@code String}.
	 * @param str the {@code String} to check
	 * @return the trimmed {@code String}
	 * @see java.lang.Character#isWhitespace
	 */
    public static String trimTrailingWhitespace(String str) {
    	if (!hasLength(str)) {
			return str;
		}

		int endIdx = str.length() - 1;
		while (endIdx >= 0 && Character.isWhitespace(str.charAt(endIdx))) {
			endIdx--;
		}
		return str.substring(0, endIdx + 1);
    }

    /**
	 * Trim all occurrences of the supplied leading character from the given {@code String}.
	 * @param str the {@code String} to check
	 * @param leadingCharacter the leading character to be trimmed
	 * @return the trimmed {@code String}
	 */
    public static String trimLeadingCharacter(String str, char leadingCharacter) {
    	if (!hasLength(str)) {
			return str;
		}

		int beginIdx = 0;
		while (beginIdx < str.length() && leadingCharacter == str.charAt(beginIdx)) {
			beginIdx++;
		}
		return str.substring(beginIdx);
    }

    /**
	 * Trim all occurrences of the supplied trailing character from the given {@code String}.
	 * @param str the {@code String} to check
	 * @param trailingCharacter the trailing character to be trimmed
	 * @return the trimmed {@code String}
	 */
    public static String trimTrailingCharacter(String str, char trailingCharacter) {
    	if (!hasLength(str)) {
			return str;
		}

		int endIdx = str.length() - 1;
		while (endIdx >= 0 && trailingCharacter == str.charAt(endIdx)) {
			endIdx--;
		}
		return str.substring(0, endIdx + 1);
    }
    
    /**
	 * Test if the given {@code String} matches the given single character.
	 * @param str the {@code String} to check
	 * @param singleCharacter the character to compare to
	 * @since 5.2.9
	 */
	public static boolean matchesCharacter(@Nullable String str, char singleCharacter) {
		return (str != null && str.length() == 1 && str.charAt(0) == singleCharacter);
	}

	/**
	 * Test if the given {@code String} starts with the specified prefix,
	 * ignoring upper/lower case.
	 * @param str the {@code String} to check
	 * @param prefix the prefix to look for
	 * @see java.lang.String#startsWith
	 */
	public static boolean startsWithIgnoreCase(@Nullable String str, @Nullable String prefix) {
		return (str != null && prefix != null && str.length() >= prefix.length() &&
				str.regionMatches(true, 0, prefix, 0, prefix.length()));
	}

	/**
	 * Test if the given {@code String} ends with the specified suffix,
	 * ignoring upper/lower case.
	 * @param str the {@code String} to check
	 * @param suffix the suffix to look for
	 * @see java.lang.String#endsWith
	 */
	public static boolean endsWithIgnoreCase(@Nullable String str, @Nullable String suffix) {
		return (str != null && suffix != null && str.length() >= suffix.length() &&
				str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length()));
	}

	/**
	 * Test whether the given string matches the given substring
	 * at the given index.
	 * @param str the original string (or StringBuilder)
	 * @param index the index in the original string to start matching against
	 * @param substring the substring to match at the given index
	 */
	public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
		if (index + substring.length() > str.length()) {
			return false;
		}
		for (int i = 0; i < substring.length(); i++) {
			if (str.charAt(index + i) != substring.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Count the occurrences of the substring {@code sub} in string {@code str}.
	 * @param str string to search in
	 * @param sub string to search for
	 */
	public static int countOccurrencesOf(String str, String sub) {
		if (!hasLength(str) || !hasLength(sub)) {
			return 0;
		}

		int count = 0;
		int pos = 0;
		int idx;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	/**
	 * Replace all occurrences of a substring within a string with another string.
	 * @param inString {@code String} to examine
	 * @param oldPattern {@code String} to replace
	 * @param newPattern {@code String} to insert
	 * @return a {@code String} with the replacements
	 */
	public static String replace(String inString, String oldPattern, @Nullable String newPattern) {
		if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
			return inString;
		}
		int index = inString.indexOf(oldPattern);
		if (index == -1) {
			// no occurrence -> can return input as-is
			return inString;
		}

		int capacity = inString.length();
		if (newPattern.length() > oldPattern.length()) {
			capacity += 16;
		}
		StringBuilder sb = new StringBuilder(capacity);

		int pos = 0;  // our position in the old string
		int patLen = oldPattern.length();
		while (index >= 0) {
			sb.append(inString, pos, index);
			sb.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}

		// append any characters to the right of a match
		sb.append(inString, pos, inString.length());
		return sb.toString();
	}

    /**
     * Delete all occurrences of the given substring.
     *
     * @param inString the original String
     * @param pattern the pattern to delete all occurrences of
     * @return the resulting String
     */
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    /**
	 * Delete any character in a given {@code String}.
	 * @param inString the original {@code String}
	 * @param charsToDelete a set of characters to delete.
	 * E.g. "az\n" will delete 'a's, 'z's and new lines.
	 * @return the resulting {@code String}
	 */
	public static String deleteAny(String inString, @Nullable String charsToDelete) {
		if (!hasLength(inString) || !hasLength(charsToDelete)) {
			return inString;
		}

		int lastCharIndex = 0;
		char[] result = new char[inString.length()];
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				result[lastCharIndex++] = c;
			}
		}
		if (lastCharIndex == inString.length()) {
			return inString;
		}
		return new String(result, 0, lastCharIndex);
	}

    //---------------------------------------------------------------------
    // Convenience methods for working with formatted Strings
    //---------------------------------------------------------------------
    /**
     * Quote the given String with single quotes.
     *
     * @param str the input String (e.g. "myString")
     * @return the quoted String (e.g. "'myString'"), * * or <code>null<code> if the input was
     * <code>null</code>
     */
    public static String quote(String str) {
        return (str != null ? "'" + str + "'" : null);
    }

    /**
     * Turn the given Object into a String with single quotes if it is a String;
     * keeping the Object as-is else.
     *
     * @param obj the input Object (e.g. "myString")
     * @return the quoted String (e.g. "'myString'"), or the input object as-is
     * if not a String
     */
    public static Object quoteIfString(Object obj) {
        return (obj instanceof String ? quote((String) obj) : obj);
    }

    /**
     * Unqualify a string qualified by a '.' dot character.For example,
 "this.name.is.qualified", returns "qualified".
     *
     * @param qualifiedName the qualified name
     * @return 
     */
    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, '.');
    }

    /**
     * Unqualify a string qualified by a separator character. For example,
     * "this:name:is:qualified" returns "qualified" if using a ':' separator.
     *
     * @param qualifiedName the qualified name
     * @param separator the separator
     */
    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    /**
     * Capitalize a <code>String</code>, changing the first letter to upper case
     * as per {@link Character#toUpperCase(char)}. No other letters are changed.
     *
     * @param str the String to capitalize, may be <code>null</code>
     * @return the capitalized String, <code>null</code> if null
     */
    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    /**
     * Uncapitalize a <code>String</code>, changing the first letter to lower
     * case as per {@link Character#toLowerCase(char)}. No other letters are
     * changed.
     *
     * @param str the String to uncapitalize, may be <code>null</code>
     * @return the uncapitalized String, <code>null</code> if null
     */
    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (!hasLength(str)) {
			return str;
		}

		char baseChar = str.charAt(0);
		char updatedChar;
		if (capitalize) {
			updatedChar = Character.toUpperCase(baseChar);
		}
		else {
			updatedChar = Character.toLowerCase(baseChar);
		}
		if (baseChar == updatedChar) {
			return str;
		}

		char[] chars = str.toCharArray();
		chars[0] = updatedChar;
		return new String(chars);
	}

    /**
	 * Extract the filename from the given Java resource path,
	 * e.g. {@code "mypath/myfile.txt" &rarr; "myfile.txt"}.
	 * @param path the file path (may be {@code null})
	 * @return the extracted filename, or {@code null} if none
	 */
	@Nullable
	public static String getFilename(@Nullable String path) {
		if (path == null) {
			return null;
		}

		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR_CHAR);
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
	}

	/**
	 * Extract the filename extension from the given Java resource path,
	 * e.g. "mypath/myfile.txt" &rarr; "txt".
	 * @param path the file path (may be {@code null})
	 * @return the extracted filename extension, or {@code null} if none
	 */
	@Nullable
	public static String getFilenameExtension(@Nullable String path) {
		if (path == null) {
			return null;
		}

		int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		if (extIndex == -1) {
			return null;
		}

		int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR_CHAR);
		if (folderIndex > extIndex) {
			return null;
		}

		return path.substring(extIndex + 1);
	}

	/**
	 * Strip the filename extension from the given Java resource path,
	 * e.g. "mypath/myfile.txt" &rarr; "mypath/myfile".
	 * @param path the file path
	 * @return the path with stripped filename extension
	 */
	public static String stripFilenameExtension(String path) {
		int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		if (extIndex == -1) {
			return path;
		}

		int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR_CHAR);
		if (folderIndex > extIndex) {
			return path;
		}

		return path.substring(0, extIndex);
	}

    /**
     * Apply the given relative path to the given path, assuming standard Java
     * folder separation (i.e. "/" separators).
     *
     * @param path the path to start from (usually a full file path)
     * @param relativePath the relative path to apply (relative to the full file
     * path above)
     * @return the full file path that results from applying the relative path
     */
    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
                newPath += FOLDER_SEPARATOR;
            }
            return newPath + relativePath;
        } else {
            return relativePath;
        }
    }

    /**
	 * Normalize the path by suppressing sequences like "path/.." and
	 * inner simple dots.
	 * <p>The result is convenient for path comparison. For other uses,
	 * notice that Windows separators ("\") are replaced by simple slashes.
	 * <p><strong>NOTE</strong> that {@code cleanPath} should not be depended
	 * upon in a security context. Other mechanisms should be used to prevent
	 * path-traversal issues.
	 * @param path the original path
	 * @return the normalized path
	 */
	public static String cleanPath(String path) {
		if (!hasLength(path)) {
			return path;
		}

		String normalizedPath = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);
		String pathToUse = normalizedPath;

		// Shortcut if there is no work to do
		if (pathToUse.indexOf('.') == -1) {
			return pathToUse;
		}

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(':');
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			if (prefix.contains(FOLDER_SEPARATOR)) {
				prefix = "";
			}
			else {
				pathToUse = pathToUse.substring(prefixIndex + 1);
			}
		}
		if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
			prefix = prefix + FOLDER_SEPARATOR;
			pathToUse = pathToUse.substring(1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		// we never require more elements than pathArray and in the common case the same number
		Deque<String> pathElements = new ArrayDeque<>(pathArray.length);
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			String element = pathArray[i];
			if (CURRENT_PATH.equals(element)) {
				// Points to current directory - drop it.
			}
			else if (TOP_PATH.equals(element)) {
				// Registering top path found.
				tops++;
			}
			else {
				if (tops > 0) {
					// Merging path element with element corresponding to top path.
					tops--;
				}
				else {
					// Normal path element found.
					pathElements.addFirst(element);
				}
			}
		}

		// All path elements stayed the same - shortcut
		if (pathArray.length == pathElements.size()) {
			return normalizedPath;
		}
		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.addFirst(TOP_PATH);
		}
		// If nothing else left, at least explicitly point to current path.
		if (pathElements.size() == 1 && pathElements.getLast().isEmpty() && !prefix.endsWith(FOLDER_SEPARATOR)) {
			pathElements.addFirst(CURRENT_PATH);
		}

		final String joined = collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
		// avoid string concatenation with empty prefix
		return prefix.isEmpty() ? joined : prefix + joined;
	}

    /**
     * Compare two paths after normalization of them.
     *
     * @param path1 first path for comparison
     * @param path2 second path for comparison
     * @return whether the two paths are equivalent after normalization
     */
    public static boolean pathEquals(String path1, String path2) {
        return cleanPath(path1).equals(cleanPath(path2));
    }
    
    /**
	 * Decode the given encoded URI component value. Based on the following rules:
	 * <ul>
	 * <li>Alphanumeric characters {@code "a"} through {@code "z"}, {@code "A"} through {@code "Z"},
	 * and {@code "0"} through {@code "9"} stay the same.</li>
	 * <li>Special characters {@code "-"}, {@code "_"}, {@code "."}, and {@code "*"} stay the same.</li>
	 * <li>A sequence "{@code %<i>xy</i>}" is interpreted as a hexadecimal representation of the character.</li>
	 * </ul>
	 * @param source the encoded String
	 * @param charset the character set
	 * @return the decoded value
	 * @throws IllegalArgumentException when the given source contains invalid encoded sequences
	 * @since 5.0
	 * @see java.net.URLDecoder#decode(String, String)
	 */
	public static String uriDecode(String source, Charset charset) {
		int length = source.length();
		if (length == 0) {
			return source;
		}
		Assert.notNull(charset, "Charset must not be null");

		ByteArrayOutputStream baos = new ByteArrayOutputStream(length);
		boolean changed = false;
		for (int i = 0; i < length; i++) {
			int ch = source.charAt(i);
			if (ch == '%') {
				if (i + 2 < length) {
					char hex1 = source.charAt(i + 1);
					char hex2 = source.charAt(i + 2);
					int u = Character.digit(hex1, 16);
					int l = Character.digit(hex2, 16);
					if (u == -1 || l == -1) {
						throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
					}
					baos.write((char) ((u << 4) + l));
					i += 2;
					changed = true;
				}
				else {
					throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
				}
			}
			else {
				baos.write(ch);
			}
		}
		return (changed ? StreamUtils.copyToString(baos, charset) : source);
	}
	
	/**
	 * Parse the given {@code String} value into a {@link Locale}, accepting
	 * the {@link Locale#toString} format as well as BCP 47 language tags as
	 * specified by {@link Locale#forLanguageTag}.
	 * @param localeValue the locale value: following either {@code Locale's}
	 * {@code toString()} format ("en", "en_UK", etc), also accepting spaces as
	 * separators (as an alternative to underscores), or BCP 47 (e.g. "en-UK")
	 * @return a corresponding {@code Locale} instance, or {@code null} if none
	 * @throws IllegalArgumentException in case of an invalid locale specification
	 * @since 5.0.4
	 * @see #parseLocaleString
	 * @see Locale#forLanguageTag
	 */
	@Nullable
	public static Locale parseLocale(String localeValue) {
		String[] tokens = tokenizeLocaleSource(localeValue);
		if (tokens.length == 1) {
			validateLocalePart(localeValue);
			Locale resolved = Locale.forLanguageTag(localeValue);
			if (resolved.getLanguage().length() > 0) {
				return resolved;
			}
		}
		return parseLocaleTokens(localeValue, tokens);
	}

    
    /**
	 * Parse the given {@code String} representation into a {@link Locale}.
	 * <p>For many parsing scenarios, this is an inverse operation of
	 * {@link Locale#toString Locale's toString}, in a lenient sense.
	 * This method does not aim for strict {@code Locale} design compliance;
	 * it is rather specifically tailored for typical Spring parsing needs.
	 * <p><b>Note: This delegate does not accept the BCP 47 language tag format.
	 * Please use {@link #parseLocale} for lenient parsing of both formats.</b>
	 * @param localeString the locale {@code String}: following {@code Locale's}
	 * {@code toString()} format ("en", "en_UK", etc), also accepting spaces as
	 * separators (as an alternative to underscores)
	 * @return a corresponding {@code Locale} instance, or {@code null} if none
	 * @throws IllegalArgumentException in case of an invalid locale specification
	 */
	@Nullable
	public static Locale parseLocaleString(String localeString) {
		return parseLocaleTokens(localeString, tokenizeLocaleSource(localeString));
	}

	private static String[] tokenizeLocaleSource(String localeSource) {
		return tokenizeToStringArray(localeSource, "_ ", false, false);
	}

	@Nullable
	private static Locale parseLocaleTokens(String localeString, String[] tokens) {
		String language = (tokens.length > 0 ? tokens[0] : "");
		String country = (tokens.length > 1 ? tokens[1] : "");
		validateLocalePart(language);
		validateLocalePart(country);

		String variant = "";
		if (tokens.length > 2) {
			// There is definitely a variant, and it is everything after the country
			// code sans the separator between the country code and the variant.
			int endIndexOfCountryCode = localeString.indexOf(country, language.length()) + country.length();
			// Strip off any leading '_' and whitespace, what's left is the variant.
			variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
			if (variant.startsWith("_")) {
				variant = trimLeadingCharacter(variant, '_');
			}
		}

		if (variant.isEmpty() && country.startsWith("#")) {
			variant = country;
			country = "";
		}

		return (language.length() > 0 ? new Locale(language, country, variant) : null);
	}

    private static void validateLocalePart(String localePart) {
        for (int i = 0; i < localePart.length(); i++) {
            char ch = localePart.charAt(i);
            if (ch != '_' && ch != ' ' && !Character.isLetterOrDigit(ch)) {
                throw new IllegalArgumentException(
                        "Locale part \"" + localePart + "\" contains invalid characters");
            }
        }
    }

    /**
	 * Determine the RFC 3066 compliant language tag,
	 * as used for the HTTP "Accept-Language" header.
	 * @param locale the Locale to transform to a language tag
	 * @return the RFC 3066 compliant language tag as {@code String}
	 * @deprecated as of 5.0.4, in favor of {@link Locale#toLanguageTag()}
	 */
	@Deprecated
	public static String toLanguageTag(Locale locale) {
		return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
	}
	
	/**
	 * Parse the given {@code timeZoneString} value into a {@link TimeZone}.
	 * @param timeZoneString the time zone {@code String}, following {@link TimeZone#getTimeZone(String)}
	 * but throwing {@link IllegalArgumentException} in case of an invalid time zone specification
	 * @return a corresponding {@link TimeZone} instance
	 * @throws IllegalArgumentException in case of an invalid time zone specification
	 */
	public static TimeZone parseTimeZoneString(String timeZoneString) {
		TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
		if ("GMT".equals(timeZone.getID()) && !timeZoneString.startsWith("GMT")) {
			// We don't want that GMT fallback...
			throw new IllegalArgumentException("Invalid time zone specification '" + timeZoneString + "'");
		}
		return timeZone;
	}

    //---------------------------------------------------------------------
    // Convenience methods for working with String arrays
    //---------------------------------------------------------------------
    /**
     * Append the given String to the given String array, returning a new array
     * consisting of the input array contents plus the given String.
     *
     * @param array the array to append to (can be <code>null</code>)
     * @param str the String to append
     * @return the new array (never <code>null</code>)
     */
    public static String[] addStringToArray(String[] array, String str) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[]{str};
        }
        String[] newArr = new String[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = str;
        return newArr;
    }
    
    /**
	 * Copy the given {@link Collection} into a {@code String} array.
	 * <p>The {@code Collection} must contain {@code String} elements only.
	 * @param collection the {@code Collection} to copy
	 * (potentially {@code null} or empty)
	 * @return the resulting {@code String} array
	 */
	public static String[] toStringArray(@Nullable Collection<String> collection) {
		return (!CollectionUtils.isEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
	}

	/**
	 * Copy the given {@link Enumeration} into a {@code String} array.
	 * <p>The {@code Enumeration} must contain {@code String} elements only.
	 * @param enumeration the {@code Enumeration} to copy
	 * (potentially {@code null} or empty)
	 * @return the resulting {@code String} array
	 */
	public static String[] toStringArray(@Nullable Enumeration<String> enumeration) {
		return (enumeration != null ? toStringArray(Collections.list(enumeration)) : EMPTY_STRING_ARRAY);
	}

	/**
	 * Concatenate the given {@code String} arrays into one,
	 * with overlapping array elements included twice.
	 * <p>The order of elements in the original arrays is preserved.
	 * @param array1 the first array (can be {@code null})
	 * @param array2 the second array (can be {@code null})
	 * @return the new array ({@code null} if both given arrays were {@code null})
	 */
	@Nullable
	public static String[] concatenateStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}

		String[] newArr = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, newArr, 0, array1.length);
		System.arraycopy(array2, 0, newArr, array1.length, array2.length);
		return newArr;
	}

	/**
	 * Merge the given {@code String} arrays into one, with overlapping
	 * array elements only included once.
	 * <p>The order of elements in the original arrays is preserved
	 * (with the exception of overlapping elements, which are only
	 * included on their first occurrence).
	 * @param array1 the first array (can be {@code null})
	 * @param array2 the second array (can be {@code null})
	 * @return the new array ({@code null} if both given arrays were {@code null})
	 * @deprecated as of 4.3.15, in favor of manual merging via {@link LinkedHashSet}
	 * (with every entry included at most once, even entries within the first array)
	 */
	@Deprecated
	@Nullable
	public static String[] mergeStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}

		List<String> result = new ArrayList<>(Arrays.asList(array1));
		for (String str : array2) {
			if (!result.contains(str)) {
				result.add(str);
			}
		}
		return toStringArray(result);
	}

    /**
     * Turn given source String array into sorted array.
     *
     * @param array the source array
     * @return the sorted array (never <code>null</code>)
     */
    public static String[] sortStringArray(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[0];
        }
        Arrays.sort(array);
        return array;
    }


    /**
     * Trim the elements of the given String array, calling
     * <code>String.trim()</code> on each of them.
     *
     * @param array the original String array
     * @return the resulting array (of the same size) with trimmed elements
     */
    public static String[] trimArrayElements(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[0];
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            String element = array[i];
            result[i] = (element != null ? element.trim() : null);
        }
        return result;
    }

    /**
	 * Remove duplicate strings from the given array.
	 * <p>As of 4.2, it preserves the original order, as it uses a {@link LinkedHashSet}.
	 * @param array the {@code String} array (potentially empty)
	 * @return an array without duplicates, in natural sort order
	 */
	public static String[] removeDuplicateStrings(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return array;
		}

		Set<String> set = new LinkedHashSet<>(Arrays.asList(array));
		return toStringArray(set);
	}

	/**
	 * Split a {@code String} at the first occurrence of the delimiter.
	 * Does not include the delimiter in the result.
	 * @param toSplit the string to split (potentially {@code null} or empty)
	 * @param delimiter to split the string up with (potentially {@code null} or empty)
	 * @return a two element array with index 0 being before the delimiter, and
	 * index 1 being after the delimiter (neither element includes the delimiter);
	 * or {@code null} if the delimiter wasn't found in the given input {@code String}
	 */
	@Nullable
	public static String[] split(@Nullable String toSplit, @Nullable String delimiter) {
		if (!hasLength(toSplit) || !hasLength(delimiter)) {
			return null;
		}
		int offset = toSplit.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}

		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + delimiter.length());
		return new String[] {beforeDelimiter, afterDelimiter};
	}

    /**
     * Take an array Strings and split each element based on the given
     * delimiter. A <code>Properties</code> instance is then generated, with the
     * left of the delimiter providing the key, and the right of the delimiter
     * providing the value.
     * <p>
     * Will trim both the key and value before adding them to the
     * <code>Properties</code> instance.
     *
     * @param array the array to process
     * @param delimiter to split each element using (typically the equals
     * symbol)
     * @return a <code>Properties</code> instance representing the array
     * contents, or <code>null</code> if the array to process was null or empty
     */
    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
        return splitArrayElementsIntoProperties(array, delimiter, null);
    }

    /**
	 * Take an array of strings and split each element based on the given delimiter.
	 * A {@code Properties} instance is then generated, with the left of the
	 * delimiter providing the key, and the right of the delimiter providing the value.
	 * <p>Will trim both the key and value before adding them to the
	 * {@code Properties} instance.
	 * @param array the array to process
	 * @param delimiter to split each element using (typically the equals symbol)
	 * @param charsToDelete one or more characters to remove from each element
	 * prior to attempting the split operation (typically the quotation mark
	 * symbol), or {@code null} if no removal should occur
	 * @return a {@code Properties} instance representing the array contents,
	 * or {@code null} if the array to process was {@code null} or empty
	 */
	@Nullable
	public static Properties splitArrayElementsIntoProperties(
			String[] array, String delimiter, @Nullable String charsToDelete) {

		if (ObjectUtils.isEmpty(array)) {
			return null;
		}

		Properties result = new Properties();
		for (String element : array) {
			if (charsToDelete != null) {
				element = deleteAny(element, charsToDelete);
			}
			String[] splittedElement = split(element, delimiter);
			if (splittedElement == null) {
				continue;
			}
			result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
		}
		return result;
	}

	/**
	 * Tokenize the given {@code String} into a {@code String} array via a
	 * {@link StringTokenizer}.
	 * <p>Trims tokens and omits empty tokens.
	 * <p>The given {@code delimiters} string can consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using {@link #delimitedListToStringArray}.
	 * @param str the {@code String} to tokenize (potentially {@code null} or empty)
	 * @param delimiters the delimiter characters, assembled as a {@code String}
	 * (each of the characters is individually considered as a delimiter)
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(@Nullable String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * Tokenize the given {@code String} into a {@code String} array via a
	 * {@link StringTokenizer}.
	 * <p>The given {@code delimiters} string can consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using {@link #delimitedListToStringArray}.
	 * @param str the {@code String} to tokenize (potentially {@code null} or empty)
	 * @param delimiters the delimiter characters, assembled as a {@code String}
	 * (each of the characters is individually considered as a delimiter)
	 * @param trimTokens trim the tokens via {@link String#trim()}
	 * @param ignoreEmptyTokens omit empty tokens from the result array
	 * (only applies to tokens that are empty after trimming; StringTokenizer
	 * will not consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(
			@Nullable String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

		if (str == null) {
			return EMPTY_STRING_ARRAY;
		}

		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	/**
	 * Take a {@code String} that is a delimited list and convert it into a
	 * {@code String} array.
	 * <p>A single {@code delimiter} may consist of more than one character,
	 * but it will still be considered as a single delimiter string, rather
	 * than as bunch of potential delimiter characters, in contrast to
	 * {@link #tokenizeToStringArray}.
	 * @param str the input {@code String} (potentially {@code null} or empty)
	 * @param delimiter the delimiter between elements (this is a single delimiter,
	 * rather than a bunch individual delimiter characters)
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(@Nullable String str, @Nullable String delimiter) {
		return delimitedListToStringArray(str, delimiter, null);
	}

	/**
	 * Take a {@code String} that is a delimited list and convert it into
	 * a {@code String} array.
	 * <p>A single {@code delimiter} may consist of more than one character,
	 * but it will still be considered as a single delimiter string, rather
	 * than as bunch of potential delimiter characters, in contrast to
	 * {@link #tokenizeToStringArray}.
	 * @param str the input {@code String} (potentially {@code null} or empty)
	 * @param delimiter the delimiter between elements (this is a single delimiter,
	 * rather than a bunch individual delimiter characters)
	 * @param charsToDelete a set of characters to delete; useful for deleting unwanted
	 * line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a {@code String}
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(
			@Nullable String str, @Nullable String delimiter, @Nullable String charsToDelete) {

		if (str == null) {
			return EMPTY_STRING_ARRAY;
		}
		if (delimiter == null) {
			return new String[] {str};
		}

		List<String> result = new ArrayList<>();
		if (delimiter.isEmpty()) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		}
		else {
			int pos = 0;
			int delPos;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		return toStringArray(result);
	}

	/**
	 * Convert a comma delimited list (e.g., a row from a CSV file) into an
	 * array of strings.
	 * @param str the input {@code String} (potentially {@code null} or empty)
	 * @return an array of strings, or the empty array in case of empty input
	 */
	public static String[] commaDelimitedListToStringArray(@Nullable String str) {
		return delimitedListToStringArray(str, ",");
	}

	/**
	 * Convert a comma delimited list (e.g., a row from a CSV file) into a set.
	 * <p>Note that this will suppress duplicates, and as of 4.2, the elements in
	 * the returned set will preserve the original order in a {@link LinkedHashSet}.
	 * @param str the input {@code String} (potentially {@code null} or empty)
	 * @return a set of {@code String} entries in the list
	 * @see #removeDuplicateStrings(String[])
	 */
	public static Set<String> commaDelimitedListToSet(@Nullable String str) {
		String[] tokens = commaDelimitedListToStringArray(str);
		return new LinkedHashSet<>(Arrays.asList(tokens));
	}

	/**
	 * Convert a {@link Collection} to a delimited {@code String} (e.g. CSV).
	 * <p>Useful for {@code toString()} implementations.
	 * @param coll the {@code Collection} to convert (potentially {@code null} or empty)
	 * @param delim the delimiter to use (typically a ",")
	 * @param prefix the {@code String} to start each element with
	 * @param suffix the {@code String} to end each element with
	 * @return the delimited {@code String}
	 */
	public static String collectionToDelimitedString(
			@Nullable Collection<?> coll, String delim, String prefix, String suffix) {

		if (CollectionUtils.isEmpty(coll)) {
			return "";
		}

		int totalLength = coll.size() * (prefix.length() + suffix.length()) + (coll.size() - 1) * delim.length();
		for (Object element : coll) {
			totalLength += String.valueOf(element).length();
		}

		StringBuilder sb = new StringBuilder(totalLength);
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	/**
	 * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
	 * <p>Useful for {@code toString()} implementations.
	 * @param coll the {@code Collection} to convert (potentially {@code null} or empty)
	 * @param delim the delimiter to use (typically a ",")
	 * @return the delimited {@code String}
	 */
	public static String collectionToDelimitedString(@Nullable Collection<?> coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	/**
	 * Convert a {@code Collection} into a delimited {@code String} (e.g., CSV).
	 * <p>Useful for {@code toString()} implementations.
	 * @param coll the {@code Collection} to convert (potentially {@code null} or empty)
	 * @return the delimited {@code String}
	 */
	public static String collectionToCommaDelimitedString(@Nullable Collection<?> coll) {
		return collectionToDelimitedString(coll, ",");
	}

	/**
	 * Convert a {@code String} array into a delimited {@code String} (e.g. CSV).
	 * <p>Useful for {@code toString()} implementations.
	 * @param arr the array to display (potentially {@code null} or empty)
	 * @param delim the delimiter to use (typically a ",")
	 * @return the delimited {@code String}
	 */
	public static String arrayToDelimitedString(@Nullable Object[] arr, String delim) {
		if (ObjectUtils.isEmpty(arr)) {
			return "";
		}
		if (arr.length == 1) {
			return ObjectUtils.nullSafeToString(arr[0]);
		}

		StringJoiner sj = new StringJoiner(delim);
		for (Object elem : arr) {
			sj.add(String.valueOf(elem));
		}
		return sj.toString();
	}

	/**
	 * Convert a {@code String} array into a comma delimited {@code String}
	 * (i.e., CSV).
	 * <p>Useful for {@code toString()} implementations.
	 * @param arr the array to display (potentially {@code null} or empty)
	 * @return the delimited {@code String}
	 */
	public static String arrayToCommaDelimitedString(@Nullable Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

    @SuppressWarnings("unchecked")
    public static <T> T fromString(String str, Class<?> T) {
        T obj = null;
        try {
            if (StringUtils.isNotBlank(str)) {
                Constructor<?> c = T.getConstructor(new Class[]{String.class});
                obj = (T) c.newInstance(str.trim());
            }
        } catch (Exception ex) {
        }
        return obj;
    }

    public static <T> List<T> fromDelimitedString(String str, Class<?> T) {

        List<T> resList = new ArrayList<>();
        try {
            if (StringUtils.isNotBlank(str)) {

                Constructor<?> c = T.getConstructor(new Class[]{String.class});
                if (c == null) {
                }
                T obj = null;

                if (str.indexOf(',') == -1) {
                    obj = fromString(str.trim(), T);
                    resList.add(obj);
                } else {
                    String[] strArray = str.split(",");
                    for (String strArray1 : strArray) {
                        obj = fromString(strArray1.trim(), T);
                        resList.add(obj);
                    }
                }
            }
        } catch (Exception ex) {
        	log.error("StringUtil ---> fromDelimitedString error");
        }
        return resList;
    }


	public static String replaceParametersInString(final String inputString, final Object... messageParameters) {
		StringBuilder pOutputBuild = new StringBuilder(10);
		pOutputBuild.append(inputString);

		for (int i = 0; i < messageParameters.length; i++) {
			String intermediate = pOutputBuild.toString();
			intermediate = intermediate.replace(getAsConcatenatedString("{", String.valueOf(i), "}"),
					messageParameters[i] != null ? messageParameters[i].toString() : "");
			pOutputBuild.delete(0, pOutputBuild.length());
			pOutputBuild.append(intermediate);
		}
		return pOutputBuild.toString();
	}

	public static String getAsConcatenatedString(final Object... args) {
		StringBuilder outBuilder = new StringBuilder(getTotalLength(args));

		for (Object arg : args) {
			if (arg != null) {
				outBuilder.append(String.valueOf(arg));
			} else {
				outBuilder.append("null");
			}
		}
		return outBuilder.toString();
	}

	public static int getTotalLength(final Object... args) {
		int iTotal = 0;
		for (Object arg : args) {
			if (arg != null) {
				iTotal += String.valueOf(arg).length();
			} else {
				iTotal += "null".length();
			}
		}
		return iTotal;
	}
	
	/**
     * @param path
     *            Path to which the input will be added
     * @param input
     *            the string that should be added to the path
     * @return Returns a final path with concatenated the path and the input
     */
    public static String addToPath(String path, String input) {
        String retVal = "";
        if (path.endsWith(File.separator)) {
            retVal = path + input;
        } else {
            retVal = path + File.separator + input;
        }
        return retVal;

    }
}
