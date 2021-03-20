package mechanical_man.com.analyzerdummy.mock.responses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockPressureDrop {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setPressureDropResult(NSTProtos.Command.PressureDropResult.newBuilder()
                    .setTestId(1)
                    .setPressureDrop(5.0f)
                    .build())
            .build();

    public static final NSTProtos.Command FAIL = NSTProtos.Command.newBuilder()
            .setPressureDropResult(NSTProtos.Command.PressureDropResult.newBuilder()
                    .setTestId(1)
                    .setPressureDrop(1000)
                    .build())
            .build();
}
