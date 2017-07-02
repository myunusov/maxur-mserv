package org.maxur.mserv.core.domain

abstract class Service {

    var beforeStart: MutableList<(Service) -> Unit> = ArrayList()
    var afterStop: MutableList<(Service) -> Unit> = ArrayList()
    var onError: MutableList<(Service, Exception) -> Unit> = ArrayList()

    protected var state: Service.State = Service.State.STOPPED
    private set

    /**
     * The service name
     */
    abstract var name: String

    /**
     * Start this Service
     */
    fun start() = state.start(this)

    /**
     * Immediately shuts down this Service.
     */
    fun stop() = state.stop(this)

    /**
     * shutdown this service.
     */
    abstract protected fun shutdown()

    /**
     * launch this service.
     */
    abstract protected fun launch()

    /**
     * Represent State of micro-service
     */
    enum class State {
        /**
         *  Running application
         */
        STARTED {
            override fun start(service: Service) = Unit
            override fun stop(service: Service) = shutdown(service)
            override fun restart(service: Service) {
                shutdown(service)
                launch(service)

            }
        },
        /**
         * Stop application
         */
        STOPPED {
            override fun start(service: Service) = launch(service)
            override fun stop(service: Service) = Unit
            override fun restart(service: Service) = launch(service)
        };
        abstract fun start(service: Service)
        abstract fun stop(service: Service)
        abstract fun restart(service: Service)

        protected fun shutdown(service: Service) {
            service.shutdown()
            service.state = Service.State.STOPPED
            service.afterStop.forEach { it.invoke(service) }
        }
        protected fun launch(service: Service) {
            service.beforeStart.forEach { it.invoke(service) }
            service.launch()
            service.state = Service.State.STARTED
        }
    }

}