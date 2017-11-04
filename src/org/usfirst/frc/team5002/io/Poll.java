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

package org.usfirst.frc.team5002.io;

import java.util.ArrayList;
import java.util.HashMap;

import org.usfirst.frc.team5002.io.input.base.Input;

public class Poll {

    private static ArrayList<Input<?, ?>> inputs;
    private static ArrayList<Object> values;

    public static void poll() {
        for(int i = 0; i < inputs.size(); i++) {

            // TODO: I doubt this .equals() even works
            if(inputs.get(i).getRawValue().equals(values.get(i))) continue;


        }
    }

    public static void add(HashMap<String, Input<?, ?>> hashmap) {
        inputs.addAll(hashmap.values());
        values = new ArrayList<Object>(inputs.size());
    }

}
