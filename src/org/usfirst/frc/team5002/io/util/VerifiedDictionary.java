/*
 * Copyright (c) 2017 Brandon Gong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.usfirst.frc.team5002.io.util;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * VerifiedDictionary.java : Defines a dictionary datatype with verification
 * checks.
 *
 * VerifiedDictionary uses HashMap internally to store key and value pairs.
 * Using VerifiedDictionary has two main benefits:
 *     - It is less verbose, as the key type is always String.  So, only one
 *       generic argument is required.
 *     - It performs null checks on the values and checks on the keys to ensure
 *       that the key name is valid and does not contain any illegal characters.
 *
 * This class will be used especially by the parser to connect string keys to
 * their corresponding values, but it can also be used as a convenience for
 * other code.
 *
 * @author Brandon Gong
 * @param <T> The value type.
 */
public class VerifiedDictionary<T> {

    /**
     * define the default capacity of this dictionary.  We keep this low because
     * the lower it is, the better performance we squeeze out of the hash map.
     */
    public static final int DEFAULT_CAPACITY = 50;

    // internal instance of HashMap.
    private HashMap<String, T> instance;

    /**
     * Keys cannot contain ->, |, :, ;, #, commas, periods, parentheses, or
     * whitespace.
     */
    private final Pattern keyVer =
        Pattern.compile("->|\\||:|;|#|,|\\.|\\(|\\)|\\s");

    /**
     * Create a new VerifiedDictionary with the default capacity.
     */
    public VerifiedDictionary() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Create a new VerifiedDictionary with a custom capacity.
     * @param capacity The initial capacity of the dictionary.
     */
    public VerifiedDictionary(int capacity) {
        instance = new HashMap<>(capacity);
    }

    /**
     * Add a new entry to the VerifiedDictionary.
     * @param key the entry key.
     * @param value the entry value.
     */
    public void add(String key, T value) {

        // no null values, duplicate keys, or illegal characters allowed.
        if(value == null) {
            throw new IllegalArgumentException(
                "`value` argument must not be null."
            );
        } else if(this.instance.containsKey(key)) {
            throw new IllegalArgumentException(
                "Bad key: Duplicate key already exists in control map."
            );
        } else if(keyVer.matcher(key).find()) {
            throw new IllegalArgumentException(
                "Bad key: Key contains illegal characters."
            );
        } else {
            // assuming it's passed all of those checks, now call
            // <code>put()</code> to add it to the map.
            this.instance.put(key, value);
        }

    }
}
