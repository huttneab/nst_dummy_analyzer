package mechanical_man.com.analyzerdummy.mock.MockResponses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockSpanFlow {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setSpanFlowResult(NSTProtos.Command.SpanFlowResult.newBuilder()
                    .setSuccess(true)
                    .build())
            .build();
}
