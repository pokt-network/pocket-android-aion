package network.pokt.aion.rpc.callbacks;

import org.jetbrains.annotations.NotNull;

import network.pokt.pocketsdk.models.Query;

public class StringCallback extends BaseCallback<String> {

    public StringCallback(@NotNull Query request, RPCCallback<String> rpcCallback) {
        super(request, rpcCallback);
    }

    @Override
    public void onResponse(Query query, Exception exception) {
        if (executeErrorCallback(query, exception)) {
            return;
        }

        Object queryResult = query.getResult();
        if (queryResult instanceof String) {
            this.rpcCallback.onResult((String) queryResult, null);
        } else {
            this.rpcCallback.onResult(null, new Exception("Invalid result format"));
        }
    }
}
