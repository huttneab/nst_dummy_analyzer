package mechanical_man.com.analyzerdummy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mechanical_man.nst_proto.NSTProtos;
import com.mechanical_man.nst_proto.NSTProtos.Command.AnalyzerInfoResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.BatteryInfoResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.CalibrationDateResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.ConcentrationResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.DetectedGasResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.ExpectedGasResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.FlowResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.GasZeroResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.PressureDropResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.ReadFlowResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.ReadGasResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.ReadVacuumResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.ResetResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.SensorZeroResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.SpanGasResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.SpanPressureResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.SpanVacuumResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.StaticPressureResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.TransientFlowResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.VACFlowResult;
import com.mechanical_man.nst_proto.NSTProtos.Command.VACPressureResult;
import mechanical_man.com.analyzerdummy.util.EnumAdapter;

import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.AIR;
import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.CO2;
import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.CO2_VALUE;
import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.N2;
import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.N2O;
import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.NONE;
import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.O2;
import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.UNKNOWN;
import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.VAC;
import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.VAC_F;
import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.WAGD;
import static com.mechanical_man.nst_proto.NSTProtos.Command.Gas.WAGD_F;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = "MainActivity";

  @BindView(R.id.log_view) ListView logView;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.result_spinner) Spinner resultSpinner;

  private static final int REQUEST_ENABLE_BT = 3;
  private long testId = 0;

  /**
   * Name of the connected device
   */
  private String mConnectedDeviceName = null;

  /**
   * Array adapter for the conversation thread
   */
  private ArrayAdapter<String> mConversationArrayAdapter;

  /**
   * String buffer for outgoing messages
   */
  private StringBuffer mOutStringBuffer;

  /**
   * Local Bluetooth adapter
   */
  private BluetoothAdapter mBluetoothAdapter = null;

  /**
   * Member object for the chat services
   */
  private BluetoothService bluetoothService = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    // If the adapter is null, then Bluetooth is not supported
    if (mBluetoothAdapter == null) {
      Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
    }

    EnumAdapter<ResponseEnum> adapter = new EnumAdapter<>(this, ResponseEnum.class);
    resultSpinner.setAdapter(adapter);
  }

  @Override
  public void onStart() {
    super.onStart();
    // If BT is not on, request that it be enabled.
    // setupChat() will then be called during onActivityResult
    if (!mBluetoothAdapter.isEnabled()) {
      Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
      // Otherwise, setup the chat session
    } else if (bluetoothService == null) {
      setupChat();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (bluetoothService != null) {
      bluetoothService.stop();
    }
  }

  @Override
  public void onResume() {
    super.onResume();

    // Performing this check in onResume() covers the case in which BT was
    // not enabled during onStart(), so we were paused to enable it...
    // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
    if (bluetoothService != null) {
      // Only if the state is STATE_NONE, do we know that we haven't started already
      if (bluetoothService.getState() == BluetoothService.STATE_NONE) {
        // Start the Bluetooth chat services
        bluetoothService.start();
      }
    }
  }

  /**
   * Set up the UI and background operations for chat.
   */
  private void setupChat() {
    Log.d(TAG, "setupChat()");

    // Initialize the array adapter for the conversation thread
    mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);

    logView.setAdapter(mConversationArrayAdapter);

    // Initialize the BluetoothChatService to perform bluetooth connections
    bluetoothService = new BluetoothService(this, mHandler);

    // Initialize the buffer for outgoing messages
    mOutStringBuffer = new StringBuffer("");
  }

  @OnClick(R.id.clear)
  public void onClickClear(View v) {
    mConversationArrayAdapter.clear();
  }

  @OnClick(R.id.discovery)
  public void onClickDiscovery(View v) {
    if (mBluetoothAdapter.getScanMode() !=
        BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
      mConversationArrayAdapter.add("making device discoverable");
      Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
      discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
      startActivity(discoverableIntent);
    }
  }

  @OnClick(R.id.start)
  public void onClickStart(View V) {
    mConversationArrayAdapter.add("starting bluetooth service");
    bluetoothService.start();
  }

  @OnClick(R.id.stop)
  public void onClickStop(View V) {
    mConversationArrayAdapter.add("stopping bluetooth service");
    bluetoothService.stop();
  }

  @OnClick(R.id.send)
  public void onClickSend(View v) {
    NSTProtos.Command.Builder builder = NSTProtos.Command.newBuilder();

    switch ((ResponseEnum) resultSpinner.getSelectedItem()) {
      case EXPECTED_GAS:
        builder.setExpectedGasResult(ExpectedGasResult.newBuilder()
            .setTestId((int) testId)
            .setExpectedGas(AIR)
            .build());
        break;

      case DETECTED_GAS:
        builder.setDetectedGasResult(DetectedGasResult.newBuilder()
            .setTestId((int) testId)
            .setDetectedGas(AIR)
            .build());
        break;

      case STATIC_PRESSURE:
        builder.setStaticPressureResult(StaticPressureResult.newBuilder()
            .setTestId((int) testId)
            .setStaticPressure(55.0f)
            .build());
        break;

      case FLOW_RATE:
        builder.setFlowResult(FlowResult.newBuilder()
            .setTestId((int) testId)
            .setFlowPressure(55.0f)
            .build());
        break;

      case PRESSURE_DROP:
        builder.setPressureDropResult(PressureDropResult.newBuilder()
            .setTestId((int) testId)
            .setPressureDrop(5.0f)
            .build());
        break;

      case TRANSIENT_FLOW:
        builder.setTransientFlowResult(TransientFlowResult.newBuilder()
            .setTestId((int) testId)
            .setTransientFlow(55.0f)
            .build());
        break;

      case CONCENTRATION:
        builder.setConcentrationResult(ConcentrationResult.newBuilder()
            .setTestId((int) testId)
            .setGasConcentration(55.0f)
            .build());
        break;

      case GAS_ZERO:
        builder.setGasZeroResult(GasZeroResult.newBuilder()
            .setSuccess(true)
            .setCo2(100)
            .setN2(100)
            .setN2O(100)
            .setO2(100)
            .build());
        break;
      case SENSOR_ZERO:
        builder.setSensorZeroResult(SensorZeroResult.newBuilder()
            .setSuccess(true)
            .setFlow(100)
            .setPressure(100)
            .setVacuum(-100)
            .build());
        break;

      case READ_GAS:
        builder.setReadGasResult(ReadGasResult.newBuilder()
            .setO2(100)
            .setCo2(100)
            .setN2(100)
            .setN2O(100)
            .build());
        break;

      case SPAN_GAS:
        builder.setSpanGasResult(SpanGasResult.newBuilder()
            .setSuccess(true)
            .build());
        break;

      case READ_FLOW:
        builder.setReadFlowResult(ReadFlowResult.newBuilder()
            .setFlow(100)
            .setSuccess(true)
            .build());
        break;

      case SPAN_FLOW:
        builder.setSpanFlowResult(
            NSTProtos.Command.SpanFlowResult.newBuilder().setSuccess(true).build());
        break;

      case READ_PRESSURE:
        builder.setReadPressureResult(NSTProtos.Command.ReadPressureResult.newBuilder()
            .setPressure(100)
            .setSuccess(true)
            .build());
        break;

      case SPAN_PRESSURE:
        builder.setSpanPressureResult(SpanPressureResult.newBuilder().setSuccess(true).build());
        break;

      case READ_VACUUM:
        builder.setReadVacuumResult(
            ReadVacuumResult.newBuilder().setVacuum(100).setSuccess(true).build());
        break;
      case SPAN_VACUUM:
        builder.setSpanVacuumResult(SpanVacuumResult.newBuilder().setSuccess(true).build());
        break;
      case RESET:
        builder.setResetResult(ResetResult.newBuilder().setSuccess(true).build());
        break;
      case CALIBRATION_DATE:
        builder.setCalibrationDateResult(
            CalibrationDateResult.newBuilder().setSuccess(true).build());
        break;
      case ANALYZER_INFO:
        builder.setAnalyzerInfoResult(AnalyzerInfoResult.newBuilder()
            .setCalibrationDate("Today")
            .setFirmwareVersion("1.0")
            .setSuccess(true)
            .build());
        break;
      case VAC_PRESSURE_TEST:
        builder.setVacPressureResult(
            VACPressureResult.newBuilder().setTestId(1).setVacPressure(100).build());
        break;
      case VAC_FLOW_TEST:
        builder.setVacFlowResult(VACFlowResult.newBuilder().setTestId(1).setVacFlow(100).build());
        break;
      case BATTERY_INFO:
        builder.setBatteryInfoResult(
            BatteryInfoResult.newBuilder().setVoltage(100).setCapacity(100).build());
        break;

      case CANCEL:
        builder.setCancelResult(NSTProtos.Command.CancelResult.newBuilder()
            .setSuccess(true)
            .build());
        break;
    }

    NSTProtos.Command command = builder.build();
    mConversationArrayAdapter.add(
        "SEND " + command.getTypeOneofCase().toString() + " for testID: " + testId);
    Log.i("MainActivity", "Sending: " + command.getTypeOneofCase().toString());
    Log.i("MainActivity", "Command: " + command);
    bluetoothService.write(command);
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case REQUEST_ENABLE_BT:
        // When the request to enable Bluetooth returns
        if (resultCode == Activity.RESULT_OK) {
          // Bluetooth is now enabled, so set up a chat session
          setupChat();
        } else {
          // User did not enable Bluetooth or an error occurred
          Log.d(TAG, "BT not enabled");
          Toast.makeText(this, "bluetooth required",
              Toast.LENGTH_SHORT).show();
          finish();
        }
    }
  }

  /**
   * The Handler that gets information back from the BluetoothChatService
   */
  @SuppressLint("HandlerLeak")
  private final Handler mHandler = new Handler() {
    @SuppressLint("HandlerLeak") @Override
    public void handleMessage(Message msg) {
      AppCompatActivity activity = MainActivity.this;
      switch (msg.what) {
        case Constants.MESSAGE_STATE_CHANGE:
          switch (msg.arg1) {
            case BluetoothService.STATE_CONNECTED:
              mConversationArrayAdapter.add("connected");
              break;
            case BluetoothService.STATE_CONNECTING:
              mConversationArrayAdapter.add("connecting...");
              break;
            case BluetoothService.STATE_LISTEN:
              mConversationArrayAdapter.add("waiting for connection...");
              break;
            case BluetoothService.STATE_NONE:
              mConversationArrayAdapter.add("connection lost.");
              break;
          }
          break;
        case Constants.MESSAGE_WRITE:
          byte[] writeBuf = (byte[]) msg.obj;
          // construct a string from the buffer
          String writeMessage = new String(writeBuf);
          mConversationArrayAdapter.add(writeMessage);
          break;
        case Constants.MESSAGE_READ:
          NSTProtos.Command command = (NSTProtos.Command) msg.obj;

          switch (command.getTypeOneofCase()) {
            case FLOW_TEST:
              testId = command.getFlowTest().getTestId();
              break;
            case PRESSURE_DROP_TEST:
              testId = command.getPressureDropTest().getTestId();
              break;
            case CONCENTRATION_TEST:
              testId = command.getConcentrationTest().getTestId();
              break;
            case EXPECTED_GAS_TEST:
              testId = command.getExpectedGasTest().getTestId();
              break;
            case DETECTED_GAS_TEST:
              testId = command.getDetectedGasTest().getTestId();
              break;
            case TRANSIENT_FLOW_TEST:
              testId = command.getTransientFlowTest().getTestId();
              break;
            case STATIC_PRESSURE_TEST:
              testId = command.getStaticPressureTest().getTestId();
              break;
          }
          // construct a string from the valid bytes in the buffer
          mConversationArrayAdapter.add(
              "RECEIVE " + command.getTypeOneofCase().toString() + " for testID: " + testId);

          break;
        case Constants.MESSAGE_DEVICE_NAME:
          // save the connected device's name
          mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
          if (null != activity) {
            Toast.makeText(activity, "Connected to "
                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
          }
          break;
        case Constants.MESSAGE_TOAST:
          if (null != activity) {
            Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                Toast.LENGTH_SHORT).show();
          }
          break;
      }
    }
  };
}
