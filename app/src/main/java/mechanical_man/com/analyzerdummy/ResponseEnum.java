package mechanical_man.com.analyzerdummy;

/**
 * Created by Mechanical Man on 11/20/17.
 */

public enum ResponseEnum {
  EXPECTED_GAS(),
  DETECTED_GAS(),
  STATIC_PRESSURE(),
  FLOW_RATE(),
  PRESSURE_DROP(),
  TRANSIENT_FLOW(),
  CONCENTRATION(),
  GAS_ZERO(),
  SENSOR_ZERO(),
  READ_GAS(),
  SPAN_GAS(),
  READ_FLOW(),
  SPAN_FLOW(),
  READ_PRESSURE(),
  SPAN_PRESSURE(),
  READ_VACUUM(),
  SPAN_VACUUM(),
  RESET(),
  CANCEL(),
  CALIBRATION_DATE(),
  ANALYZER_INFO(),
  VAC_PRESSURE_TEST(),
  VAC_FLOW_TEST(),
  BATTERY_INFO(),
}
