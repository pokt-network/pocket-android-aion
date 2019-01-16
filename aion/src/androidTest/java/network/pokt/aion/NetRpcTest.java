package network.pokt.aion;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;
import java.util.concurrent.Semaphore;

import network.pokt.aion.rpc.callbacks.RPCCallback;
import network.pokt.aion.util.RpcMockDispatcher;
import network.pokt.aion.util.SemaphoreUtil;
import network.pokt.aion.util.TestConfiguration;
import network.pokt.pocketsdk.exceptions.CreateQueryException;
import network.pokt.pocketsdk.exceptions.InvalidConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class NetRpcTest {

    PocketAion pocketAion;

    public NetRpcTest() {
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
    public void version() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreUtil.SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.net.version("mastery", new RPCCallback<String>() {
                        @Override
                        public void onResult(String result, Exception exception) {
                            assertEquals(result, "mastery");
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
    public void listening() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreUtil.SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.net.listening("mastery", new RPCCallback<Boolean>() {
                        @Override
                        public void onResult(Boolean result, Exception exception) {
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
    public void peerCount() {
        SemaphoreUtil.executeSemaphoreCallback(new SemaphoreUtil.SemaphoreCallback() {
            @Override
            public void execute(final Semaphore semaphore) {
                try {
                    pocketAion.net.peerCount("mastery", new RPCCallback<BigInteger>() {
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
}
