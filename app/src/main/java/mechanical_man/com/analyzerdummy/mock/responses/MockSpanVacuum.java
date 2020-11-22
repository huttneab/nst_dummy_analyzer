package mechanical_man.com.analyzerdummy.mock.responses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockSpanVacuum {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setSpanVacuumResult(NSTProtos.Command.SpanVacuumResult.newBuilder()
                    .setSuccess(true)
                    .build())
            .build();
}
