package mechanical_man.com.analyzerdummy.mock.responses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockSensorZero {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setSensorZeroResult(NSTProtos.Command.SensorZeroResult.newBuilder()
                    .setSuccess(true)
                    .setFlow(100)
                    .setPressure(100)
                    .setVacuum(-100)
                    .build())
            .build();
}
