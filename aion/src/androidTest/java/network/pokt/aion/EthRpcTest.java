package network.pokt.aion;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import network.pokt.aion.rpc.callbacks.RPCCallback;
import network.pokt.aion.util.RpcMockDispatcher;
import network.pokt.aion.util.TestConfiguration;
import network.pokt.pocketsdk.exceptions.CreateQueryException;
import network.pokt.pocketsdk.exceptions.InvalidConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EthRpcTest {

    PocketAion pocketAion;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("network.pokt.aion.test", appContext.getPackageName());
    }


    @Before
    @Test
    public void testPocketAionInitialization() throws InvalidConfigurationException {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        try {
            this.pocketAion = new PocketAion(new TestConfiguration(new RpcMockDispatcher(appContext, 4)), appContext);
            assertNotNull(this.pocketAion);
        } catch (InvalidConfigurationException e) {
            throw e;
        }
    }

    @Test
    public void protocolVersion() {
        try {
            this.pocketAion.eth.protocolVersion("mastery", new RPCCallback<String>() {
                @Override
                public void onResult(String result, Exception exception) {
                    System.out.println(result);
                    System.out.println(exception.getMessage());
                    //assertEquals("54", result);
                    //assertNull(exception);
                }
            });
        } catch (CreateQueryException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void syncing() {
//        try {
//            this.pocketAion.eth.syncing("mastery", new RPCCallback<ObjectOrBoolean>() {
//                @Override
//                public void onResult(ObjectOrBoolean result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }

//    @Test
//    public void nrgPrice() {
//        try {
//            this.pocketAion.eth.nrgPrice("mastery", new RPCCallback<BigInteger>() {
//                @Override
//                public void onResult(BigInteger result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void blockNumber() {
//        try {
//            this.pocketAion.eth.blockNumber("mastery", new RPCCallback<BigInteger>() {
//                @Override
//                public void onResult(BigInteger result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getBalance() {
//        try {
//            this.pocketAion.eth.getBalance("0x00000", null, "mastery", new RPCCallback<BigInteger>() {
//                @Override
//                public void onResult(BigInteger result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getStorageAt() {
//        try {
//            this.pocketAion.eth.getStorageAt("0x00000", new BigInteger("21000", 16), null, "mastery", new RPCCallback<String>() {
//                @Override
//                public void onResult(String result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getTransactionCount() {
//        try {
//            this.pocketAion.eth.getTransactionCount("0x00000", null, "mastery", new RPCCallback<BigInteger>() {
//                @Override
//                public void onResult(BigInteger result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getBlockTransactionCountByHash() {
//        try {
//            this.pocketAion.eth.getBlockTransactionCountByHash("0x00000", "mastery", new RPCCallback<BigInteger>() {
//                @Override
//                public void onResult(BigInteger result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getBlockTransactionCountByNumber() {
//        try {
//            this.pocketAion.eth.getBlockTransactionCountByNumber(null, "mastery", new RPCCallback<BigInteger>() {
//                @Override
//                public void onResult(BigInteger result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getCode() {
//        try {
//            this.pocketAion.eth.getCode("0x0",null, "mastery", new RPCCallback<String>() {
//                @Override
//                public void onResult(String result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void call() {
//        try {
//            this.pocketAion.eth.call("mastery", "0x0", null, "0x0", null, null, null, "0x0", new RPCCallback<JSONObject>() {
//                @Override
//                public void onResult(JSONObject result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void estimateGas() {
//        try {
//            this.pocketAion.eth.estimateGas("mastery", "0x0", null, "0x0", null, null, null, "0x0", new RPCCallback<BigInteger>() {
//                @Override
//                public void onResult(BigInteger result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getBlockByHash() {
//        try {
//            this.pocketAion.eth.getBlockByHash("mastery", "0x0", false, new RPCCallback<JSONObject>() {
//                @Override
//                public void onResult(JSONObject result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getBlockByNumber() {
//        try {
//            this.pocketAion.eth.getBlockByNumber("mastery", null, false, new RPCCallback<JSONObject>() {
//                @Override
//                public void onResult(JSONObject result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getTransactionByHash() {
//        try {
//            this.pocketAion.eth.getTransactionByHash("mastery", "0x0", new RPCCallback<JSONObject>() {
//                @Override
//                public void onResult(JSONObject result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getTransactionByBlockHashAndIndex() {
//        try {
//            this.pocketAion.eth.getTransactionByBlockHashAndIndex("mastery", "0x0", new BigInteger("21000", 16), new RPCCallback<JSONObject>() {
//                @Override
//                public void onResult(JSONObject result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getTransactionByBlockNumberAndIndex() {
//        try {
//            this.pocketAion.eth.getTransactionByBlockNumberAndIndex("mastery", null, new BigInteger("21000", 16), new RPCCallback<JSONObject>() {
//                @Override
//                public void onResult(JSONObject result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getTransactionReceipt() {
//        try {
//            this.pocketAion.eth.getTransactionReceipt("mastery", "0x0", new RPCCallback<JSONObject>() {
//                @Override
//                public void onResult(JSONObject result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getLogs() {
//        try {
//            this.pocketAion.eth.getLogs("mastery", null, null, new ArrayList<String>(), new ArrayList<String>(), "0x0", new RPCCallback<JSONArray>() {
//                @Override
//                public void onResult(JSONArray result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getProof() {
//        try {
//            this.pocketAion.eth.getProof("mastery", "0x0", new ArrayList<String>(), null, new RPCCallback<JSONObject>() {
//                @Override
//                public void onResult(JSONObject result, Exception exception) {
//                    assertNotNull(result);
//                    assertNull(exception);
//                }
//            });
//        } catch (CreateQueryException e) {
//            e.printStackTrace();
//        }
//    }
}
