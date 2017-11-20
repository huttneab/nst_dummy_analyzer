package mechanical_man.com.analyzerdummy;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mechanical_man.nst_proto.NSTProtos;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mechanical_man.com.analyzerdummy.util.EnumAdapter;

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
            case FLOW:
                builder.setType(NSTProtos.Command.Type.FLOW_RESULT)
                        .setFlowResult(NSTProtos.Command.FlowResult.newBuilder()
                                .setTestId(testId)
                                .setDetectedGas(NSTProtos.Command.Gas.AIR)
                                .setExpectedGas(NSTProtos.Command.Gas.AIR)
                                .setFlowPressure(55.0)
                                .setFlowRate(55.0)
                                .build());
                break;
            case CONCENTRATION:
                break;
            case PRESSURE:
                break;
        }
        NSTProtos.Command command = builder.build();
        mConversationArrayAdapter.add("SEND " + command.getType().toString() + " for testID: " + testId);
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
    private final Handler mHandler = new Handler() {
        @Override
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


                    switch (command.getType()) {
                        case FLOW_TEST:
                            testId = command.getFlowTest().getTestId();
                            break;
                        case PRESSURE_TEST:
                            break;
                        case CONCENTRATION_TEST:
                            break;

                    }
                    // construct a string from the valid bytes in the buffer
                    mConversationArrayAdapter.add("RECEIVE " + command.getType().toString() + " for testID: " + testId);

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
