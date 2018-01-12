package mechanical_man.com.analyzerdummy.mock.MockResponses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockConcentration {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setConcentrationResult(NSTProtos.Command.ConcentrationResult.newBuilder()
                    .setTestId(1)
                    .setGasConcentration(55.0f)
                    .build())
            .build();
}
