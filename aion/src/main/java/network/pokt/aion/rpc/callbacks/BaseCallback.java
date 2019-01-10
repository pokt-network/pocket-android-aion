package network.pokt.aion.rpc.callbacks;

import org.jetbrains.annotations.NotNull;

import network.pokt.pocketsdk.interfaces.SendRequestCallback;
import network.pokt.pocketsdk.models.Query;

public abstract class BaseCallback<T> extends SendRequestCallback<Query> {

    RPCCallback<T> rpcCallback;

    BaseCallback(@NotNull Query request) {
        super(request);
    }

    public BaseCallback(@NotNull Query request, RPCCallback<T> rpcCallback) {
        super(request);
        this.rpcCallback = rpcCallback;
    }

    boolean executeErrorCallback(Query query, Exception exception) {
        if (exception != null) {
            this.rpcCallback.onResult(null, exception);
            return true;
        }

        if (query.isError()) {
            this.rpcCallback.onResult(null, new Exception(query.getErrorMsg()));
            return true;
        }
        return false;
    }
}
