package com.hxr.javatone.util;

public class NestedException extends RuntimeException {

    private static final long serialVersionUID = 5893258079497055346L;

    private final Throwable throwable;

    private NestedException(final Throwable t) {
        this.throwable = t;
    }

    /** Wraps another exeception in a RuntimeException. */
    public static RuntimeException wrap(final Throwable t) {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;
        return new NestedException(t);
    }

    @Override
    public Throwable getCause() {
        return this.throwable;
    }

    @Override
    public void printStackTrace() {
        this.throwable.printStackTrace();
    }

}
