package org.maxur.mserv.core.core

import com.fasterxml.jackson.annotation.JsonIgnore
import java.net.NetworkInterface
import java.net.SocketException
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger

@Suppress("unused", "MemberVisibilityCanPrivate")
/**
 * The Global Unique Identifier.
 *
 * @param <T> the type parameter
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/25/2016</pre>
</T> */
class Guid<T> : Id<T>, Comparable<Guid<T>> {

    /**
     * Gets the timestamp (number of seconds since the Unix epoch).
     * @return the timestamp
     */
    @JsonIgnore
    var timestamp: Int = 0
        private set

    /**
     * Gets the machine identifier.
     * @return the machine identifier
     */
    @JsonIgnore
    var machineIdentifier: Int = 0
        private set

    /**
     * Gets the process identifier.
     * @return the process identifier
     */
    @JsonIgnore
    var processIdentifier: Short = 0
        private set

    /**
     * Gets the counter.
     * @return the counter
     */
    @JsonIgnore
    var counter: Int = 0
        private set

    /**
     * Gets the timestamp as a `Date` instance.
     * @return the Date
     */
    val date: Date
        get() = Date(timestamp * 1000L)

    /**
     * Constructs a new instance using the given [date].
     */
    @JvmOverloads constructor(date: Date = Date()) : this(
        dateToTimestampSeconds(date),
        generatedMachineIdentifier,
        Guid.processIdentifier,
        NEXT_COUNTER.getAndIncrement(),
        false
    )

    /**
     * Constructs a new instances using the given [date] and [counter].
     * @throws IllegalArgumentException if the high order byte of counter is not zero
     */
    constructor(date: Date, counter: Int) : this(
        date,
        generatedMachineIdentifier,
        Guid.processIdentifier,
        counter
    )

    /**
     * Constructs a new instances using the given [date],
     * machine identifier as [machineIdentifier],
     * process identifier as [processIdentifier], and [counter].
     * @throws IllegalArgumentException if the high order byte of machineIdentifier or counter is not zero
     */
    constructor(date: Date, machineIdentifier: Int, processIdentifier: Short, counter: Int) : this(
        dateToTimestampSeconds(date),
        machineIdentifier,
        processIdentifier,
        counter
    )

    /**
     * Constructs a new instances using the given [timestamp],
     * machine identifier as [machineIdentifier],
     * process identifier as [processIdentifier], and [counter].
     * @throws IllegalArgumentException if the high order byte of machineIdentifier or counter is not zero
     */
    constructor(timestamp: Int, machineIdentifier: Int, processIdentifier: Short, counter: Int) : this(
        timestamp,
        machineIdentifier,
        processIdentifier,
        counter,
        true
    )

    private constructor(
        timestamp: Int,
        machineIdentifier: Int,
        processIdentifier: Short,
        counter: Int,
        checkCounter: Boolean
    ) {
        if (machineIdentifier and -0x1000000 != 0) {
            throw IllegalArgumentException(
                "The machine identifier must be between 0 and 16777215 (it must fit in three bytes)."
            )
        }
        if (checkCounter && counter and -0x1000000 != 0) {
            throw IllegalArgumentException(
                "The counter must be between 0 and 16777215 (it must fit in three bytes)."
            )
        }
        this.timestamp = timestamp
        this.machineIdentifier = machineIdentifier
        this.processIdentifier = processIdentifier
        this.counter = counter and LOW_ORDER_THREE_BYTES
    }

    /**
     * Constructs a new instance from a 24-byte hexadecimal string representation.
     * @param hexString the string to convert
     * @throws IllegalArgumentException if the string is not a valid hex string representation of an Id
     */
    constructor(hexString: String) : this(parseHexString(hexString))

    /**
     * Constructs a new instance from the given byte array
     * @param bytes the byte array
     * @throws IllegalArgumentException if array is null or not of length 12
     */
    constructor(bytes: ByteArray?) {
        if (bytes == null) {
            throw IllegalArgumentException()
        }
        if (bytes.size != 12) {
            throw IllegalArgumentException("need 12 bytes")
        }
        timestamp = makeInt(bytes[0], bytes[1], bytes[2], bytes[3])
        machineIdentifier = makeInt(0.toByte(), bytes[4], bytes[5], bytes[6])
        processIdentifier = makeInt(0.toByte(), 0.toByte(), bytes[7], bytes[8]).toShort()
        counter = makeInt(0.toByte(), bytes[9], bytes[10], bytes[11])
    }

