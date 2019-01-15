package network.pokt.aion.rpc.callbacks;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import network.pokt.aion.util.HexStringUtil;
import network.pokt.pocketsdk.models.Query;

public class BigIntegerCallback extends BaseCallback<BigInteger> {

    public BigIntegerCallback(@NotNull Query request, RPCCallback<BigInteger> rpcCallback) {
        super(request, rpcCallback);
    }

    @Override
    public void onResponse(Query query, Exception exception) {
        if (executeErrorCallback(query, exception)) {
            return;
        }

        Object queryResult = query.getResult();
        if (queryResult instanceof String) {
            this.rpcCallback.onResult(new BigInteger(HexStringUtil.removeLeadingZeroX((String) queryResult), 16), null);
        } else {
            this.rpcCallback.onResult(null, new Exception("Invalid result format"));
        }
    }
}
