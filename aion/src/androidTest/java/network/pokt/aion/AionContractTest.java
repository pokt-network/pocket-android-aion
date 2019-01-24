package network.pokt.aion;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import network.pokt.aion.exceptions.AionContractException;
import network.pokt.aion.rpc.callbacks.RPCCallback;
import network.pokt.aion.util.HexStringUtil;
import network.pokt.aion.util.RawFileUtil;
import network.pokt.aion.util.SemaphoreUtil;
import network.pokt.aion.util.TestConfiguration;
import network.pokt.pocketsdk.exceptions.InvalidConfigurationException;
import network.pokt.pocketsdk.models.Wallet;
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

    final String MASTERY_SUBNETWORK = "32";
    final String pocketTestContractAddress = "0xA0707404B9BE7a5F630fCed3763d28FA5C988964fDC25Aa621161657a7Bf4b89";
    String testAccountAddress = "0xa05b88ac239f20ba0a4d2f0edac8c44293e9b36fa937fb55bf7a1cd61a60f036";
    String testAccountPK = "0x2b5d6fd899ccc148b5f85b4ea20961678c04d70055b09dac7857ea430757e6badb4cfe129e670e2fef1b632ed0eab9572954feebbea9cb32134b284763acd34e";

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("network.pokt.aion.test", appContext.getPackageName());
    }

    @Test
    public void testConstantFunctionCall() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreUtil.SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    // Get the contract instance
                    // To get an instance that connects to the AION Pocket Node uncomment the following line
                    AionContract contract = getContractInstance(R.raw.pocket_test_abi, null);

                    // To get an instance that connects to the mock server uncomment the following line
                    //AionContract contract = getContractInstance(R.raw.pocket_test_abi, new SimpleContractMockDispatcher());

                    // Prepare parameters
                    List<Object> functionParams = new ArrayList<>();
                    functionParams.add(new BigInteger("2"));
                    functionParams.add(new BigInteger("10"));

                    // Execute function and assert on response
                    contract.executeConstantFunction("multiply", null, functionParams, null, null, null, new RPCCallback<Object>() {

                        @Override
                        public void onResult(Object result, Exception exception) {
                            // Since we know from JSON ABI that the return value is a uint128 we can check if it's type BigInteger
                            assertNull(exception);
                            assertNotNull(result);
                            String hexResult = (String)result;
                            assertEquals(new BigInteger(HexStringUtil.removeLeadingZeroX(hexResult), 16), new BigInteger("20"));
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
    public void testMultipleReturnsConstantFunction() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreUtil.SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    // Get the contract instance
                    // To get an instance that connects to the AION Pocket Node uncomment the following line
                    AionContract contract = getContractInstance(R.raw.pocket_test_abi, null);

                    // To get an instance that connects to the mock server uncomment the following line
                    //AionContract contract = getContractInstance(R.raw.pocket_test_abi, new SimpleContractMockDispatcher());

                    List<Object> functionParams = new ArrayList<>();
                    functionParams.add(new BigInteger("100"));
                    functionParams.add(new Boolean(true));
                    functionParams.add(pocketTestContractAddress);
                    functionParams.add("Hello World!");
                    functionParams.add(pocketTestContractAddress);

                    contract.executeConstantFunction("echo", null, functionParams, null, null, null, new RPCCallback<Object>() {

                        @Override
                        public void onResult(Object result, Exception exception) {
                            assertNull(exception);
                            assertNotNull(result);
                            semaphore.release();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void testFunctionCall() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreUtil.SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    // Get the contract instance
                    // To get an instance that connects to the AION Pocket Node uncomment the following line
                    final AionContract contract = getContractInstance(R.raw.pocket_test_abi, null);

                    // To get an instance that connects to the mock server uncomment the following line
                    //AionContract contract = getContractInstance(R.raw.pocket_test_abi, new SimpleContractMockDispatcher());

                    contract.getPocketAion().eth.getTransactionCount(testAccountAddress, null, MASTERY_SUBNETWORK, new RPCCallback<BigInteger>() {
                        @Override
                        public void onResult(BigInteger result, Exception exception) {
                            assertNull(exception);
                            assertNotNull(result);

                            List<Object> functionParams = new ArrayList<>();
                            functionParams.add(new BigInteger("1"));

                            try {
                                Wallet wallet = contract.getPocketAion().importWallet(testAccountPK, MASTERY_SUBNETWORK, testAccountAddress, null);
                                contract.executeFunction("addToState", wallet, functionParams, result, null, null, null, new RPCCallback<String>() {
                                    @Override
                                    public void onResult(String result, Exception exception) {
                                        assertNotNull(result);
                                        assertNull(exception);
                                        semaphore.release();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                semaphore.release();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    semaphore.release();
                }
            }
        });
    }


    // Private helpers
    private AionContract getContractInstance(int abiInterfaceJSON, Dispatcher mockDispatcher) throws InvalidConfigurationException, JSONException, AionContractException, MalformedURLException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        TestConfiguration testConfiguration;
        if (mockDispatcher == null) {
            testConfiguration = new TestConfiguration(new URL("https://aion.pokt.network"));
        } else {
            testConfiguration = new TestConfiguration(mockDispatcher);
        }
        PocketAion pocketAion = new PocketAion(testConfiguration, appContext);
        JSONArray abiInterface = new JSONArray(RawFileUtil.readRawTextFile(InstrumentationRegistry.getTargetContext(), abiInterfaceJSON));
        return new AionContract(pocketAion, abiInterface, pocketTestContractAddress, MASTERY_SUBNETWORK);
    }

}
