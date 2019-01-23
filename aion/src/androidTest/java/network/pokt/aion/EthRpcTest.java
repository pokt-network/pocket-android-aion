package network.pokt.aion;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import network.pokt.aion.rpc.callbacks.RPCCallback;
import network.pokt.aion.rpc.types.BlockTag;
import network.pokt.aion.rpc.types.ObjectOrBoolean;
import network.pokt.aion.util.SemaphoreUtil;
import network.pokt.aion.util.SemaphoreUtil.SemaphoreCallback;
import network.pokt.aion.util.TestConfiguration;
import network.pokt.pocketsdk.exceptions.CreateQueryException;
import network.pokt.pocketsdk.exceptions.CreateTransactionException;
import network.pokt.pocketsdk.exceptions.ImportWalletException;
import network.pokt.pocketsdk.exceptions.InvalidConfigurationException;
import network.pokt.pocketsdk.models.Wallet;

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
    static final String MASTERY_SUBNETWORK = "32";
    String testAccountAddress = "0xa05b88ac239f20ba0a4d2f0edac8c44293e9b36fa937fb55bf7a1cd61a60f036";
    String testTxHashHex = "0xab24681fc474b4a6cbc9489a7595634abbfcd1ef205e1807df53fc70619496bd";
    String blockHashHex = "0x1ab636692ebfaf9a181d4671e0f1f3d3bc8bd9a9ec91c8a19dcbcc06a9975390";
    BigInteger blockNumber = new BigInteger("1565463");
    String pocketTestContractAddress = "0xA0707404B9BE7a5F630fCed3763d28FA5C988964fDC25Aa621161657a7Bf4b89";
    String storageContractAddress = "0xa061d41a9de8b2f317073cc331e616276c7fc37a80b0e05a7d0774c9cf956107";
    String testAccountPK = "0x2b5d6fd899ccc148b5f85b4ea20961678c04d70055b09dac7857ea430757e6badb4cfe129e670e2fef1b632ed0eab9572954feebbea9cb32134b284763acd34e";
    String secondaryTestAddress = "0xa07743f4170ded07da3ccd2ad926f9e684a5f61e90d018a3c5d8ea60a8b3406a";

    // To use the mock tests, uncomment this constructor
//    public EthRpcTest() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//
//        try {
//            pocketAion = new PocketAion(new TestConfiguration(new RpcMockDispatcher(appContext, 4)), appContext);
//            assertNotNull(pocketAion);
//        } catch (InvalidConfigurationException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException murle) {
//            murle.printStackTrace();
//        }
//    }

    // To use the mastery server uncomment this constructor
    public EthRpcTest() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        try {
            pocketAion = new PocketAion(new TestConfiguration(new URL("https://aion.pokt.network")), appContext);
            assertNotNull(pocketAion);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (MalformedURLException murle) {
            murle.printStackTrace();
        }
    }

    @Test
    public void protocolVersion() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.protocolVersion(MASTERY_SUBNETWORK, new RPCCallback<String>() {
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
    public void syncing() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.eth.syncing(MASTERY_SUBNETWORK, new RPCCallback<ObjectOrBoolean>() {
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
                    pocketAion.eth.nrgPrice(MASTERY_SUBNETWORK, new RPCCallback<BigInteger>() {
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
                    pocketAion.eth.blockNumber(MASTERY_SUBNETWORK, new RPCCallback<BigInteger>() {
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
                    pocketAion.eth.getBalance(testAccountAddress, null, MASTERY_SUBNETWORK, new RPCCallback<BigInteger>() {
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
                    pocketAion.eth.getStorageAt(storageContractAddress, new BigInteger("0"), null, MASTERY_SUBNETWORK, new RPCCallback<String>() {
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
                    pocketAion.eth.getTransactionCount(testAccountAddress, null, MASTERY_SUBNETWORK, new RPCCallback<BigInteger>() {
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
                    pocketAion.eth.getBlockTransactionCountByHash(blockHashHex, MASTERY_SUBNETWORK, new RPCCallback<BigInteger>() {
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
                    pocketAion.eth.getBlockTransactionCountByNumber(new BlockTag(blockNumber), MASTERY_SUBNETWORK, new RPCCallback<BigInteger>() {
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
                    pocketAion.eth.getCode(pocketTestContractAddress,null, MASTERY_SUBNETWORK, new RPCCallback<String>() {
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
                    // Test data for calling into the multiply function of the PocketTest contract deployed to mastery

                    pocketAion.eth.call(MASTERY_SUBNETWORK, pocketTestContractAddress, null, null, null, null, null, "0xbbaa0820000000000000000000000000000000020000000000000000000000000000000a", new RPCCallback<String>() {
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
                    pocketAion.eth.estimateGas(MASTERY_SUBNETWORK, pocketTestContractAddress, null, null, null, null, null, "0xbbaa0820000000000000000000000000000000020000000000000000000000000000000a", new RPCCallback<BigInteger>() {
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
                    pocketAion.eth.getBlockByHash(MASTERY_SUBNETWORK, blockHashHex, false, new RPCCallback<JSONObject>() {
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
                    pocketAion.eth.getBlockByNumber(MASTERY_SUBNETWORK, null, false, new RPCCallback<JSONObject>() {
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
                    pocketAion.eth.getTransactionByHash(MASTERY_SUBNETWORK, testTxHashHex, new RPCCallback<JSONObject>() {
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
                    pocketAion.eth.getTransactionByBlockHashAndIndex(MASTERY_SUBNETWORK, "0x1ab636692ebfaf9a181d4671e0f1f3d3bc8bd9a9ec91c8a19dcbcc06a9975390", new BigInteger("0"), new RPCCallback<JSONObject>() {
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
                    pocketAion.eth.getTransactionByBlockNumberAndIndex(MASTERY_SUBNETWORK, new BlockTag(new BigInteger("1565463")), new BigInteger("0"), new RPCCallback<JSONObject>() {
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
                    pocketAion.eth.getTransactionReceipt(MASTERY_SUBNETWORK, testTxHashHex, new RPCCallback<JSONObject>() {
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
                    pocketAion.eth.getLogs(MASTERY_SUBNETWORK, null, null, new ArrayList<String>(), new ArrayList<String>(), blockHashHex, new RPCCallback<JSONArray>() {
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
    public void sendTransaction() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreUtil.SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                final Wallet wallet;
                try {
                    wallet = pocketAion.importWallet(testAccountPK, MASTERY_SUBNETWORK, testAccountAddress, null);

                    pocketAion.eth.getTransactionCount(wallet.getAddress(), null, MASTERY_SUBNETWORK, new RPCCallback<BigInteger>() {
                        @Override
                        public void onResult(BigInteger result, Exception exception) {
                            assertNotNull(result);
                            assertNull(exception);

                            try {
                                pocketAion.eth.sendTransaction(MASTERY_SUBNETWORK, wallet, secondaryTestAddress, new BigInteger("21000"), new BigInteger("10000000000"), new BigInteger("1000000000"), null, result, new RPCCallback<String>() {
                                    @Override
                                    public void onResult(String result, Exception exception) {
                                        // Result is the transaction hash
                                        assertNotNull(result);
                                        assertNull(exception);
                                        semaphore.release();
                                    }
                                });
                            } catch (CreateTransactionException cte) {
                                cte.printStackTrace();
                            }
                        }
                    });

                } catch (ImportWalletException e) {
                    e.printStackTrace();
                    semaphore.release();
                } catch (CreateQueryException cqe) {
                    cqe.printStackTrace();
                    semaphore.release();
                }

            }
        });
    }
}
