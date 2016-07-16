package org.maxur.mserv;

/**
 * The type Condition.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>15.07.2016</pre>
 */
public class Condition {

    private final Object left;

    /**
     * On condition.
     *
     * @param left the left
     * @return the condition
     */
    public static Condition on(final Object left) {
        return new Condition(left);
    }

    private Condition(final Object left) {
        this.left = left;
    }

    /**
     * Is executor.
     *
     * @param right the right
     * @return the executor
     */
    public Executor is(Class<?> right) {
        final boolean result = right.isAssignableFrom(left.getClass());
        return result ? new Executor() : new NullExecutor();
    }

    /**
     * The type Executor.
     */
    public static class Executor {

        /**
         * Exec.
         *
         * @param function the function
         */
        public void exec(final Runnable function) {
            function.run();
        }
    }

    private static class NullExecutor extends Executor {

        @Override
        public void exec(final Runnable function) {
            // NOP
        }
    }

}
