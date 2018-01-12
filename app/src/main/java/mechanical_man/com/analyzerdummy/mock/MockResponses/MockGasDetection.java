package mechanical_man.com.analyzerdummy.mock.MockResponses;

import com.mechanical_man.nst_proto.NSTProtos;

import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.N2;
import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.O2;
import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.WAGD;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockGasDetection {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setGasResult(NSTProtos.Command.GasResult.newBuilder()
                    .setTestId(1)
                    .setDetectedGas(N2)
                    .setExpectedGas(N2)
                    .build())
            .build();

    public static final NSTProtos.Command FAIL = NSTProtos.Command.newBuilder()
            .setGasResult(NSTProtos.Command.GasResult.newBuilder()
                    .setTestId(1)
                    .setDetectedGas(WAGD)
                    .setExpectedGas(O2)
                    .build())
            .build();
}
