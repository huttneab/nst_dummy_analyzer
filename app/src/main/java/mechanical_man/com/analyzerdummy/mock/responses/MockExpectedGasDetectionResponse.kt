package mechanical_man.com.analyzerdummy.mock.responses

import com.mechanical_man.nst_proto.NSTProtos

enum class MockExpectedGasDetectionResponse(
    private val testName: String,
    var response: NSTProtos.Command
) {
    SUCCESS("success", MockExpectedGasDetection.SUCCESS);

    override fun toString(): String{
        return testName
    }
}