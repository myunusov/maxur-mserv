package com.github.jabbalaci.graphviz;

// GraphViz.java - a simple API to call dot from Java programs

/*$Id$*/
/*
 ******************************************************************************
 *                                                                            *
 *                    (c) Copyright Laszlo Szathmary                          *
 *                                                                            *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms of the GNU Lesser General Public License as published by   *
 * the Free Software Foundation; either version 2.1 of the License, or        *
 * (at your option) any later version.                                        *
 *                                                                            *
 * This program is distributed in the hope that it will be useful, but        *
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY *
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public    *
 * License for more details.                                                  *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public License   *
 * along with this program; if not, write to the Free Software Foundation,    *
 * Inc., 675 Mass Ave, Cambridge, MA 02139, USA.                              *
 *                                                                            *
 ******************************************************************************
 */

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static java.lang.String.format;

/**
 * <dl>
 * <dt>Purpose: GraphViz Java API
 * <dd>
 * <p>
 * <dt>Description:
 * <dd> With this Java class you can simply call dot
 * from your Java programs.
 * <dt>Example usage:
 * <dd>
 * <pre>
 *    GraphViz gv = new GraphViz();
 *    gv.addln(gv.start_graph());
 *    gv.addln("A -> B;");
 *    gv.addln("A -> C;");
 *    gv.addln(gv.end_graph());
 *    System.out.println(gv.getDotSource());
 *
 *    String type = "gif";
 *    String representationType="dot";
 *    File out = new File("out." + type);   // out.gif in this example
 *    gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, representationType), out );
 * </pre>
 * </dd>
 * <p>
 * </dl>
 *
 * @author Laszlo Szathmary (<a href="jabba.laci@gmail.com">jabba.laci@gmail.com</a>)
 * @version v0.1, 2003/12/04 (December) -- first release
 */
@Slf4j
public class GraphViz {

    private static final GraphViz.Os os = detectOs();

    /**
     * The image size in dpi. 96 dpi is normal size. Higher values are 10% higher each.
     * Lower values 10% lower each.
     * <p>
     * dpi patch by Peter Mueller
     */
    private DpiSizes dpiSize = DpiSizes.DPI_96;

    /**
     * The source of the graph written in dot language.
     */
    private StringBuilder graph = new StringBuilder();

    private String tempDir;

    private String executable;

    private RepresentationType representationType = RepresentationType.DOT;

    private Type type = Type.PNG;

    /**
     * Configurable Constructor with path to executable dot and a temp dir
     *
     * @param executable absolute path to dot executable
     * @param tempDir    absolute path to temp directory
     */
    public GraphViz(String executable, String tempDir) {
        this.executable = executable;
        this.tempDir = tempDir;
    }

    /**
     * Convenience Constructor with default OS specific pathes
     * creates a new GraphViz object that will contain a graph.
     * Windows:
     * executable = c:/Program Files (x86)/Graphviz 2.28/bin/dot.exe
     * tempDir = c:/temp
     * MacOs:
     * executable = /usr/local/bin/dot
     * tempDir = /tmp
     * Linux:
     * executable = /usr/bin/dot
     * tempDir = /tmp
     */
    public GraphViz() {
        this.executable = findDot();
        this.tempDir = findTempDir();
    }

    private String findTempDir() {
        final String value = System.getProperty("java.io.tmpdir");
        if (value != null) {
            return value;
        }
        switch (os) {
            case WINDOWS:
                return "c:/temp";
            case LINUX:
            case MAC:
                return "/tmp";
            default:
                throw new IllegalStateException("Unknown OS");
        }
    }

    private String findDot() {
        final String value = System.getenv("GRAPHVIZ_DOT");
        if (value != null) {
            return value;
        }
        switch (os) {
            case WINDOWS:
                return "c:/Program Files (x86)/Graphviz 2.28/bin/dot.exe";
            case LINUX:
                return "/usr/bin/dot";
            case MAC:
                return "/usr/local/bin/dot";
            default:
                throw new IllegalStateException("Unknown OS");
        }
    }


    /**
     * Sets image dpi.
     *
     * @param dpiSize the dpi size
     */
    public void setImageDpi(final DpiSizes dpiSize) {
        this.dpiSize = dpiSize;
    }

    /**
     * Gets image dpi.
     *
     * @return the image dpi
     */
    public int getImageDpi() {
        return this.dpiSize.value();
    }

    /**
     * Returns the graph's source description in dot language.
     *
     * @return Source of the graph in dot language.
     */
    public String source() {
        return this.graph.toString();
    }

