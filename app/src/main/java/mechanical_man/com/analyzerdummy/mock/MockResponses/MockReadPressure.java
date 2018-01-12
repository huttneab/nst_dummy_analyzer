package mechanical_man.com.analyzerdummy.mock.MockResponses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockReadPressure {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setReadPressureResult(NSTProtos.Command.ReadPressureResult.newBuilder()
                    .setSuccess(true)
                    .setPressure(100)
                    .build())
            .build();
}
