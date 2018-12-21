package network.pokt.aion;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import network.pokt.aion.util.TestConfiguration;
import network.pokt.pocketsdk.exceptions.CreateTransactionException;
import network.pokt.pocketsdk.exceptions.CreateWalletException;
import network.pokt.pocketsdk.exceptions.ImportWalletException;
import network.pokt.pocketsdk.exceptions.InvalidConfigurationException;
import network.pokt.pocketsdk.models.Transaction;
import network.pokt.pocketsdk.models.Wallet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PocketAionTest {

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
            this.pocketAion = new PocketAion(new TestConfiguration(), appContext);
            assertTrue(this.pocketAion != null);
        } catch (InvalidConfigurationException e) {
            throw e;
        }
    }

    @Test
    public void testCreateWallet() throws CreateWalletException {
        try {
            Wallet wallet = this.pocketAion.createWallet("4", null);
            assert(wallet != null);
        } catch (CreateWalletException cwe) {
            throw cwe;
        }
    }

    @Test
    public void testImportWallet() throws ImportWalletException, CreateWalletException {
        try {
            Wallet wallet = this.pocketAion.createWallet("4", null);
            assert(wallet != null);
            Wallet importedWallet = this.pocketAion.importWallet(wallet.getPrivateKey(), wallet.getSubnetwork(), wallet.getAddress(), null);
            assert(importedWallet != null);
        } catch (CreateWalletException cwe) {
            throw cwe;
        } catch (ImportWalletException iwe) {
            throw iwe;
        }
    }

    @Test
    public void testCreateTransaction() throws CreateWalletException, CreateTransactionException {
        try {
            Wallet senderWallet = this.pocketAion.createWallet("4", null);
            assert(senderWallet != null);
            Wallet receiverWallet = this.pocketAion.createWallet("4", null);
            assert(receiverWallet != null);
            // Create transaction params
            Map<String, Object> txParams = new HashMap<>();
            txParams.put("nonce", "0x0");
            txParams.put("to", receiverWallet.getAddress());
            txParams.put("value", "0x989680");
            txParams.put("data", null);
            txParams.put("nrg", "0x989680");
            txParams.put("nrgPrice", "0x989680");
            Transaction transaction = this.pocketAion.createTransaction(senderWallet, "4", txParams);
            assert(transaction != null);
        } catch (CreateWalletException cwe) {
            throw cwe;
        }
    }
}
