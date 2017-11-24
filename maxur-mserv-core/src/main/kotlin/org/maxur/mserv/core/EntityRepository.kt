package org.maxur.mserv.core

import java.net.NetworkInterface
import java.net.SocketException
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger

/**
 * Repository of Entity.
 */
interface EntityRepository {

    /**
     * Constructs a new instance using the given [date].
     */
    fun <T> nextId(date: Date = Date()): Guid<T>

    /**
     * Constructs a new instances using the given [date] and [counter].
     * @throws IllegalArgumentException if the high order byte of counter is not zero
     */
    fun <T> nextId(date: Date, counter: Int): Guid<T>

    /**
     * Constructs a new instances using the given [timestamp], and [counter].
     * @throws IllegalArgumentException if the high order byte of machineIdentifier or counter is not zero
     */
    fun <T> nextId(timestamp: Int, counter: Int): Guid<T>
}

/**
 * Implementation of EntityRepository for standalone service.
 */
class LocalEntityRepository(machineIdentifier: Int? = null, processIdentifier: Short? = null) : EntityRepository {

    companion object {
        private val LOW_ORDER_THREE_BYTES = 0x00ffffff
    }

    private val nextCounter = AtomicInteger(SecureRandom().nextInt())

    /** Gets the process identifier. */
    val processIdentifier = processIdentifier ?: createProcessIdentifier()
    /** Gets the generated machine identifier. */
    val machineIdentifier = machineIdentifier ?: createMachineIdentifier()

    /** {@inheritDoc} */
    override fun <T> nextId(date: Date) = Guid<T>(
        dateToTimestampSeconds(date),
        machineIdentifier,
        processIdentifier,
        nextCounter.getAndIncrement(),
        false
    )

    /** {@inheritDoc} */
    override fun <T> nextId(date: Date, counter: Int) = Guid<T>(
        dateToTimestampSeconds(date),
        machineIdentifier,
        processIdentifier,
        counter,
        true
    )

    /** {@inheritDoc} */
    override fun <T> nextId(timestamp: Int, counter: Int) = Guid<T>(
        timestamp,
        machineIdentifier,
        processIdentifier,
        counter,
        true
    )

    private fun dateToTimestampSeconds(time: Date): Int = (time.time / 1000).toInt()

    private fun createMachineIdentifier(): Int {
        // build a 2-byte machine piece based on NICs info
        var machinePiece: Int
        try {
            val sb = StringBuilder()
            val e = NetworkInterface.getNetworkInterfaces()
            while (e.hasMoreElements()) {
                val ni = e.nextElement()
                sb.append(ni.toString())
                val mac = ni.hardwareAddress
                if (mac != null) {
                    val bb = ByteBuffer.wrap(mac)
                    try {
                        sb.append(bb.char)
                        sb.append(bb.char)
                        sb.append(bb.char)
                    } catch (shortHardwareAddressException: BufferUnderflowException) {
                        /*  NOPMD mac with less than 6 bytes. continue */
                    }
                }
            }
            machinePiece = sb.toString().hashCode()
        } catch (t: RuntimeException) {
            // exception sometimes happens with IBM JVM, use random
            machinePiece = SecureRandom().nextInt()
            //"Failed to get machine identifier from network interface, using random number instead"
        } catch (t: SocketException) {
            machinePiece = SecureRandom().nextInt()
            //"Failed to get machine identifier from network interface, using random number instead"
        }
        machinePiece = machinePiece and LOW_ORDER_THREE_BYTES
        return machinePiece
    }

    // Creates the process identifier.  This does not have to be unique per class loader because
    // NEXT_COUNTER will provide the uniqueness.
    private fun createProcessIdentifier() = try {
        val processName = java.lang.management.ManagementFactory.getRuntimeMXBean().name
        if (processName.contains("@")) {
            Integer.parseInt(processName.substring(0, processName.indexOf('@'))).toShort()
        } else {
            java.lang.management.ManagementFactory.getRuntimeMXBean().name.hashCode().toShort()
        }
    } catch (t: RuntimeException) {
        SecureRandom().nextInt().toShort()
        //"Failed to get process identifier from JMX, using random number instead"
    }
}