package mechanical_man.com.analyzerdummy.mock.responses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockTransientFlow {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setTransientFlowResult(NSTProtos.Command.TransientFlowResult.newBuilder()
                    .setTestId(1)
                    .setTransientFlow(55.0f)
                    .build())
            .build();
}
