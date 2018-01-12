package mechanical_man.com.analyzerdummy.mock.MockResponses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public enum MockGasDetectionResponse {
    SUCCESS("success", MockGasDetection.SUCCESS), FAIL("fail", MockGasDetection.FAIL);

    public final String name;
    public NSTProtos.Command response;

    MockGasDetectionResponse(String name, NSTProtos.Command response) {
        this.name = name;
        this.response = response;
    }

    @Override
    public String toString() {
        return name;
    }
}
