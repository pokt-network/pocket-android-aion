package network.pokt.aion.rpc;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.pokt.aion.PocketAion;
import network.pokt.aion.rpc.callbacks.BigIntegerCallback;
import network.pokt.aion.rpc.callbacks.BooleanCallback;
import network.pokt.aion.rpc.callbacks.RPCCallback;
import network.pokt.aion.rpc.callbacks.StringCallback;
import network.pokt.pocketsdk.exceptions.CreateQueryException;
import network.pokt.pocketsdk.models.Query;

public class NetRpc {

    private final PocketAion pocketAion;
    private enum NetRpcMethod {
        net_version,
        net_listening,
        net_peerCount
    }

    // Public interface
    public NetRpc(PocketAion pocketAion) {
        this.pocketAion = pocketAion;
    }

    public void version(@NotNull String subnetwork, @NotNull final RPCCallback<String> callback) throws CreateQueryException {
        Query query = this.createQuery(subnetwork, NetRpcMethod.net_version, new ArrayList());
        this.pocketAion.executeQueryAsync(new StringCallback(query, callback));
    }

    public void listening(@NotNull String subnetwork, @NotNull final RPCCallback<Boolean> callback) throws CreateQueryException {
        Query query = this.createQuery(subnetwork, NetRpcMethod.net_listening, new ArrayList());
        this.pocketAion.executeQueryAsync(new BooleanCallback(query, callback));
    }

    public void peerCount(@NotNull String subnetwork, @NotNull final RPCCallback<BigInteger> callback) throws CreateQueryException {
        Query query = this.createQuery(subnetwork, NetRpcMethod.net_peerCount, new ArrayList());
        this.pocketAion.executeQueryAsync(new BigIntegerCallback(query, callback));
    }

    // Private interface
    private Query createQuery(String subnetwork, NetRpc.NetRpcMethod rpcMethod, List rpcParams) throws CreateQueryException {
        Map<String, Object> params = new HashMap<>();
        params.put("rpcMethod", rpcMethod.name());
        params.put("rpcParams", rpcParams);
        return pocketAion.createQuery(subnetwork, params, null);
    }
}
