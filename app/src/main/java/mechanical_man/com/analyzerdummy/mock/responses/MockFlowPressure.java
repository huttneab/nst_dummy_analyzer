package mechanical_man.com.analyzerdummy.mock.responses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockFlowPressure {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setFlowResult(NSTProtos.Command.FlowResult.newBuilder()
                    .setTestId(1)
                    .setFlowPressure(55.0f)
                    .build())
            .build();

    public static final NSTProtos.Command FAIL = NSTProtos.Command.newBuilder()
            .setFlowResult(NSTProtos.Command.FlowResult.newBuilder()
                    .setTestId(1)
                    .setFlowPressure(0)
                    .build())
            .build();
}
