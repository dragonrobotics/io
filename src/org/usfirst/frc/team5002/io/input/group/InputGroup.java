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
