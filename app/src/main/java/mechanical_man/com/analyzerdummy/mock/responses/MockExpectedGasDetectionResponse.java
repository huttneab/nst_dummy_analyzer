package mechanical_man.com.analyzerdummy.mock.responses;

import com.mechanical_man.nst_proto.NSTProtos;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public enum MockExpectedGasDetectionResponse {
    SUCCESS("success", MockExpectedGasDetection.INSTANCE.getSUCCESS());

    public final String name;
    public NSTProtos.Command response;

    MockExpectedGasDetectionResponse(String name, NSTProtos.Command response) {
        this.name = name;
        this.response = response;
    }

    @Override
    public String toString() {
        return name;
    }
}
