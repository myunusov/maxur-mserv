package org.maxur.ldoc;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The type Bi link.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>8/16/2016</pre>
 */
public class BiLink {

    @Getter
    private final String leftId;

    @Getter
    private final String rightId;

    @Getter
    private final boolean biDirection;

    @Getter
    private final String label;


    private BiLink(final String leftId, final String rightId, final String label, final boolean biDirection) {
        this.leftId = leftId;
        this.rightId = rightId;
        this.label = label;
        this.biDirection = biDirection;
    }

    /**
     * Instantiates a new Bi link.
     *
     * @param leftId  the left id
     * @param rightId the right id
     * @param label   the label
     */
    public BiLink(final String leftId, final String rightId, final String label) {
        this(leftId, rightId, label, false);
    }

    /**
     * Merge with bi link.
     *
     * @param bilink the bilink
     * @return the bi link
     */
    public BiLink mergeWith(final BiLink bilink) {
        if (!this.equals(bilink)) {
            throw new IllegalStateException("Merge is not acceptable. Links have different nodes");
        }

        return new BiLink(
            leftId,
            rightId,
            mergeLabel(bilink.label),
            this.leftId.equals(bilink.rightId)
        );
    }

    private String mergeLabel(final String label) {
        if (label.equals(this.label)) {
            return label;
        } else {
            return Arrays.stream(new String[]{this.label, label}).collect(Collectors.joining("/"));
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BiLink)) {
            return false;
        }
        BiLink biLink = (BiLink) o;
        return
            (Objects.equals(leftId, biLink.leftId) && Objects.equals(rightId, biLink.rightId)) ||
                (Objects.equals(leftId, biLink.rightId) && Objects.equals(rightId, biLink.leftId));
    }

    @Override
    public int hashCode() {
        if (leftId.compareTo(rightId) > 0) {
            return Objects.hash(leftId, rightId);
        } else {
            return Objects.hash(rightId, leftId);
        }
    }

    @Override
    public String toString() {
        return "BiLink{" +
            "leftId='" + leftId + '\'' +
            ", rightId='" + rightId + '\'' +
            ", biDirection=" + biDirection +
            ", label='" + label + '\'' +
            '}';
    }
}
