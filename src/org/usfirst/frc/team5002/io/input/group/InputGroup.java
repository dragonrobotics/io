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

package org.usfirst.frc.team5002.io.input.group;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.usfirst.frc.team5002.io.input.base.Input;

/**
 * InputGroup.java : defines the base InputGroup type.
 *
 * An InputGroup is any groupable set of inputs, i.e. inputs that come from
 * the same input device.  For example, a gamepad might be an input group,
 * and the NAVX might be an input group.
 *
 * Each input is associated with a name, which is used during parsing to
 * find and link the corresponding input.
 *
 * @author Brandon Gong
 * @version 1.0.0
 * Date: 11.3.17
 */
public abstract class InputGroup<T> {

    /**
     * Holds all of the key names and their corresponding Inputs.
     */
    protected Map<String, Input<?, ?>> controlMap;

    /**
     * Holds the instance of the device class the inputs call.
     */
    protected T device;

    /**
     * Keys cannot contain ->, |, :, ;, #, commas, periods, parentheses, or
     * whitespace.
     */
    private final Pattern keyVer =
        Pattern.compile("->|\\||:|;|#|,|\\.|\\(|\\)|\\s");

    /**
     * The maximum capacity of this input group.  Set for performance reasons.
     */
    private static final int MAP_CAPACITY = 50;

    /**
     * Construct with the given device instance.
     * This automatically initializes and populates the map.
     *
     * @param device the internal instance of the input device.
     */
    public InputGroup(T device) {
        this.device = device;
        this.controlMap = new HashMap<>(MAP_CAPACITY);
        this.initMap();
    }

    /**
     * get the map of all of the inputs.
     */
    public Map<String, Input<?, ?>> getMap() {
        return this.controlMap;
    }

    /**
     * Get the internal instance of the device.
     */
    public T getDevice() {
        return this.device;
    }

    /**
     * Add a key-value pair to the hash map.
     * We choose not to use the built-in <code>put()</code> method because
     * we want to do our own validity checks on the keys.
     */
    public void add(String key, Input<?, ?> value) {

        // no null values, duplicate keys, or illegal characters allowed.
        if(value == null) {
            throw new IllegalArgumentException(
                "`value` argument must not be null."
            );
        } else if(this.controlMap.containsKey(key)) {
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
            this.controlMap.put(key, value);
        }
    }

    /**
     * Use this method to populate the map with key and value pairs.
     * This is called automatically when the object is constructed.
     */
    public abstract void initMap();
}
