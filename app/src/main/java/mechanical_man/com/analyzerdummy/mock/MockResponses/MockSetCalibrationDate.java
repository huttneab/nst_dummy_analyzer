package mechanical_man.com.analyzerdummy.mock.MockResponses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockSetCalibrationDate {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setCalibrationDateResult(NSTProtos.Command.CalibrationDateResult.newBuilder()
                    .setSuccess(true)
                    .build())
            .build();
}
