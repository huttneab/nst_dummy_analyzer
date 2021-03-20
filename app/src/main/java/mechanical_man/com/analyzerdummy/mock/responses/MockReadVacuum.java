package mechanical_man.com.analyzerdummy.mock.responses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockReadVacuum {
    public static final NSTProtos.Command SUCCESS = NSTProtos.Command.newBuilder()
            .setReadVacuumResult(NSTProtos.Command.ReadVacuumResult.newBuilder()
                    .setSuccess(true)
                    .setVacuum(-100)
                    .build())
            .build();
}
