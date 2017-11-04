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

public abstract class InputGroup<T> {

    protected Map<String, Input<?, ?>> controlMap;
    protected T device;

    private Pattern keyVer;

    private static final int MAP_CAPACITY = 50;

    public InputGroup(T device) {
        this.device = device;
        this.controlMap = new HashMap<>(MAP_CAPACITY);
        this.keyVer = Pattern.compile("->|\\s");
        this.initMap();
    }

    public Map<String, Input<?, ?>> getMap() {
        return this.controlMap;
    }

    public T getDevice() {
        return this.device;
    }

    public void add(String key, Input<?, ?> value) {
        if(value == null)
            throw new IllegalArgumentException("`value` argument must not be null.");
        else if(this.controlMap.containsKey(key))
            throw new IllegalArgumentException("Bad key: Duplicate key already exists in control map.");
        else if(keyVer.matcher(key).find())
            throw new IllegalArgumentException("Bad key: Key cannot contain '->' or whitespace characters.");
        else
            this.controlMap.put(key, value);
    }

    public abstract void initMap();

}
