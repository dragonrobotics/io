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

import org.usfirst.frc.team5002.io.input.base.Input;
import org.usfirst.frc.team5002.io.util.VerifiedDictionary;

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
    protected VerifiedDictionary<Input<?, ?>> dict;

    /**
     * Holds the instance of the device class the inputs call.
     */
    protected T device;

    /**
     * Construct with the given device instance.
     * This automatically initializes and populates the dictionary.
     *
     * @param device the internal instance of the input device.
     */
    public InputGroup(T device) {
        this.device = device;
        this.dict = new VerifiedDictionary<>();
        this.initDict();
    }

    /**
     * get the dictionary of all of the inputs.
     */
    public VerifiedDictionary<Input<?, ?>> getDict() {
        return this.dict;
    }

    /**
     * Get the internal instance of the device.
     */
    public T getDevice() {
        return this.device;
    }

    /**
     * Add a key-value pair to the dictionary.
     */
    public void add(String key, Input<?, ?> value) {
        this.dict.add(key, value);
    }

    /**
     * Use this method to populate the dictionary values.
     * This is called automatically when the object is constructed.
     */
    public abstract void initDict();
}
