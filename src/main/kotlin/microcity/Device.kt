package microcity

import it.unibo.alchemist.protelis.AlchemistExecutionContext
import org.protelis.lang.datatype.Tuple
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.ExecutionEnvironment
import microcity.Utils.role

object Device {
    @JvmStatic
    fun getID(ctx: AlchemistExecutionContext<*>): DeviceUID = ctx.getDeviceUID()

    @JvmStatic
    fun has(ctx: AlchemistExecutionContext<*>, molecule: String): Boolean = ctx.getExecutionEnvironment().has(molecule)

    @JvmStatic
    fun get(ctx: AlchemistExecutionContext<*>, molecule: String): Any = ctx.getExecutionEnvironment().get(molecule)
}