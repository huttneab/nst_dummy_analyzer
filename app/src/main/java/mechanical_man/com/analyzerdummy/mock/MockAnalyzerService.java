package mechanical_man.com.analyzerdummy.mock;

import android.content.SharedPreferences;

import com.mechanical_man.nst_proto.NSTProtos;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.subjects.BehaviorSubject;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockCalibrationDateResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockCancelResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockConcentrationResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockFlowPressureResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockGasDetectionResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockGasZeroResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockPressureDropResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockReadFlowResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockReadGasResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockReadPressureResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockReadVacuumResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockSensorZeroResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockSpanFlowResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockSpanGasResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockSpanPressureResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockSpanVacuumResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockStaticPressureResponse;
import mechanical_man.com.analyzerdummy.mock.MockResponses.MockTransientFlowResponse;
import mechanical_man.com.analyzerdummy.util.EnumPreferences;

/**
 * Created by Mechanical Man on 12/11/17.
 */

public class MockAnalyzerService {

    private final Map<Class<? extends Enum<?>>, Enum<?>> responses = new LinkedHashMap<>();
    private final BehaviorSubject<String> requestSubject = BehaviorSubject.create();
    private SharedPreferences preferences;

    private static MockAnalyzerService instance;

    public static MockAnalyzerService getInstance(SharedPreferences preferences) {
        if (instance == null) {
            instance = new MockAnalyzerService();
            instance.preferences = preferences;
            instance.loadData(preferences);
        }
        return instance;
    }


    private void loadData(SharedPreferences preferences) {

        loadResponse(MockFlowPressureResponse.class, MockFlowPressureResponse.SUCCESS);
        loadResponse(MockConcentrationResponse.class, MockConcentrationResponse.SUCCESS);
        loadResponse(MockGasDetectionResponse.class, MockGasDetectionResponse.SUCCESS);
        loadResponse(MockPressureDropResponse.class, MockPressureDropResponse.SUCCESS);
        loadResponse(MockTransientFlowResponse.class, MockTransientFlowResponse.SUCCESS);
        loadResponse(MockStaticPressureResponse.class, MockStaticPressureResponse.SUCCESS);
        loadResponse(MockGasZeroResponse.class, MockGasZeroResponse.SUCCESS);
        loadResponse(MockSensorZeroResponse.class, MockSensorZeroResponse.SUCCESS);
        loadResponse(MockCancelResponse.class, MockCancelResponse.SUCCESS);
        loadResponse(MockReadGasResponse.class, MockReadGasResponse.SUCCESS);
        loadResponse(MockSpanGasResponse.class, MockSpanGasResponse.SUCCESS);
        loadResponse(MockCalibrationDateResponse.class, MockCalibrationDateResponse.SUCCESS);
        loadResponse(MockReadFlowResponse.class, MockReadFlowResponse.SUCCESS);
        loadResponse(MockReadPressureResponse.class, MockReadPressureResponse.SUCCESS);
        loadResponse(MockSpanFlowResponse.class, MockSpanFlowResponse.SUCCESS);
        loadResponse(MockSpanPressureResponse.class, MockSpanPressureResponse.SUCCESS);
        loadResponse(MockSpanVacuumResponse.class, MockSpanVacuumResponse.SUCCESS);
        loadResponse(MockReadVacuumResponse.class, MockReadVacuumResponse.SUCCESS);

    }

    public NSTProtos.Command getResponseForCommand(NSTProtos.Command command) {
        switch (command.getTypeOneofCase()) {
            case EXPECTED_GAS_TEST:
                return getResponse(MockExpectedGasResponse.class).response;
            case DETECTED_GAS_TEST:
                return getResponse(MockDetectedGasResponse.class).response;
            case FLOW_TEST:
                return getResponse(MockFlowPressureResponse.class).response;
            case CONCENTRATION_TEST:
                return getResponse(MockConcentrationResponse.class).response;
            case PRESSURE_DROP_TEST:
                return getResponse(MockPressureDropResponse.class).response;
            case TRANSIENT_FLOW_TEST:
                return getResponse(MockTransientFlowResponse.class).response;
            case STATIC_PRESSURE_TEST:
                return getResponse(MockStaticPressureResponse.class).response;
            case GAS_ZERO:
                return getResponse(MockGasZeroResponse.class).response;
            case SENSOR_ZERO:
                return getResponse(MockSensorZeroResponse.class).response;
            case CANCEL:
                return getResponse(MockCancelResponse.class).response;
            case READ_GAS:
                return getResponse(MockReadGasResponse.class).response;
            case SPAN_GAS:
                return getResponse(MockSpanGasResponse.class).response;
            case CALIBRATION_DATE:
                return getResponse(MockCalibrationDateResponse.class).response;
            case READ_FLOW:
                return getResponse(MockReadFlowResponse.class).response;
            case READ_PRESSURE:
                return getResponse(MockReadPressureResponse.class).response;
            case SPAN_FLOW:
                return getResponse(MockSpanFlowResponse.class).response;
            case SPAN_PRESSURE:
                return getResponse(MockSpanPressureResponse.class).response;
            case SPAN_VACUUM:
                return getResponse(MockSpanVacuumResponse.class).response;
            case READ_VACUUM:
                return getResponse(MockReadVacuumResponse.class).response;
            default:
                return null;
        }
    }


    /**
     * Initializes the current response for {@code responseClass} from {@code SharedPreferences}, or
     * uses {@code defaultValue} if a response was not found.
     */
    private <T extends Enum<T>> void loadResponse(Class<T> responseClass, T defaultValue) {
        responses.put(responseClass, EnumPreferences.getEnumValue(preferences, responseClass, //
                responseClass.getCanonicalName(), defaultValue));
    }

    public <T extends Enum<T>> T getResponse(Class<T> responseClass) {
        requestSubject.onNext(responseClass.getSimpleName());
        return responseClass.cast(responses.get(responseClass));
    }

}
