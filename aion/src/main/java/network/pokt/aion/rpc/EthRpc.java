package network.pokt.aion.rpc;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.pokt.aion.PocketAion;
import network.pokt.aion.rpc.callbacks.BigIntegerCallback;
import network.pokt.aion.rpc.callbacks.ObjectCallback;
import network.pokt.aion.rpc.callbacks.ObjectListCallback;
import network.pokt.aion.rpc.callbacks.ObjectOrBooleanCallback;
import network.pokt.aion.rpc.callbacks.RPCCallback;
import network.pokt.aion.rpc.callbacks.StringCallback;
import network.pokt.aion.rpc.callbacks.TransactionHashCallback;
import network.pokt.aion.rpc.types.BlockTag;
import network.pokt.aion.rpc.types.ObjectOrBoolean;
import network.pokt.pocketsdk.exceptions.CreateQueryException;
import network.pokt.pocketsdk.exceptions.CreateTransactionException;
import network.pokt.pocketsdk.models.Query;
import network.pokt.pocketsdk.models.Transaction;
import network.pokt.pocketsdk.models.Wallet;

public class EthRpc {

    private final PocketAion pocketAion;
    private enum AionRpcMethod {
        aion_protocolVersion,
        aion_syncing,
        aion_nrgPrice,
        aion_blockNumber,
        aion_getBalance,
        aion_getStorageAt,
        aion_getTransactionCount,
        aion_getBlockTransactionCountByHash,
        aion_getBlockTransactionCountByNumber,
        aion_getCode,
        aion_call,
        aion_estimateGas,
        aion_getBlockByHash,
        aion_getBlockByNumber,
        aion_getTransactionByHash,
        aion_getTransactionByBlockHashAndIndex,
        aion_getTransactionByBlockNumberAndIndex,
        aion_getTransactionReceipt,
        aion_getLogs,
        aion_getProof
    }

    // Public interface
    public EthRpc(PocketAion pocketAion) {
        this.pocketAion = pocketAion;
    }

    public void protocolVersion(@NotNull String subnetwork, @NotNull final RPCCallback<String>
            callback) throws CreateQueryException {
        Query query = this.createQuery(subnetwork, AionRpcMethod.aion_protocolVersion, new ArrayList());
        this.pocketAion.executeQueryAsync(new StringCallback(query, callback));
    }

    public void syncing(@NotNull String subnetwork, @NotNull final RPCCallback<ObjectOrBoolean>
            callback) throws CreateQueryException {
        Query query = this.createQuery(subnetwork, AionRpcMethod.aion_syncing, new ArrayList());
        this.pocketAion.executeQueryAsync(new ObjectOrBooleanCallback(query, callback));
    }

    public void nrgPrice(@NotNull String subnetwork, @NotNull final RPCCallback<BigInteger>
            callback) throws CreateQueryException {
        Query query = this.createQuery(subnetwork, AionRpcMethod.aion_nrgPrice, new ArrayList());
        this.pocketAion.executeQueryAsync(new BigIntegerCallback(query, callback));
    }

    public void blockNumber(@NotNull String subnetwork, @NotNull final RPCCallback<BigInteger>
            callback) throws CreateQueryException {
        Query query = this.createQuery(subnetwork, AionRpcMethod.aion_blockNumber, new ArrayList());
        this.pocketAion.executeQueryAsync(new BigIntegerCallback(query, callback));
    }

    public void getBalance(@NotNull String address, @NotNull BlockTag blockTag, @NotNull String
            subnetwork, @NotNull final RPCCallback<BigInteger> callback) throws
            CreateQueryException {
        this.executeGenericQuantityQuery(AionRpcMethod.aion_getBalance, address, blockTag, subnetwork,
                callback);
    }

    public void getStorageAt(@NotNull String address, @NotNull BigInteger storagePosition,
                             @NotNull BlockTag blockTag, @NotNull String subnetwork, @NotNull
                             final RPCCallback<String> callback) throws CreateQueryException {
        blockTag = BlockTag.tagOrLatest(blockTag);
        List<String> rpcParams = new ArrayList<>();
        rpcParams.add(address);
        rpcParams.add(storagePosition.toString(16));
        rpcParams.add(blockTag.getBlockTagString());
        Query query = this.createQuery(subnetwork, AionRpcMethod.aion_getStorageAt, rpcParams);
        this.pocketAion.executeQueryAsync(new StringCallback(query, callback));
    }

