package network.pokt.aion;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import network.pokt.aion.rpc.callbacks.RPCCallback;
import network.pokt.aion.rpc.types.ObjectOrBoolean;
import network.pokt.aion.util.RpcMockDispatcher;
import network.pokt.aion.util.SemaphoreUtil;
import network.pokt.aion.util.SemaphoreUtil.SemaphoreCallback;
import network.pokt.aion.util.TestConfiguration;
import network.pokt.pocketsdk.exceptions.CreateQueryException;
import network.pokt.pocketsdk.exceptions.InvalidConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EthRpcTest {

    PocketAion pocketAion;

    public EthRpcTest() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        try {
            pocketAion = new PocketAion(new TestConfiguration(new RpcMockDispatcher(appContext, 5)), appContext);
            assertNotNull(pocketAion);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void protocolVersion() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.protocolVersion("mastery", new RPCCallback<String>() {
                        @Override
                        public void onResult(String result, Exception exception) {
                            Logger.getGlobal().log(Level.ALL, result);
                            if (exception != null) {
                                Logger.getGlobal().log(Level.ALL, exception.getMessage());
                            }
                            assertEquals("54", result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void syncing() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.syncing("mastery", new RPCCallback<ObjectOrBoolean>() {
                        @Override
                        public void onResult(ObjectOrBoolean result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void nrgPrice() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.nrgPrice("mastery", new RPCCallback<BigInteger>() {
                        @Override
                        public void onResult(BigInteger result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void blockNumber() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.blockNumber("mastery", new RPCCallback<BigInteger>() {
                        @Override
                        public void onResult(BigInteger result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getBalance() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getBalance("0x00000", null, "mastery", new RPCCallback<BigInteger>() {
                        @Override
                        public void onResult(BigInteger result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getStorageAt() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getStorageAt("0x00000", new BigInteger("21000", 16), null, "mastery", new RPCCallback<String>() {
                        @Override
                        public void onResult(String result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getTransactionCount() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getTransactionCount("0x00000", null, "mastery", new RPCCallback<BigInteger>() {
                        @Override
                        public void onResult(BigInteger result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getBlockTransactionCountByHash() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getBlockTransactionCountByHash("0x00000", "mastery", new RPCCallback<BigInteger>() {
                        @Override
                        public void onResult(BigInteger result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getBlockTransactionCountByNumber() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getBlockTransactionCountByNumber(null, "mastery", new RPCCallback<BigInteger>() {
                        @Override
                        public void onResult(BigInteger result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getCode() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getCode("0x0",null, "mastery", new RPCCallback<String>() {
                        @Override
                        public void onResult(String result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void call() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.call("mastery", "0x0", null, "0x0", null, null, null, "0x0", new RPCCallback<String>() {
                        @Override
                        public void onResult(String result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void estimateGas() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.estimateGas("mastery", "0x0", null, "0x0", null, null, null, "0x0", new RPCCallback<BigInteger>() {
                        @Override
                        public void onResult(BigInteger result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getBlockByHash() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getBlockByHash("mastery", "0x0", false, new RPCCallback<JSONObject>() {
                        @Override
                        public void onResult(JSONObject result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getBlockByNumber() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getBlockByNumber("mastery", null, false, new RPCCallback<JSONObject>() {
                        @Override
                        public void onResult(JSONObject result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getTransactionByHash() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getTransactionByHash("mastery", "0x0", new RPCCallback<JSONObject>() {
                        @Override
                        public void onResult(JSONObject result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getTransactionByBlockHashAndIndex() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getTransactionByBlockHashAndIndex("mastery", "0x0", new BigInteger("21000", 16), new RPCCallback<JSONObject>() {
                        @Override
                        public void onResult(JSONObject result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getTransactionByBlockNumberAndIndex() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getTransactionByBlockNumberAndIndex("mastery", null, new BigInteger("21000", 16), new RPCCallback<JSONObject>() {
                        @Override
                        public void onResult(JSONObject result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getTransactionReceipt() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getTransactionReceipt("mastery", "0x0", new RPCCallback<JSONObject>() {
                        @Override
                        public void onResult(JSONObject result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getLogs() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getLogs("mastery", null, null, new ArrayList<String>(), new ArrayList<String>(), "0x0", new RPCCallback<JSONArray>() {
                        @Override
                        public void onResult(JSONArray result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void getProof() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.getProof("mastery", "0x0", new ArrayList<String>(), null, new RPCCallback<JSONObject>() {
                        @Override
                        public void onResult(JSONObject result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);
                            semaphore.release();
                        }
                    });
                } catch (CreateQueryException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
