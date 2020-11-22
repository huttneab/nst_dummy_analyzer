package mechanical_man.com.analyzerdummy.mock.responses

import com.mechanical_man.nst_proto.NSTProtos.Command
import com.mechanical_man.nst_proto.NSTProtos.Command.ExpectedGasResult
import com.mechanical_man.nst_proto.NSTProtos.Command.Gas.N2
import com.mechanical_man.nst_proto.NSTProtos.Command.newBuilder

object MockExpectedGasDetection {
  val SUCCESS: Command = newBuilder()
      .setExpectedGasResult(
          ExpectedGasResult.newBuilder()
              .setTestId(1)
              .setExpectedGas(N2)
              .build()
      )
      .build()
}