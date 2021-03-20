package mechanical_man.com.analyzerdummy.mock.responses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockReadFlow {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setReadFlowResult(NSTProtos.Command.ReadFlowResult.newBuilder()
                    .setFlow(100)
                    .build())
            .build();
}
