package mechanical_man.com.analyzerdummy.mock.responses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockStaticPressure {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setStaticPressureResult(NSTProtos.Command.StaticPressureResult.newBuilder()
                    .setTestId(1)
                    .setStaticPressure(55.0f)
                    .build())
            .build();

    public static final NSTProtos.Command FAIL = NSTProtos.Command.newBuilder()
            .setStaticPressureResult(NSTProtos.Command.StaticPressureResult.newBuilder()
                    .setTestId(1)
                    .setStaticPressure(0)
                    .build())
            .build();
}
