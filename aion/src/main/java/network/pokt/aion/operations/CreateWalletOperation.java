package network.pokt.aion.operations;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.liquidplayer.javascript.JSContext;
import org.liquidplayer.javascript.JSException;
import org.liquidplayer.javascript.JSObject;

import network.pokt.aion.R;
import network.pokt.aion.util.RawFileUtil;
import network.pokt.pocketsdk.models.Wallet;

public class CreateWalletOperation extends BaseOperation {

    private String network;
    private String subnetwork;
    private Wallet wallet;

    CreateWalletOperation(Context context) {
        super(context);
    }

    public CreateWalletOperation(Context context, @NotNull String network, @NotNull String subnetwork) {
        this(context);
        this.network = network;
        this.subnetwork = subnetwork;
    }

    @Override
    void executeOperation(JSContext jsContext) {
        // Run the script to create the wallet in JS
        jsContext.evaluateScript(RawFileUtil.readRawTextFile(this.context, R.raw.create_wallet));
        JSObject walletObj = jsContext.property("wallet").toObject();
        this.wallet = OperationUtil.parseWalletObj(walletObj, this.network, this.subnetwork);
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    @Override
    public void handle(JSException exception) {
        super.handle(exception);
        this.wallet = null;
    }
}
