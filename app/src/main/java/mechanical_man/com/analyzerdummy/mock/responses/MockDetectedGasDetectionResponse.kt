package mechanical_man.com.analyzerdummy.mock.responses

import com.mechanical_man.nst_proto.NSTProtos.Command

/**
 * Created by Mechanical Man on 12/11/17.
 */
enum class MockDetectedGasDetectionResponse(
  private val testName: String,
  var response: Command
) {
  SUCCESS("success", MockDetectedGasDetection.SUCCESS);

  override fun toString(): String {
    return testName
  }
}