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
import java.util.concurrent.Semaphore;

import network.pokt.aion.exceptions.AionContractException;
import network.pokt.aion.rpc.callbacks.RPCCallback;
import network.pokt.aion.util.HexStringUtil;
import network.pokt.aion.util.RawFileUtil;
import network.pokt.aion.util.SemaphoreUtil;
import network.pokt.aion.util.SimpleContractMockDispatcher;
import network.pokt.aion.util.TestConfiguration;
import network.pokt.aion.util.TypesTestMockDispatcher;
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
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreUtil.SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    // Get the contract instance
                    AionContract contract = getContractInstance(R.raw.simple_contract, new SimpleContractMockDispatcher());

                    // Prepare parameters
                    List<Object> functionParams = new ArrayList<>();
                    functionParams.add(new BigInteger("10"));

                    // Execute function and assert on response
                    contract.executeConstantFunction("multiply", null, functionParams, null, null, null, new RPCCallback<Object>() {

                        @Override
                        public void onResult(Object result, Exception exception) {
                            // Since we know from JSON ABI that the return value is a uint128 we can check if it's type BigInteger
                            // Result should be input * 7
                            // Since input was 10, result = 70
                            assertNull(exception);
                            assertNotNull(result);
                            String hexResult = (String)result;
                            assertEquals(new BigInteger(HexStringUtil.removeLeadingZeroX(hexResult), 16), new BigInteger("70"));
                            semaphore.release();
                        }
                    });
                } catch (Exception exception) {
                    exception.printStackTrace();
                    semaphore.release();
                }
            }
        });
    }

    @Test
    public void testTypeEncodingAndDecoding() throws InvalidConfigurationException, JSONException, AionContractException, CreateQueryException {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreUtil.SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    // Get the contract instance
                    AionContract contract = getContractInstance(R.raw.types_contract, new TypesTestMockDispatcher());

                    // Prepare parameters
                    List<Object> functionParams = new ArrayList<>();
                    functionParams.add(new BigInteger("10"));
                    functionParams.add(new Boolean(true));
                    functionParams.add("0xa02e5aad91bed3a1c6bbd1958cea6f0ecedd31ac8801a435913b7ada136dcdfa");
                    functionParams.add("Hello World!");
                    functionParams.add("0x6c9cc34575dc7d15afd12cf98b0cdb18cbcea8788266473eef8044095da40131");

                    // Execute function and assert on response
                    contract.executeConstantFunction("returnValues", null, functionParams, null, null, null, new RPCCallback<Object>() {

                        @Override
                        public void onResult(Object result, Exception exception) {
                            // Since we know from JSON ABI that the return value is an array, we can decode it
                            assertNull(exception);
                            assertNotNull(result);
                            semaphore.release();
                        }
                    });
                } catch (Exception exception) {
                    exception.printStackTrace();
                    semaphore.release();
                }
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
