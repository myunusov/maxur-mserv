package com.jasonclawson.jackson.dataformat.hocon;

import com.fasterxml.jackson.core.JsonLocation;
import com.typesafe.config.ConfigOrigin;

import java.net.URL;
import java.util.List;

/**
 * The type Hocon json location.
 */
public class HoconJsonLocation extends JsonLocation implements ConfigOrigin {
    private static final long serialVersionUID = 1L;
    
    private final ConfigOrigin origin;

    /**
     * Instantiates a new Hocon json location.
     *
     * @param origin the origin
     */
    public HoconJsonLocation(final ConfigOrigin origin) {
        super(origin.description(), -1L, origin.lineNumber(), -1);
        this.origin = origin;
    }

    @Override
    public String description() {
        return origin.description();
    }

    @Override
    public String filename() {
        return origin.filename();
    }

    @Override
    public URL url() {
        return origin.url();
    }

    @Override
    public String resource() {
        return origin.resource();
    }

    @Override
    public int lineNumber() {
        return origin.lineNumber();
    }

    @Override
    public List<String> comments() {
        return origin.comments();
    }

    @Override
    public ConfigOrigin withComments(List<String> comments) {
        // TODO
        return this;
    }

    @Override
    public ConfigOrigin withLineNumber(int lineNumber) {
        // TODO 
        return this;
    }
}
