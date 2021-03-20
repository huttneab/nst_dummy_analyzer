package mechanical_man.com.analyzerdummy.mock.responses

import com.mechanical_man.nst_proto.NSTProtos.Command
import com.mechanical_man.nst_proto.NSTProtos.Command.DetectedGasResult
import com.mechanical_man.nst_proto.NSTProtos.Command.Gas.N2
import com.mechanical_man.nst_proto.NSTProtos.Command.Gas.O2
import com.mechanical_man.nst_proto.NSTProtos.Command.newBuilder

class MockDetectedGasDetection {
  companion object {
    val SUCCESS: Command = newBuilder()
        .setDetectedGasResult(
            DetectedGasResult.newBuilder()
                .setTestId(-1)
                .setDetectedGas(O2)
                .build()
        )
        .build()
  }
}