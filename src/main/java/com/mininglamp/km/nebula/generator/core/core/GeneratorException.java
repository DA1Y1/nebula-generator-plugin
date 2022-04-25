package com.mininglamp.km.nebula.generator.core.core;

public class GeneratorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GeneratorException(String message) {
        super(message);
    }

    public GeneratorException(Throwable throwable) {
        super(throwable);
    }

    public GeneratorException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
