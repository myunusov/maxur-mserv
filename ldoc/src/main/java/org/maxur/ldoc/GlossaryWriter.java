package org.maxur.ldoc;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

@Slf4j
class GlossaryWriter {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    private static final String GLOSSARY_NAME_TEMPLATE = "%s-glossary.md";

    private final Template template;

    private GlossaryWriter(final Template template) {
        this.template = template;

    }

    static GlossaryWriter make(final String basedirPath) {
        try {
            return new GlossaryWriter(handlebars(basedir(basedirPath)).compile("glossary"));
        } catch (IOException e) {
            throw new IllegalStateException(
                format("Glossary Template is not found or is not accessible: %s", e.getMessage()),
                e
            );
        }
    }

    private static File basedir(final String pathname) {
        final File dir = new File(pathname);
        if (dir.exists() && dir.isDirectory() && dir.canRead()) {
            return dir;
        } else {
            throw new IllegalStateException(
                format("Directory '%s' is not found or is not accessible", dir.getAbsolutePath())
            );
        }
    }

    @NotNull
    private static Handlebars handlebars(final File basedir) {
        return basedir == null ?
            new Handlebars() :
            new Handlebars(new FileTemplateLoader(basedir.getAbsolutePath()));
    }

    /**
     * Write glossary.
     */
    void writeBy(final DomainModels domains) {
        for (DomainModel domain : domains.getDomains()) {
            try {
                final Path file = Paths.get(String.format(GLOSSARY_NAME_TEMPLATE, domain.getName()));
                byte[] bytes = template.apply(domain).getBytes(CHARSET);
                Files.write(file, bytes);
            } catch (IOException e) {
                log.debug(e.getMessage(), e);
            }
        }
    }

}