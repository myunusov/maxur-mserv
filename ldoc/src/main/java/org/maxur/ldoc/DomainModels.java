package org.maxur.ldoc;

import com.sun.javadoc.RootDoc;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>8/29/2016</pre>
 */
class DomainModels {

    @Getter
    @NotNull
    private final Collection<DomainModel> domains;

    @Getter
    @NotNull
    private final Set<BiLink> links;

    DomainModels(final RootDoc root) {
        domains = collectDomainModels(root);
        links = collectLinks(domains);
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
        final Map<BiLink, BiLink> result = new HashMap<>();
        for (DomainModel domain : domains) {
            for (DomainModel.LinkModel link : domain.getLinks()) {
                final BiLink newLink = new BiLink(domain.getId(), link.getRelated(), link.getLabel());
                final BiLink oldLink = result.get(newLink);
                if (oldLink != null) {
                    final BiLink value = oldLink.mergeWith(newLink);
                    result.remove(oldLink);
                    result.put(value, value);
                } else {
                    result.put(newLink, newLink);
                }
            }
        }
        return result.keySet();
    }


}
