package network.pokt.aion.operations;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.liquidplayer.javascript.JSContext;
import org.liquidplayer.javascript.JSException;
import org.liquidplayer.javascript.JSFunction;
import org.liquidplayer.javascript.JSObject;

import network.pokt.aion.R;
import network.pokt.pocketsdk.models.Transaction;
import network.pokt.pocketsdk.models.Wallet;

public class CreateTransactionOperation extends BaseOperation {

    private Transaction transaction;
    private Wallet wallet;
    private String nonce;
    private String to;
    private String value;
    private String data;
    private String nrg;
    private String nrgPrice;

    CreateTransactionOperation(Context context) {
        super(context);
    }

    public CreateTransactionOperation(Context context, @NotNull Wallet wallet, @NotNull String nonce, @NotNull String to, String value, String data, @NotNull String nrg, @NotNull String nrgPrice) {
        this(context);
        this.wallet = wallet;
        this.nonce = nonce;
        this.to = to;
        this.value = value;
        this.data = data == null ? "" : data;
        this.nrg = nrg;
        this.nrgPrice = nrgPrice;
    }

    @Override
    void executeOperation(JSContext jsContext) {
        // Parse input parameters for transaction signature
        String privateKey = this.wallet.getPrivateKey();
        String jsCode = String.format(this.readRawTextFile(R.raw.create_transaction),
                this.nonce, this.to, this.value, this.data, this.nrg, this.nrgPrice, privateKey);

        // Run code
        jsContext.evaluateScript(jsCode);

        // Get promise and set callback
        JSObject promise = jsContext.property("txPromise").toObject();
        promise.property("then").toFunction().call(promise, this.getTransactionSignatureCallback(jsContext, wallet));
    }

    private JSFunction getTransactionSignatureCallback(JSContext jsContext, final Wallet wallet) {
        return new JSFunction(jsContext, "then") {
            public void then(JSObject result) {
                CreateTransactionOperation opInstance = CreateTransactionOperation.this;
                // Create the new Transaction
                if(result == null) {
                    opInstance.transaction = null;
                    return;
                }

                try {
                    String rawTransaction = result.property("rawTransaction").toString();
                    opInstance.transaction = new Transaction(wallet.getNetwork(), wallet.getSubnetwork(), rawTransaction, new JSONObject());
                } catch (JSONException e) {
                    opInstance.transaction = null;
                    opInstance.errorMsg = e.getMessage();
                }
            }
        };
    }

    @Override
    public void handle(JSException exception) {
        this.errorMsg = exception.getMessage();
        this.transaction = null;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }
}
