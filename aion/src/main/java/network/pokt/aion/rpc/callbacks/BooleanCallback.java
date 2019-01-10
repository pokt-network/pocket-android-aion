package network.pokt.aion.rpc.callbacks;

import org.jetbrains.annotations.NotNull;

import network.pokt.pocketsdk.models.Query;

public class BooleanCallback extends BaseCallback<Boolean> {
    public BooleanCallback(@NotNull Query request, RPCCallback<Boolean> rpcCallback) {
        super(request, rpcCallback);
    }

    @Override
    public void onResponse(Query response, Exception exception) {
        if (executeErrorCallback(response, exception)) {
            return;
        }

        Object queryResult = response.getResult();
        if (queryResult instanceof Boolean) {
            this.rpcCallback.onResult((Boolean) queryResult, null);
        } else {
            this.rpcCallback.onResult(null, new Exception("Invalid result format"));
        }
    }
}
