package org.maxur.mserv.properties;

import org.yaml.snakeyaml.Yaml;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

/**
 * The type Yaml properties file.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>15.07.2016</pre>
 */
class YamlPropertiesFile extends PropertiesFile {

    private final String path;

    /**
     * Instantiates a new Yaml properties file.
     *
     * @param path the path
     */
    YamlPropertiesFile(final String path) {
        this.path = path;
    }

    /**
     * Load t.
     *
     * @param <T> the type parameter
     * @return the t
     */
    @Override
    public <T> T load() {
        final Yaml yaml = new Yaml();
        final T config;
        try (InputStream in = Files.newInputStream(Paths.get(path))) {
            config = (T) yaml.loadAs(in, propertiesClass());
            final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
            final Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<T>> constraintViolation = validator.validate(config);
            if (!constraintViolation.isEmpty()) {
                throw new IllegalArgumentException(
                    format(
                        "User configuration error:\n %s",
                        constraintViolation.stream()
                            .map(validationMessageFrom())
                            .collect(joining(";\n"))
                    )
                );
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Config file '" + path +"' is not accessible", e);
        }
        return config;
    }

    private <T> Function<ConstraintViolation<T>, String> validationMessageFrom() {
        return cv -> format(
            "Field '%s.%s' %s",
            cv.getLeafBean().getClass().getSimpleName(),
            cv.getPropertyPath(),
            cv.getMessage()
        );
    }


}
