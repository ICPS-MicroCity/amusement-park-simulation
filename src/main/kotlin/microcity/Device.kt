package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import org.protelis.lang.datatype.Tuple
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.ExecutionEnvironment
import microcity.Utils.role

object Device {

    private val DEVICE_PREFIX: String = "PtDevice"
    private val EMPTY_PREFIX: String = ""

    @JvmStatic
    fun getId(ctx: AlchemistExecutionContext<*>): Int =
            ctx.getDeviceUID().toString().replace(DEVICE_PREFIX, EMPTY_PREFIX).toInt()

    @JvmStatic
    fun getCoordinates(ctx: AlchemistExecutionContext<*>): Tuple = ctx.getCoordinates()

    @JvmStatic
    fun has(ctx: AlchemistExecutionContext<*>, molecule: String): Boolean = ctx.getExecutionEnvironment().has(molecule)

    @JvmStatic
    fun get(ctx: AlchemistExecutionContext<*>, molecule: String): Any = ctx.getExecutionEnvironment().get(molecule)

    @JvmStatic
    fun <T> put(ctx: AlchemistExecutionContext<*>, molecule: String, value: T): Any = ctx.getExecutionEnvironment().put(molecule, value)
}