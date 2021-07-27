package com.automation.platform.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;


public class TapException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(TapException.class);

    public TapException(TapExceptionType tapExceptionType, String message, Object... args) {
        super(MessageFormatter.arrayFormat(message, args).getMessage());
        logger.info("handling exception " + tapExceptionType);
    }
}
