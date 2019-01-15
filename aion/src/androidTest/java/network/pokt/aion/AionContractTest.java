package network.pokt.aion;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import network.pokt.aion.exceptions.AionContractException;
import network.pokt.aion.rpc.callbacks.RPCCallback;
import network.pokt.aion.util.RawFileUtil;
import network.pokt.aion.util.RpcMockDispatcher;
import network.pokt.aion.util.TestConfiguration;
import network.pokt.pocketsdk.exceptions.CreateQueryException;
import network.pokt.pocketsdk.exceptions.InvalidConfigurationException;
import okhttp3.mockwebserver.Dispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AionContractTest {

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("network.pokt.aion.test", appContext.getPackageName());
    }

    @Test
    public void testSimpleConstantFunctionCall() throws InvalidConfigurationException, JSONException, AionContractException, CreateQueryException {
        // Get the contract instance
        AionContract contract = this.getContractInstance(R.raw.simple_contract, new RpcMockDispatcher(InstrumentationRegistry.getTargetContext(), 4));

        // Prepare parameters
        List<Object> functionParams = new ArrayList<>();
        functionParams.add(new BigInteger("10"));

        // Execute function and assert on response
        contract.executeConstantFunction("multiply", null, functionParams, null, null, null, new RPCCallback<Object>() {

            @Override
            public void onResult(Object result, Exception exception) {
                // Since we know from JSON ABI that the return value is a uint128 we can check if it's type BigInteger
                System.out.println("MULTIPLY RAN");
                assertNull(exception);
                assertNotNull(result);
            }
        });
    }


    // Private helpers
    private AionContract getContractInstance(int abiInterfaceJSON, Dispatcher mockDispatcher) throws InvalidConfigurationException, JSONException, AionContractException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        PocketAion pocketAion = new PocketAion(new TestConfiguration(mockDispatcher), appContext);
        JSONArray abiInterface = new JSONArray(RawFileUtil.readRawTextFile(InstrumentationRegistry.getTargetContext(), abiInterfaceJSON));
        return new AionContract(pocketAion, abiInterface, "0x0", "mastery");
    }

}
