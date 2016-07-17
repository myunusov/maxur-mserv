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
     * @param propertiesClass the class of properties object
     * @return the t
     */
    @Override
    protected <T> T load(final Class<T> propertiesClass) {
        final Yaml yaml = new Yaml();
        final T config;
        try (InputStream in = Files.newInputStream(Paths.get(path))) {
            config = yaml.loadAs(in, propertiesClass);
            final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
            final Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<T>> constraintViolation = validator.validate(config);
            if (!constraintViolation.isEmpty()) {
                throw new PropertyLoadException(
                        "User configuration error:\n %s",
                    allValidationMessagesFrom(constraintViolation)
                );
            }
        } catch (IOException e) {
            throw new PropertyLoadException(e, "Config file '%s' is not accessible", path);
        }
        return config;
    }

    private <T> String allValidationMessagesFrom(Set<ConstraintViolation<T>> constraintViolation) {
        return constraintViolation.stream()
            .map(this::validationMessageFrom)
            .collect(joining(";\n"));
    }

    private <T> String validationMessageFrom(ConstraintViolation<T> cv) {
        return format(
            "Field '%s.%s' %s",
            cv.getLeafBean().getClass().getSimpleName(),
            cv.getPropertyPath(),
            cv.getMessage()
        );
    }


}
