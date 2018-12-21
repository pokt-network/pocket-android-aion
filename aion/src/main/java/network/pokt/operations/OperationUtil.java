package network.pokt.operations;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.liquidplayer.javascript.JSObject;

import network.pokt.pocketsdk.models.Wallet;

public class OperationUtil {

    public static Wallet parseWalletObj(JSObject walletObj, @NotNull String network, @NotNull String subnetwork) {
        Wallet result = null;

        // Extract the address and private key
        if(walletObj == null || walletObj.isUndefined()) {
            return result;
        }

        String address = walletObj.property("address").toString();
        String privateKey = walletObj.property("privateKey").toString();

        if (address == null || address.isEmpty() || privateKey == null || privateKey.isEmpty()) {
            return result;
        }

        // Create the wallet
        try {
            result = new Wallet(address, privateKey, network, subnetwork, new JSONObject());
        } catch (JSONException e) {
            result = null;
        }

        return result;
    }
}