    /**
     * Convert to a byte array.  Note that the numbers are stored in big-endian order.
     * @return the byte array
     */
    fun toByteArray(): ByteArray {
        val bytes = ByteArray(12)
        bytes[0] = int3(timestamp)
        bytes[1] = int2(timestamp)
        bytes[2] = int1(timestamp)
        bytes[3] = int0(timestamp)
        bytes[4] = int2(machineIdentifier)
        bytes[5] = int1(machineIdentifier)
        bytes[6] = int0(machineIdentifier)
        bytes[7] = short1(processIdentifier)
        bytes[8] = short0(processIdentifier)
        bytes[9] = int2(counter)
        bytes[10] = int1(counter)
        bytes[11] = int0(counter)
        return bytes
    }

    /**
     * Converts this instance into a 24-byte hexadecimal string representation.
     * @return a string representation of the Id in hexadecimal format
     */
    fun toHexString(): String {
        val chars = CharArray(24)
        var i = 0
        for (b in toByteArray()) {
            chars[i++] = HEX_CHARS[b.toInt() shr 4 and 0xF]
            chars[i++] = HEX_CHARS[b.toInt() and 0xF]
        }
        return String(chars)
    }

    /** {@inheritDoc} */
    override fun asString() = toHexString()
    /** {@inheritDoc} */
    override fun toString() = toHexString()
    /** {@inheritDoc} */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val id = other as Guid<*>
        return counter == id.counter &&
            machineIdentifier == id.machineIdentifier &&
            processIdentifier == id.processIdentifier &&
            timestamp == id.timestamp
    }
    /** {@inheritDoc} */
    override fun hashCode(): Int {
        var result = timestamp
        result = 31 * result + machineIdentifier
        result = 31 * result + processIdentifier.toInt()
        result = 31 * result + counter
        return result
    }
    /** {@inheritDoc} */
    override fun compareTo(other: Guid<T>): Int {
        val byteArray = toByteArray()
        val otherByteArray = other.toByteArray()
        return (0..11)
            .firstOrNull { byteArray[it] != otherByteArray[it] }
            ?.let { if (byteArray[it].toInt() and 0xff < otherByteArray[it].toInt() and 0xff) -1 else 1 }
            ?: 0
    }

    companion object {

        private val LOW_ORDER_THREE_BYTES = 0x00ffffff

        private val HEX_CHARS =
            charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

        private val NEXT_COUNTER = AtomicInteger(SecureRandom().nextInt())

        /** Gets the process identifier. */
        val processIdentifier = createProcessIdentifier()
        /** Gets the generated machine identifier. */
        val generatedMachineIdentifier = createMachineIdentifier()
        /** Gets the current value of the auto-incrementing counter. */
        val currentCounter: Int
            get() = NEXT_COUNTER.get()

        /**
         * Creation method.
         * @param <T>        Type of Entity Identifier
         * @param identifier The entity identifier
         * @return Entity Identifier
        </T> */
        fun <T> id(identifier: String): Guid<T> = Guid(identifier)

        /**
         * Creation method.
         * @param <T>        Type of Entity Identifier
         * @param identifier The entity identifier
         * @return Entity Identifier
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> id(identifier: Id<Any>): Guid<T> = when (identifier) {
            is Guid -> identifier as Guid<T>
            else -> Guid(identifier.asString())
        }

        /**
         * Gets a new identifier.
         * @param <C> entity Type
         * @return the new id
         */
        fun <C> nextId(): Guid<C> = Guid()

        /**
         * Checks if a string could be an `Id`.
         *
         * @param hexString a potential Id as a String.
         * @return whether the string could be an object id
         * @throws IllegalArgumentException if hexString is null
         */
        fun isValid(hexString: String): Boolean = (hexString.length == 24) &&
            (0 until hexString.length).none { Character.digit(hexString[it], 16) == -1 }

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
                            /*
                        * NOPMD
                        * mac with less than 6 bytes. continue
                        */
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

        private fun parseHexString(s: String): ByteArray {
            if (!isValid(s)) {
                throw IllegalArgumentException("invalid hexadecimal representation of an Id: [$s]")
            }
            val b = ByteArray(12)
            for (i in b.indices) {
                b[i] = Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16).toByte()
            }
            return b
        }

        private fun dateToTimestampSeconds(time: Date): Int = (time.time / 1000).toInt()

        // Big-Endian helpers, in this class because all other BSON numbers are little-endian
        private fun makeInt(b3: Byte, b2: Byte, b1: Byte, b0: Byte): Int =
            (b3.toInt() shl 24) or
            (b2.toInt() and 0xff shl 16) or
            (b1.toInt() and 0xff shl 8) or
            (b0.toInt() and 0xff)

        private fun int3(x: Int): Byte = (x shr 24).toByte()

        private fun int2(x: Int): Byte = (x shr 16).toByte()

        private fun int1(x: Int): Byte = (x shr 8).toByte()

        private fun int0(x: Int): Byte = x.toByte()

        private fun short1(x: Short): Byte = (x.toInt() shr 8).toByte()

        private fun short0(x: Short): Byte = x.toByte()
    }
}