    /**
     * Start a graph.
     *
     * @param name Graph name.
     */
    public void startGraph(final String name) {
        this.graph.append(format("digraph %s {", name)).append("\n");
    }

    /**
     * End a graph.
     */
    public void endGraph() {
        this.graph.append("}").append("\n");
    }

    /**
     * Takes the cluster or subgraph id as input parameter start a subgraph.
     *
     * @param clusterid the clusterid
     */
    public void startSubgraph(int clusterid) {
        this.graph
            .append("subgraph cluster_")
            .append(clusterid)
            .append(" {")
            .append("\n");
    }

    /**
     * End a subgraph.
     */
    public void endSubgraph() {
        this.graph.append("}").append("\n");
    }

    /**
     * Add Node.
     *
     * @param name  the name
     * @param label the label
     */
    public void node(final String name, final String label) {
        addln(format("%s [label=\"%s\"];", name, label));
    }

    /**
     * Add Node.
     *
     * @param name the name
     */
    public void node(final String name) {
        addln(format("%s;", name));
    }

    /**
     * Adds a string to the graph's source (without newline).
     *
     * @param line the line
     */
    public void add(final String line) {
        this.graph.append(line);
    }

    /**
     * Adds a string to the graph's source (with newline).
     *
     * @param line the line
     */
    public void addln(final String line) {
        this.graph.append(line).append("\n");
    }

    /**
     * Adds a newline to the graph's source.
     */
    public void addln() {
        this.graph.append('\n');
    }

    /**
     * Clear graph.
     */
    public void clearGraph() {
        this.graph = new StringBuilder();
    }

    /**
     * Returns the graph as an image in binary format.
     *
     * @param dot Dot file
     * @return A byte array containing the image of the graph. http://www.graphviz.org under the Roadmap title
     */
    private byte[] process(final File dot) {
        final byte[] imgStream = getImgStream(dot);
        if (!dot.delete()) {
            log.warn("Warning: " + dot.getAbsolutePath() + " could not be deleted!");
        }
        return imgStream;
    }

    /**
     * Writes the graph's image in a fileName.
     *
     * @param fileName Name of the fileName to where we want to write.
     * @return true on Success
     */
    public boolean writeTo(final String fileName) {
        return writeTo(new File(fileName + "." + type.value()));
    }

