package network.pokt.aion;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;

import network.pokt.aion.rpc.callbacks.RPCCallback;
import network.pokt.aion.util.RpcMockDispatcher;
import network.pokt.aion.util.TestConfiguration;
import network.pokt.pocketsdk.exceptions.CreateQueryException;
import network.pokt.pocketsdk.exceptions.InvalidConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class NetRpcTest {

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
            this.pocketAion = new PocketAion(new TestConfiguration(new RpcMockDispatcher(appContext, 3)), appContext);
            assertNotNull(this.pocketAion);
        } catch (InvalidConfigurationException e) {
            throw e;
        }
    }

    @Test
    public void version() {
        try {
            this.pocketAion.net.version("mastery", new RPCCallback<String>() {
                @Override
                public void onResult(String result, Exception exception) {
                    assertEquals(result, "mastery");
                    assertNull(exception);
                }
            });
        } catch (CreateQueryException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void listening() {
        try {
            this.pocketAion.net.listening("mastery", new RPCCallback<Boolean>() {
                @Override
                public void onResult(Boolean result, Exception exception) {
                    assertNotNull(result);
                    assertNull(exception);
                }
            });
        } catch (CreateQueryException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void peerCount() {
        try {
            this.pocketAion.net.peerCount("mastery", new RPCCallback<BigInteger>() {
                @Override
                public void onResult(BigInteger result, Exception exception) {
                    assertNotNull(result);
                    assertNull(exception);
                }
            });
        } catch (CreateQueryException e) {
            e.printStackTrace();
        }
    }
}