    public void getTransactionCount(@NotNull String address, @NotNull BlockTag blockTag, @NotNull
            String subnetwork, @NotNull final RPCCallback<BigInteger> callback) throws
            CreateQueryException {
        this.executeGenericQuantityQuery(AionRpcMethod.aion_getTransactionCount, address, blockTag,
                subnetwork, callback);
    }

    public void getBlockTransactionCountByHash(@NotNull String blockHashHex, @NotNull String
            subnetwork, @NotNull final RPCCallback<BigInteger> callback) throws
            CreateQueryException {
        this.executeGenericCountByBlockHash(AionRpcMethod.aion_getBlockTransactionCountByHash, blockHashHex,
                subnetwork, callback);
    }

    public void getBlockTransactionCountByNumber(BlockTag blockTag, @NotNull String
            subnetwork, @NotNull final RPCCallback<BigInteger> callback) throws
            CreateQueryException {
        this.executeGenericCountForBlockTagQuery(blockTag,
                AionRpcMethod.aion_getBlockTransactionCountByNumber, subnetwork, callback);
    }

    public void getCode(@NotNull String address, BlockTag blockTag, @NotNull String subnetwork,
                        @NotNull final RPCCallback<String> callback) throws CreateQueryException {
        blockTag = BlockTag.tagOrLatest(blockTag);
        List<String> rpcParams = new ArrayList<>();
        rpcParams.add(address);
        rpcParams.add(blockTag.getBlockTagString());
        Query query = this.createQuery(subnetwork, AionRpcMethod.aion_getCode, rpcParams);
        this.pocketAion.executeQueryAsync(new StringCallback(query, callback));
    }

    public void sendTransaction(@NotNull String subnetwork, @NotNull Wallet wallet, @NotNull
            String toAddress, @NotNull BigInteger nrg, @NotNull BigInteger nrgPrice, BigInteger
            value, String data, @NotNull BigInteger nonce, @NotNull final RPCCallback<String>
            callback) throws CreateTransactionException {
        if (subnetwork.equalsIgnoreCase(wallet.getSubnetwork())) {
            throw new CreateTransactionException(wallet, subnetwork, null, "Invalid wallet's " +
                    "subnetwork");
        }

        Map<String, Object> txParams = new HashMap<>();
        txParams.put("from", wallet.getAddress());
        if (toAddress != null) {
            txParams.put("to", toAddress);
        }

        if (nonce != null) {
            txParams.put("nonce", nonce.toString(16));
        }

        if (value != null) {
            txParams.put("value", value.toString(16));
        }

        if (data != null) {
            txParams.put("data", data);
        }

        if (nrg != null) {
            txParams.put("nrg", nrg.toString(16));
        }

        if (nrgPrice != null) {
            txParams.put("nrgPrice", nrgPrice.toString(16));
        }
        Transaction transaction = this.pocketAion.createTransaction(wallet, subnetwork, txParams);
        this.pocketAion.sendTransactionAsync(new TransactionHashCallback(transaction, callback));
    }

    public void call(@NotNull String subnetwork, @NotNull String toAddress, BlockTag blockTag,
                     String fromAddress, BigInteger nrg, BigInteger nrgPrice, BigInteger value,
                     String data, @NotNull final RPCCallback<String> callback) throws
            CreateQueryException {
        Query query = this.callOrEstimateGasQuery(AionRpcMethod.aion_call, subnetwork, toAddress, blockTag,
                fromAddress, nrg, nrgPrice, value, data);
        this.pocketAion.executeQueryAsync(new StringCallback(query, callback));
    }

    public void estimateGas(@NotNull String subnetwork, String toAddress, BlockTag blockTag,
                            String fromAddress, BigInteger nrg, BigInteger nrgPrice, BigInteger
                                    value, String data, @NotNull final RPCCallback<BigInteger>
                                    callback) throws CreateQueryException {
        Query query = this.callOrEstimateGasQuery(AionRpcMethod.aion_estimateGas, subnetwork, toAddress,
                blockTag, fromAddress, nrg, nrgPrice, value, data);
        this.pocketAion.executeQueryAsync(new BigIntegerCallback(query, callback));
    }

    public void getBlockByHash(@NotNull String subnetwork, @NotNull String blockHashHex, boolean
            fullTx, @NotNull final RPCCallback<JSONObject> callback) throws CreateQueryException {
        this.executeGetBlockByHex(AionRpcMethod.aion_getBlockByHash, subnetwork, blockHashHex, fullTx,
                callback);
    }

