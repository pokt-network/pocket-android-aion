package network.pokt.aion.rpc.callbacks;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import network.pokt.aion.rpc.types.ObjectOrBoolean;
import network.pokt.pocketsdk.models.Query;

public class ObjectOrBooleanCallback extends BaseCallback<ObjectOrBoolean> {

    public ObjectOrBooleanCallback(@NotNull Query request, RPCCallback<ObjectOrBoolean> rpcCallback) {
        super(request, rpcCallback);
    }

    @Override
    public void onResponse(Query query, Exception exception) {
        if (executeErrorCallback(query, exception)) {
            return;
        }

        Object queryResult = query.getResult();
        if (queryResult instanceof JSONObject) {
            this.rpcCallback.onResult(new ObjectOrBoolean((JSONObject) queryResult), null);
        } else if(queryResult instanceof Boolean) {
            this.rpcCallback.onResult(new ObjectOrBoolean((Boolean) queryResult), null);
        } else {
            this.rpcCallback.onResult(null, new Exception("Invalid result format"));
        }
    }
}
