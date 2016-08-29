package org.maxur.ldoc;

import com.github.jabbalaci.graphviz.GraphViz;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
class ContextMapDrawer {

    private ContextMapDrawer() {
    }

    static ContextMapDrawer make() {
        return new ContextMapDrawer();
    }

    void drawBy(final DomainModels domains) {

        final GraphViz graphViz = new GraphViz();
        graphViz.setImageDpi(GraphViz.DpiSizes.DPI_249);


        graphViz.startGraph("ContextMap");

        for (DomainModel domain : domains.getDomains()) {
            graphViz.node(domain.getId(), domain.getTitle());
        }

        for (BiLink link : domains.getLinks()) {
            log.debug(link.toString());
            final List<String> attr = new ArrayList<String>();

            if (!Strings.isNullOrEmpty(link.getLabel())) {
                attr.add(String.format("[label=\"%s\"]", link.getLabel()));
            }

            if (link.isBiDirection()) {
                attr.add("[dir=\"both\"]");
            }

            graphViz.addln(String.format("%s -> %s %s;",
                link.getLeftId(),
                link.getRightId(),
                attr.stream().collect(Collectors.joining()))
            );
        }

        graphViz.endGraph();
        log.debug(graphViz.source());

        graphViz.writeTo("contextMap");
    }
}