package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.factory.ConverterFactory;
import org.example.services.ConverterService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("***** Start excel2json conversion *****");

        validate(args);
        String converterType = args[0];

        Instant start = Instant.now();

        Arrays.stream(args)
                .skip(1)
                .forEach(f -> {
                    ConverterService converter = ConverterFactory.createFromType(converterType);
                    converter.convert(f);
                });

        logger.info("***** End excel2json conversion with total duration {}s *****", ChronoUnit.SECONDS.between(start, Instant.now()));
    }

    private static void validate(String[] args) {
        if(args.length < 2) {
            throw new IllegalArgumentException("Missing argument");
        }
    }

}