    public void getBlockByNumber(@NotNull String subnetwork, BlockTag blockTag, boolean
            fullTx, @NotNull final RPCCallback<JSONObject> callback) throws CreateQueryException {
        blockTag = BlockTag.tagOrLatest(blockTag);
        this.executeGetBlockByHex(AionRpcMethod.aion_getBlockByNumber, subnetwork, blockTag.getBlockTagString
                (), fullTx, callback);
    }

    public void getTransactionByHash(@NotNull String subnetwork, @NotNull String txHashHex,
                                     @NotNull final RPCCallback<JSONObject> callback) throws
            CreateQueryException {
        this.executeGetObjectWithHex(AionRpcMethod.aion_getTransactionByHash, subnetwork, txHashHex, callback);
    }

    public void getTransactionByBlockHashAndIndex(@NotNull String subnetwork, @NotNull String
            blockHashHex, @NotNull BigInteger txIndex, @NotNull final RPCCallback<JSONObject>
            callback) throws CreateQueryException {
        this.executeGetObjectWithHexPair(AionRpcMethod.aion_getTransactionByBlockHashAndIndex, subnetwork,
                blockHashHex, txIndex.toString(16), callback);
    }

    public void getTransactionByBlockNumberAndIndex(@NotNull String subnetwork, BlockTag
            blockTag, @NotNull BigInteger txIndex, @NotNull final RPCCallback<JSONObject>
            callback) throws CreateQueryException {

        blockTag = BlockTag.tagOrLatest(blockTag);
        this.executeGetObjectWithHexPair(AionRpcMethod.aion_getTransactionByBlockNumberAndIndex, subnetwork,
                blockTag.getBlockTagString(), txIndex.toString(16), callback);
    }

    public void getTransactionReceipt(@NotNull String subnetwork, @NotNull String txHashHex,
                                      @NotNull final RPCCallback<JSONObject> callback) throws
            CreateQueryException {
        this.executeGetObjectWithHex(AionRpcMethod.aion_getTransactionReceipt, subnetwork, txHashHex, callback);
    }

    public void getLogs(@NotNull String subnetwork, BlockTag fromBlock, BlockTag toBlock,
                        List<String> addressList, List<String> topics, String blockHashHex,
                        @NotNull final RPCCallback<JSONArray> callback) throws
            CreateQueryException {
        Map<String, Object> queryParams = new HashMap<>();

        if (addressList != null) {
            queryParams.put("address", addressList);
        }

        if (topics != null) {
            queryParams.put("topics", topics);
        }

        if (blockHashHex != null) {
            queryParams.put("blockhash", blockHashHex);
        } else {
            queryParams.put("fromBlock", BlockTag.tagOrLatest(fromBlock));
            queryParams.put("toBlock", BlockTag.tagOrLatest(toBlock));
        }

        List rpcParams = new ArrayList();
        rpcParams.add(queryParams);
        Query query = this.createQuery(subnetwork, AionRpcMethod.aion_getLogs, rpcParams);
        this.pocketAion.executeQueryAsync(new ObjectListCallback(query, callback));
    }

    public void getProof(@NotNull String subnetwork, @NotNull String address, @NotNull
            List<String> storageKeys, BlockTag blockTag, @NotNull final RPCCallback<JSONObject>
            callback) throws CreateQueryException {
        blockTag = BlockTag.tagOrLatest(blockTag);
        List rpcParams = new ArrayList();
        rpcParams.add(address);
        rpcParams.add(storageKeys);
        rpcParams.add(blockTag);
        Query query = this.createQuery(subnetwork, AionRpcMethod.aion_getProof, rpcParams);
        this.pocketAion.executeQueryAsync(new ObjectCallback(query, callback));
    }

    // Private interfaces
    private Query createQuery(String subnetwork, AionRpcMethod rpcMethod, List rpcParams) throws
            CreateQueryException {
        Map<String, Object> params = new HashMap<>();
        params.put("rpcMethod", rpcMethod.name());
        params.put("rpcParams", rpcParams);
        return pocketAion.createQuery(subnetwork, params, null);
    }

