package microcity

import it.unibo.alchemist.model.implementations.times.DoubleTime
import it.unibo.alchemist.model.interfaces.Time
import it.unibo.alchemist.protelis.AlchemistExecutionContext
import org.protelis.lang.datatype.Tuple
import kotlin.math.roundToInt

/**
 * Set of operations that can be performed on a device.
 */
object Devices {

    private const val DEVICE_PREFIX: String = "PtDevice"
    private const val EMPTY_PREFIX: String = ""
    private const val APPROX: Double = 10000000.0

    @JvmStatic
    fun getId(ctx: AlchemistExecutionContext<*>): Int =
        ctx.getDeviceUID().toString().replace(DEVICE_PREFIX, EMPTY_PREFIX).toInt()

    @JvmStatic
    fun getCurrentTime(ctx: AlchemistExecutionContext<*>): Time = DoubleTime(ctx.getCurrentTime().toDouble())

    @JvmStatic
    fun getCoordinates(ctx: AlchemistExecutionContext<*>): Tuple = ctx.getCoordinates().map { ((it as Double) * APPROX).roundToInt() / APPROX }

    @JvmStatic
    fun has(ctx: AlchemistExecutionContext<*>, molecule: String): Boolean = ctx.getExecutionEnvironment().has(molecule)

    @JvmStatic
    fun get(ctx: AlchemistExecutionContext<*>, molecule: String): Any = ctx.getExecutionEnvironment().get(molecule)

    @JvmStatic
    fun <T> put(ctx: AlchemistExecutionContext<*>, molecule: String, value: T): Any = ctx.getExecutionEnvironment().put(molecule, value)
}
