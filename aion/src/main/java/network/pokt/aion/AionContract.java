package network.pokt.aion;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.pokt.aion.abi.v2.Function;
import network.pokt.aion.exceptions.AionContractException;
import network.pokt.aion.rpc.callbacks.RPCCallback;
import network.pokt.pocketsdk.exceptions.CreateQueryException;
import network.pokt.pocketsdk.exceptions.CreateTransactionException;
import network.pokt.pocketsdk.models.Wallet;

public class AionContract {

    private PocketAion pocketAion;
    private JSONArray abiDefinition;
    private String contractAddress;
    private String subnetwork;
    private Map<String, Function> functions = new HashMap<>();

    // Public interface
    public AionContract(@NotNull PocketAion pocketAion, @NotNull JSONArray abiDefinition, @NotNull String contractAddress, @NotNull String subnetwork) throws JSONException, AionContractException {
        this.pocketAion = pocketAion;
        this.abiDefinition = abiDefinition;
        this.contractAddress = contractAddress;
        this.subnetwork = subnetwork;
        if (this.subnetwork == null || this.pocketAion == null || this.abiDefinition == null || this.contractAddress == null) {
            throw new AionContractException("None of the arguments can be null");
        }
        this.parseContractFunctions();
    }

    public void executeConstantFunction(@NotNull String functionName, String fromAddress, List<Object> functionParams, BigInteger nrg, BigInteger nrgPrice, BigInteger value, @NotNull final RPCCallback<Object> callback) throws CreateQueryException, AionContractException {
        Function function = this.functions.get(functionName);
        if (function == null || function.isConstant() == false) {
            throw new AionContractException("Invalid function name or function is not constant");
        }

        String data = function.getEncodedFunctionCall(this.pocketAion.getContext(), functionParams);
        this.pocketAion.eth.call(this.subnetwork, this.contractAddress, null, fromAddress, nrg, nrgPrice, value, data, new RpcCallHandler(callback));
    }

    public void executeFunction(@NotNull String functionName, @NotNull Wallet wallet, List<Object> functionParams, BigInteger nonce, @NotNull BigInteger nrg, @NotNull BigInteger nrgPrice, @NotNull BigInteger value, @NotNull final RPCCallback<String> callback) throws CreateTransactionException, CreateQueryException, AionContractException {
        Function function = this.functions.get(functionName);
        if (function == null) {
            throw new AionContractException("Invalid function name");
        }

        String data = function.getEncodedFunctionCall(this.pocketAion.getContext(), functionParams);
        if (nonce != null) {
            this.sendTransaction(wallet, nonce, nrg, nrgPrice, value, data, callback);
        } else {
            TxCountRpcHandler txCountHandler = new TxCountRpcHandler(wallet, nrg, nrgPrice, value, data, callback);
            this.pocketAion.eth.getTransactionCount(wallet.getAddress(), null, this.subnetwork, txCountHandler);
        }
    }

    public PocketAion getPocketAion() {
        return pocketAion;
    }

    // Private interface
    private void sendTransaction(@NotNull Wallet wallet, @NotNull BigInteger nonce, @NotNull BigInteger nrg, @NotNull BigInteger nrgPrice, @NotNull BigInteger value, @NotNull String data, @NotNull final RPCCallback<String> callback) throws CreateTransactionException {
        this.pocketAion.eth.sendTransaction(this.subnetwork, wallet, this.contractAddress, nrg, nrgPrice, value, data, nonce, callback);
    }

    private void parseContractFunctions() throws JSONException {
        for (int i = 0; i < this.abiDefinition.length(); i++) {
            JSONObject abiElement = this.abiDefinition.optJSONObject(i);
            Function function = Function.parseFunctionElement(abiElement);
            if (function != null) {
                functions.put(function.getName(), function);
            }
        }
    }

    private class RpcCallHandler implements RPCCallback<String> {

        private final RPCCallback<Object> callback;

        public RpcCallHandler(@NotNull final RPCCallback<Object> callback) {
            this.callback = callback;
        }

        @Override
        public void onResult(String result, Exception exception) {
            this.callback.onResult(result, exception);
        }
    }

    private class TxCountRpcHandler implements RPCCallback<BigInteger> {

        private Wallet wallet;
        private BigInteger nrg;
        private BigInteger nrgPrice;
        private BigInteger value;
        private String data;
        private  RPCCallback<String> callback;

        public TxCountRpcHandler(@NotNull Wallet wallet, @NotNull BigInteger nrg, @NotNull BigInteger nrgPrice, @NotNull BigInteger value, @NotNull String data, @NotNull final RPCCallback<String> callback) {
            this.wallet = wallet;
            this.nrg = nrg;
            this.nrgPrice = nrgPrice;
            this.value = value;
            this.data = data;
            this.callback = callback;
        }

        @Override
        public void onResult(BigInteger result, Exception exception) {
            if (exception != null) {
                this.callback.onResult(null, exception);
            }

            if (result == null) {
                this.callback.onResult(null, new Exception("Could not retrieve account nonce, try calling with nonce"));
            }

            AionContract contractInstance = AionContract.this;
            try {
                contractInstance.sendTransaction(this.wallet, result, this.nrg, this.nrgPrice, this.value, this.data, callback);
            } catch (CreateTransactionException cte) {
                this.callback.onResult(null, cte);
            }
        }
    }
}
