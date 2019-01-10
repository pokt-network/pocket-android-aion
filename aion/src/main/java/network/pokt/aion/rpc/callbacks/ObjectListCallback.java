package network.pokt.aion.rpc.callbacks;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import network.pokt.pocketsdk.models.Query;

public class ObjectListCallback extends BaseCallback<JSONArray> {

    public ObjectListCallback(@NotNull Query request, RPCCallback<JSONArray> rpcCallback) {
        super(request, rpcCallback);
    }

    @Override
    public void onResponse(Query query, Exception exception) {
        if (executeErrorCallback(query, exception)) {
            return;
        }

        Object queryResult = query.getResult();
        if (queryResult instanceof JSONArray) {
            this.rpcCallback.onResult((JSONArray) queryResult, null);
        } else {
            this.rpcCallback.onResult(null, new Exception("Invalid result format"));
        }
    }
}
