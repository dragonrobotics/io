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

package org.usfirst.frc.team5002.io.config.exception;

/**
 * ConfigParseException.java : Defines a custom exception type for parse
 * exceptions.
 *
 * There's nothing really special in this class, it just makes calls to the
 * Exception superclass.
 *
 * @author Brandon Gong
 * @version 1.0.0
 * Date: 11.3.17
 */
public class ConfigParseException extends Exception {
    public ConfigParseException() {
        super();
    }
    public ConfigParseException(String message) {
        super(message);
    }
    public ConfigParseException(String message, Throwable cause) {
        super(message, cause);
    }
    public ConfigParseException(Throwable cause) {
        super(cause);
    }
}
