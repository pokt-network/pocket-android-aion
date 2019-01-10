package network.pokt.aion.rpc.callbacks;

public interface RPCCallback<T> {
    void onResult(T result, Exception exception);
}
