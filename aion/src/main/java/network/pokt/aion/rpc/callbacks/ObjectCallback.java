package network.pokt.aion.rpc.callbacks;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import network.pokt.pocketsdk.models.Query;

public class ObjectCallback extends BaseCallback<JSONObject> {

    public ObjectCallback(@NotNull Query request, RPCCallback<JSONObject> rpcCallback) {
        super(request, rpcCallback);
    }

    @Override
    public void onResponse(Query query, Exception exception) {
        if (executeErrorCallback(query, exception)) {
            return;
        }

        Object queryResult = query.getResult();
        if (queryResult instanceof JSONObject) {
            this.rpcCallback.onResult((JSONObject) queryResult, null);
        } else {
            this.rpcCallback.onResult(null, new Exception("Invalid result format"));
        }
    }
}