    private void executeGenericQuantityQuery(@NotNull AionRpcMethod rpcMethod, @NotNull String address,
                                             BlockTag blockTag, @NotNull String
                                                     subnetwork, @NotNull final
                                             RPCCallback<BigInteger> callback) throws
            CreateQueryException {
        blockTag = BlockTag.tagOrLatest(blockTag);
        List<String> rpcParams = new ArrayList<>();
        rpcParams.add(address);
        rpcParams.add(blockTag.getBlockTagString());
        Query query = this.createQuery(subnetwork, rpcMethod, rpcParams);
        this.pocketAion.executeQueryAsync(new BigIntegerCallback(query, callback));
    }

    private void executeGenericCountByBlockHash(@NotNull AionRpcMethod rpcMethod, @NotNull String
            blockHashHex, @NotNull String subnetwork, @NotNull final RPCCallback<BigInteger>
            callback) throws CreateQueryException {
        List<String> rpcParams = new ArrayList<>();
        rpcParams.add(blockHashHex);
        Query query = this.createQuery(subnetwork, rpcMethod, rpcParams);
        this.pocketAion.executeQueryAsync(new BigIntegerCallback(query, callback));
    }

    private void executeGenericCountForBlockTagQuery(BlockTag blockTag, @NotNull AionRpcMethod
            rpcMethod, @NotNull String subnetwork, @NotNull final RPCCallback<BigInteger>
            callback) throws CreateQueryException {
        blockTag = BlockTag.tagOrLatest(blockTag);
        List<String> rpcParams = new ArrayList<>();
        rpcParams.add(blockTag.getBlockTagString());
        Query query = this.createQuery(subnetwork, rpcMethod, rpcParams);
        this.pocketAion.executeQueryAsync(new BigIntegerCallback(query, callback));
    }

    private Query callOrEstimateGasQuery(@NotNull AionRpcMethod rpcMethod, @NotNull String subnetwork,
                                         @NotNull String toAddress, BlockTag blockTag, String
                                                 fromAddress, BigInteger nrg, BigInteger
                                                 nrgPrice, BigInteger value, String data) throws
            CreateQueryException {
        blockTag = BlockTag.tagOrLatest(blockTag);
        Map<String, Object> callTxParams = new HashMap<>();
        callTxParams.put("to", toAddress);
        if (fromAddress != null) {
            callTxParams.put("from", fromAddress);
        }
        if (nrg != null) {
            callTxParams.put("nrg", nrg.toString(16));
        }
        if (nrgPrice != null) {
            callTxParams.put("nrgPrice", nrgPrice.toString(16));
        }

        if (value != null) {
            callTxParams.put("value", value.toString(16));
        }

        if (data != null) {
            callTxParams.put("data", data);
        }
        JSONObject callTxObj = new JSONObject(callTxParams);

        List<String> rpcParams = new ArrayList<>();
        rpcParams.add(callTxObj.toString());
        rpcParams.add(blockTag.getBlockTagString());

        return this.createQuery(subnetwork, rpcMethod, rpcParams);
    }

    private void executeGetBlockByHex(@NotNull AionRpcMethod rpcMethod, @NotNull String subnetwork,
                                      @NotNull String blockIdHex, boolean fullTx, @NotNull final
                                      RPCCallback<JSONObject> callback) throws
            CreateQueryException {
        List<String> rpcParams = new ArrayList<>();
        rpcParams.add(blockIdHex);
        rpcParams.add(String.valueOf(fullTx));
        Query query = this.createQuery(subnetwork, rpcMethod, rpcParams);
        this.pocketAion.executeQueryAsync(new ObjectCallback(query, callback));
    }

    private void executeGetObjectWithHex(@NotNull AionRpcMethod rpcMethod, @NotNull String subnetwork,
                                         @NotNull String hex, @NotNull final
                                         RPCCallback<JSONObject> callback) throws
            CreateQueryException {
        List<String> rpcParams = new ArrayList<>();
        rpcParams.add(hex);
        Query query = this.createQuery(subnetwork, rpcMethod, rpcParams);
        this.pocketAion.executeQueryAsync(new ObjectCallback(query, callback));
    }

    private void executeGetObjectWithHexPair(@NotNull AionRpcMethod rpcMethod, @NotNull String
            subnetwork, @NotNull String hexPrimary, @NotNull String hexSecondary, @NotNull final
    RPCCallback<JSONObject> callback) throws CreateQueryException {
        List<String> rpcParams = new ArrayList<>();
        rpcParams.add(hexPrimary);
        rpcParams.add(hexSecondary);
        Query query = this.createQuery(subnetwork, rpcMethod, rpcParams);
        this.pocketAion.executeQueryAsync(new ObjectCallback(query, callback));
    }

}
