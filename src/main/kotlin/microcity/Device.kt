package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import org.protelis.lang.datatype.Tuple
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.ExecutionEnvironment

object Device {
    @JvmStatic
    fun getPosition(ctx: AlchemistExecutionContext<*>): Tuple = ctx.getCoordinates()

    @JvmStatic
    fun getID(ctx: AlchemistExecutionContext<*>): DeviceUID = ctx.getDeviceUID()

    @JvmStatic
    fun has(ctx: AlchemistExecutionContext<*>, molecule: String): Boolean = ctx.getExecutionEnvironment().has(molecule)
}