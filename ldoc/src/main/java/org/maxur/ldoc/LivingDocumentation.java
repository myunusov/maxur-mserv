/*
 * Copyright 2016 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */

package org.maxur.ldoc;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * The type Living Documentation doclet.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>07.08.2016</pre>
 */
@Slf4j
public class LivingDocumentation {

    private static final String BASEDIR = "-basedir";

    private static LivingDocumentation instance;

    private final Map<String, String> optionsMap;

    private LivingDocumentation(final Map<String, String> optionsMap) {
        this.optionsMap = optionsMap;
    }

    /**
     * Start boolean.
     *
     * @param root the root
     * @return the boolean
     */
    public static boolean start(final RootDoc root) {
        instance.createDocumentations(root);
        return true;
    }

    /**
     * Used by javadoc to identify number of args for a given option
     *
     * @param option the option as a string
     * @return the number of expected args for the option.
     */
    public static int optionLength(final String option) {
        if (BASEDIR.equalsIgnoreCase(option)) {
            return 2;
        } else {
            throw new IllegalStateException(format("Illegal option (%s) of LivingDocumentation doclet", option));
        }

    }

    /**
     * Valid options boolean.
     *
     * @param options  the options
     * @param reporter the reporter
     * @return the boolean
     */
    public static boolean validOptions(final String[][] options, final DocErrorReporter reporter){
        try {
            final Map<String, String> optionsMap = Arrays.stream(options)
                .filter(option -> option.length > 1)
                .collect(
                    Collectors.toMap(
                        option -> option[0].toLowerCase(), option -> option[1], (s1, s2) -> s2
                    )
                );

            optionsMap.forEach((key, value) -> log.debug(key + " = " + value));

            instance = new LivingDocumentation(optionsMap);

        } catch (IllegalStateException e) {
            log.debug(e.getMessage(), e);
            reporter.printError(e.getMessage());
            return false;
        }
        return true;
    }



    private void createDocumentations(final RootDoc root) {
        final DomainModels models = new DomainModels(root);
        GlossaryWriter.make(optionsMap.get(BASEDIR)).writeBy(models);
        ContextMapDrawer.make().drawBy(models);
    }




}
