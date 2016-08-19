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

import com.github.jabbalaci.graphviz.GraphViz;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.base.Strings;
import com.sun.javadoc.RootDoc;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

    /**
     * Start boolean.
     *
     * @param root the root
     * @return the boolean
     */
    public static boolean start(final RootDoc root) {
        new LivingDocumentation().createDocumentations(root);
        return true;
    }

    private void createDocumentations(RootDoc root) {
        final Collection<DomainModel> domains = collectDomainModels(root);
        writeGlossary(domains);
        drawContextMap(domains, collectLinks(domains));
    }

    @NotNull
    private Set<DomainModel> collectDomainModels(final RootDoc root) {
        return Arrays.stream(root.specifiedPackages())
            .map(DomainModel::makeBy)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
    }

    @NotNull
    private Set<BiLink> collectLinks(final Collection<DomainModel> domains) {
        final Map<BiLink, BiLink> links = new HashMap<>();
        for (DomainModel domain : domains) {
            for (DomainModel.LinkModel link : domain.getLinks()) {
                final BiLink newLink = new BiLink(domain.getId(), link.getRelated(), link.getLabel());
                final BiLink oldLink = links.get(newLink);
                if (oldLink != null) {
                    final BiLink value = oldLink.mergeWith(newLink);
                    links.remove(oldLink);
                    links.put(value, value);
                } else {
                    links.put(newLink, newLink);
                }
            }
        }
        return links.keySet();
    }

    /**
     * Write glossary.
     *
     */
    private void writeGlossary(final Collection<DomainModel> domains) {
        for (DomainModel domain : domains) {
            try {
                final Handlebars handlebars = new Handlebars();
                final Path file = Paths.get(domain.getName() + "-glossary.md");
                final Template template = handlebars.compile("glossary");
                byte[] bytes = template.apply(domain).getBytes(Charset.forName("UTF-8"));
                Files.write(file, bytes);
            } catch (IOException e) {
                log.debug(e.getMessage(), e);
            }
        }
    }

    private void drawContextMap(final Collection<DomainModel> domains, final Collection<BiLink> links) {

        final GraphViz gv = new GraphViz();
        gv.setImageDpi(GraphViz.DpiSizes.DPI_249);


        gv.startGraph("ContextMap");

        for (DomainModel domain : domains) {
            gv.node(domain.getId(), domain.getTitle());
        }

        for (BiLink link : links) {
            log.debug(link.toString());
            final List<String> attr = new ArrayList<>();

            if (!Strings.isNullOrEmpty(link.getLabel())) {
                attr.add(format("[label=\"%s\"]", link.getLabel()));
            }

            if (link.isBiDirection()) {
                attr.add("[dir=\"both\"]");
            }

            gv.addln(format("%s -> %s %s;",
                link.getLeftId(),
                link.getRightId(),
                attr.stream().collect(Collectors.joining()))
            );
        }

        gv.endGraph();
        log.debug(gv.source());

        gv.writeTo("contextMap");
    }


}
