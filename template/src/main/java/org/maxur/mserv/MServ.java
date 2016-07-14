package org.maxur.mserv;

import org.maxur.mserv.properties.PropertiesFile;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The type M serv.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>14.07.2016</pre>
 */
public final class MServ {

    private Function<MServ, Result> hookOnStart = this::nop;

    private Function<MServ, Result> hookOnStop = this::nop;

    private BiFunction<MServ, RuntimeException, Result> hookOnError = this::nop;

    private PropertiesFile propertiesFile;

    private MServ() {
    }

    /**
     * Create m serv.
     *
     * @return the m serv
     */
    public static MServ create() {
        return new MServ();
    }

    /**
     * Read properties from properties file.
     *
     * @param propertiesFile the properties file
     * @return the Child
     */
    public PropertiesFileBinder loadPropertiesFrom(final PropertiesFile propertiesFile) {
        this.propertiesFile = propertiesFile;
        return wrap(propertiesFile);
    }

    private PropertiesFileBinder wrap(final PropertiesFile propertiesFile) {
        return propertiesClass -> {
            propertiesFile.bindPropertiesClass(propertiesClass);
            return MServ.this;
        };
    }

    /**
     * Load config from m serv.
     *
     * @param configClass the config class
     * @return the m serv
     */
    public MServ loadConfigFrom(Class<? extends SysConfig> configClass) {
        return this;
    }

    /**
     * Run.
     */
    public void run() {
        try {
            propertiesFile.load();
            hookOnStart.apply(this);
            hookOnStop.apply(this);
        } catch (RuntimeException e) {
            hookOnError.apply(this, e);
        }
    }

    /**
     * Hook on start m serv.
     *
     * @param hook the hook
     * @return the m serv
     */
    public MServ hookOnStart(Function<MServ, Result> hook) {
        hookOnStart = hook;
        return this;
    }

    /**
     * Hook on stop m serv.
     *
     * @param hook the hook
     * @return the m serv
     */
    public MServ hookOnStop(Function<MServ, Result> hook) {
        hookOnStop = hook;
        return this;
    }

    /**
     * Hook on error m serv.
     *
     * @param hook the hook
     * @return the m serv
     */
    public MServ hookOnError(BiFunction<MServ, RuntimeException, Result> hook) {
        hookOnError = hook;
        return this;
    }

    private Result nop(MServ mServ) {
        // TODO
        return null;
    }

    private Result nop(MServ mServ, RuntimeException e) {
        // TODO
        return null;
    }

}
