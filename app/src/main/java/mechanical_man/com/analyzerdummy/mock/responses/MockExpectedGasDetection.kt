package mechanical_man.com.analyzerdummy.mock.responses

import com.mechanical_man.nst_proto.NSTProtos.Command
import com.mechanical_man.nst_proto.NSTProtos.Command.ExpectedGasResult
import com.mechanical_man.nst_proto.NSTProtos.Command.Gas.N2O
import com.mechanical_man.nst_proto.NSTProtos.Command.newBuilder

open class MockExpectedGasDetection {
  companion object {
    val SUCCESS: Command = newBuilder()
        .setExpectedGasResult(
            ExpectedGasResult.newBuilder()
                .setTestId(-1)
                .setExpectedGas(N2O)
                .build()
        )
        .build()
  }
}