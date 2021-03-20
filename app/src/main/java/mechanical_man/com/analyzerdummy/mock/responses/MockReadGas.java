package mechanical_man.com.analyzerdummy.mock.responses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockReadGas {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setReadGasResult(NSTProtos.Command.ReadGasResult.newBuilder()
                    .setSuccess(true)
                    .setCo2(100)
                    .setN2(100)
                    .setN2O(100)
                    .setO2(100)
                    .build())
            .build();
}