    /**
     * Writes the graph's image in a file.
     *
     * @param to A File object to where we want to write.
     *           * @return true on Success
     */
    private boolean writeTo(final File to) {
        try (FileOutputStream fos = new FileOutputStream(to)) {
            fos.write(process(fileFrom(source())));
        } catch (RuntimeException | IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * It will call the external dot program, and return the image in binary format.
     *
     * @param dot Source of the graph (in dot language).
     *            http://www.graphviz.org under the Roadmap title
     * @return The image of the graph in .gif format.
     */
    private byte[] getImgStream(final File dot) {

        final File img;
        try {
            img = File.createTempFile("graph_", "." + type.value(), new File(this.tempDir));
        } catch (IOException ioe1) {
            throw new IllegalStateException("Error: in I/O processing of tempfile in dir " + tempDir, ioe1);
        }

        try {
            exec(dot, img);
        } catch (java.io.IOException ioe) {
            throw new IllegalStateException("Error: in calling external command", ioe);
        } catch (java.lang.InterruptedException ie) {
            throw new IllegalStateException("Error: the execution of the external program was interrupted", ie);
        }

        try (FileInputStream in = new FileInputStream(img.getAbsolutePath())) {

            byte[] imgStream = new byte[in.available()];
            int read = in.read(imgStream);
            if (read < imgStream.length) {
                log.error("Error: in read file");
            }
            if (!img.delete()) {
                log.warn("Warning: " + img.getAbsolutePath() + " could not be deleted!");
            }
            return imgStream;
        } catch (java.io.IOException ioe) {
            throw new IllegalStateException("Error: in I/O processing of tempfile in dir " + tempDir, ioe);
        }
    }

    private void exec(File dot, File img) throws IOException, InterruptedException {
        // patch by Mike Chenault
        // representation type with -K argument by Olivier Duplouy
        String[] args = {
            executable,
            "-T" + type.value(),
            "-K" + representationType.value(),
            "-Gdpi=" + dpiSize.value(),
            dot.getAbsolutePath(),
            "-o",
            img.getAbsolutePath()
        };
        Process p = Runtime.getRuntime().exec(args);
        p.waitFor();
    }

    /**
     * Writes the source of the graph in a file, and returns the written file
     * as a File object.
     *
     * @param str Source of the graph (in dot language).
     * @return The file (as a File object) that contains the source of the graph.
     */
    private File fileFrom(final String str) throws java.io.IOException {
        final File temp = File.createTempFile("graph_", ".dot.tmp", new File(tempDir));
        try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp), "UTF-8"))) {
            out.write(str);
        } catch (IOException e) {
            throw new IllegalStateException("Error: I/O error while writing the dot source to temp file!", e);
        }
        return temp;
    }


    /**
     * Detects the client's operating system.
     */
    private static Os detectOs() {
        final String osName = System.getProperty("os.name").replaceAll("\\s", "");
        if (osName.startsWith("Windows")) {
            return Os.WINDOWS;
        } else if ("MacOSX".equals(osName)) {
            return Os.MAC;
        } else if ("Linux".equals(osName)) {
            return Os.LINUX;
        } else {
            return Os.UNKNOWN;
        }
    }

    /**
     * Sets representation type.
     *
     * @param representationType the representation type
     */
    public void setRepresentationType(final RepresentationType representationType) {
        this.representationType = representationType;
    }

    /**
     * Sets representation type.
     *
     * @param type the representation type
     */
    public void setType(final Type type) {
        this.type = type;
    }



    private enum Os {
        /**
         * Linux os.
         */
        LINUX,
        /**
         * Mac os.
         */
        MAC,
        /**
         * Windows os.
         */
        WINDOWS,
        /**
         * Unknown os.
         */
        UNKNOWN
    }

    /**
     * Type of how you want to represent the graph: dot, neato, fdp, sfdp, twopi, circo.
     */
    public enum RepresentationType {
        /**
         * Dot representation type.
         */
        DOT("dot"),
        /**
         * Fig representation type.
         */
        NEATO("neato"),
        /**
         * Pdf representation type.
         */
        FDP("fdp"),
        /**
         * Ps representation type.
         */
        SFDP("sfdp"),
        /**
         * Svg representation type.
         */
        TWOPI("twopi"),
        /**
         * Png representation type.
         */
        CIRCO("circo");

        private final String value;

        RepresentationType(String value) {
            this.value = value;
        }

        /**
         * Value string.
         *
         * @return the string
         */
        public String value() {
            return value;
        }
    }

    /**
     * Type of the output image to be produced, e.g.: gif, dot, fig, pdf, ps, svg, png.
     */
    public enum Type {
        /**
         * Gif representation type.
         */
        GIF("gif"),
        /**
         * Dot representation type.
         */
        DOT("dot"),
        /**
         * Fig representation type.
         */
        FIG("fig"),
        /**
         * Pdf representation type.
         */
        PDF("pdf"),
        /**
         * Ps representation type.
         */
        PS("ps"),
        /**
         * Svg representation type.
         */
        SVG("svg"),
        /**
         * Png representation type.
         */
        PNG("png");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        /**
         * Value string.
         *
         * @return the string
         */
        public String value() {
            return value;
        }
    }

    /**
     * The enum Dpi sizes.
     */
    public enum DpiSizes {
        /**
         * Dpi 46 dpi sizes.
         */
        DPI_46(46),
        /**
         * Dpi 51 dpi sizes.
         */
        DPI_51(51),
        /**
         * Dpi 57 dpi sizes.
         */
        DPI_57(57),
        /**
         * Dpi 63 dpi sizes.
         */
        DPI_63(63),
        /**
         * Dpi 70 dpi sizes.
         */
        DPI_70(70),
        /**
         * Dpi 78 dpi sizes.
         */
        DPI_78(78),
        /**
         * Dpi 86 dpi sizes.
         */
        DPI_86(86),
        /**
         * Dpi 96 dpi sizes.
         */
        DPI_96(96),
        /**
         * Dpi 106 dpi sizes.
         */
        DPI_106(106),
        /**
         * Dpi 116 dpi sizes.
         */
        DPI_116(116),
        /**
         * Dpi 128 dpi sizes.
         */
        DPI_128(128),
        /**
         * Dpi 141 dpi sizes.
         */
        DPI_141(141),
        /**
         * Dpi 155 dpi sizes.
         */
        DPI_155(155),
        /**
         * Dpi 170 dpi sizes.
         */
        DPI_170(170),
        /**
         * Dpi 187 dpi sizes.
         */
        DPI_187(187),
        /**
         * Dpi 206 dpi sizes.
         */
        DPI_206(206),
        /**
         * Dpi 226 dpi sizes.
         */
        DPI_226(226),
        /**
         * Dpi 1249 dpi sizes.
         */
        DPI_249(249);

        private final int value;

        DpiSizes(int value) {
            this.value = value;
        }

        /**
         * Value int.
         *
         * @return the int
         */
        int value() {
            return value;
        }
    }


}