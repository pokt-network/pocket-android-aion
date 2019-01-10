package network.pokt.aion.rpc.callbacks;

import org.jetbrains.annotations.NotNull;

import network.pokt.pocketsdk.interfaces.SendRequestCallback;
import network.pokt.pocketsdk.models.Transaction;

public class TransactionHashCallback extends SendRequestCallback<Transaction> {

    RPCCallback<String> rpcCallback;

    TransactionHashCallback(@NotNull Transaction request) {
        super(request);
    }

    public TransactionHashCallback(@NotNull Transaction request, @NotNull RPCCallback<String> rpcCallback) {
        super(request);
        this.rpcCallback = rpcCallback;
    }

    @Override
    public void onResponse(Transaction transaction, Exception exception) {
        if (exception != null) {
            this.rpcCallback.onResult(null, exception);
            return;
        }

        if (transaction.isError()) {
            this.rpcCallback.onResult(null, new Exception(transaction.getErrorMsg()));
            return;
        }

        this.rpcCallback.onResult(transaction.getHash(), null);
    }
}
