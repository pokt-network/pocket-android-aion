package network.pokt.aion;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import network.pokt.aion.operations.CreateTransactionOperation;
import network.pokt.aion.operations.CreateWalletOperation;
import network.pokt.aion.operations.ImportWalletOperation;
import network.pokt.pocketsdk.exceptions.CreateQueryException;
import network.pokt.pocketsdk.exceptions.CreateTransactionException;
import network.pokt.pocketsdk.exceptions.CreateWalletException;
import network.pokt.pocketsdk.exceptions.ImportWalletException;
import network.pokt.pocketsdk.exceptions.InvalidConfigurationException;
import network.pokt.pocketsdk.interfaces.Configuration;
import network.pokt.pocketsdk.interfaces.PocketPlugin;
import network.pokt.pocketsdk.models.Query;
import network.pokt.pocketsdk.models.Transaction;
import network.pokt.pocketsdk.models.Wallet;

public class PocketAion extends PocketPlugin {
    private static final String NETWORK = "AION";
    private Context context;

    private PocketAion(@NotNull Configuration configuration) throws InvalidConfigurationException {
        super(configuration);
    }

    public PocketAion(@NotNull Configuration configuration, @NotNull Context context) throws InvalidConfigurationException {
        this(configuration);
        this.context = context;
    }

    @Override
    public @NotNull Wallet createWallet(@NotNull String subnetwork, Map<String, Object> data) throws CreateWalletException {
        Wallet result;
        CreateWalletOperation operation = new CreateWalletOperation(this.context, this.getNetwork(), subnetwork);
        boolean processExecuted = operation.startProcess();
        if(!processExecuted) {
            String errorMsg = operation.getErrorMsg() != null ? operation.getErrorMsg() : "Wallet creation process failed";
            throw new CreateWalletException(data, errorMsg);
        }
        result = operation.getWallet();
        if(result == null) {
            throw new CreateWalletException(data, "Error reading created wallet");
        }
        return result;
    }

    @Override
    public @NotNull Wallet importWallet(@NotNull String privateKey, @NotNull String subnetwork, String address, Map<String, Object> data) throws ImportWalletException {
        Wallet result;
        ImportWalletOperation operation = new ImportWalletOperation(this.context, this.getNetwork(), subnetwork, privateKey);
        boolean processExecuted = operation.startProcess();
        if(!processExecuted) {
            throw new ImportWalletException(privateKey, address, data, "Error recreating wallet from parameters");
        }
        result = operation.getWallet();
        if(result == null) {
            throw new ImportWalletException(privateKey, address, data, "Error reading recreated wallet");
        }
        if (!address.equalsIgnoreCase(result.getAddress())) {
            throw new ImportWalletException(privateKey, address, data, "Invalid address specified");
        }
        return result;
    }

    @Override
    public @NotNull Transaction createTransaction(@NotNull Wallet wallet, @NotNull String subnetwork, Map<String, Object> params) throws CreateTransactionException {
        Transaction result;

        try {
            String nonce = (String) params.get("nonce");
            String to = (String) params.get("to");
            String value = (String) params.get("value");
            String data = (String) params.get("data");
            String nrg = (String) params.get("nrg");
            String nrgPrice = (String) params.get("nrgPrice");
            CreateTransactionOperation operation = new CreateTransactionOperation(this.context, wallet, nonce, to, value, data, nrg, nrgPrice);
            boolean processExecuted = operation.startProcess();
            if(!processExecuted) {
                String errorMsg = operation.getErrorMsg() != null ? operation.getErrorMsg() : "Error creating transaction";
                throw new CreateTransactionException(wallet, subnetwork, params, errorMsg);
            }

            result = operation.getTransaction();
        } catch (Exception e) {
            throw new CreateTransactionException(wallet, subnetwork, params, e.getMessage());
        }

        if(result == null) {
            throw new CreateTransactionException(wallet, subnetwork, params, "Error creating transaction");
        }

        return result;
    }

    @Override
    public @NotNull Query createQuery(@NotNull String subnetwork, Map<String, Object> params, Map<String, Object> decoder) throws CreateQueryException {
        Query result;

        // Extract rpc request
        String rpcMethod;
        List<Object> rpcParams;
        JSONObject queryData = new JSONObject();
        try {
            rpcMethod = (String) params.get("rpcMethod");
            rpcParams = (List<Object>) params.get("rpcParams");
            queryData.put("rpc_method", rpcMethod);
            queryData.put("rpc_params", rpcParams);
        } catch (Exception e) {
            throw new CreateQueryException(subnetwork, params, decoder, e.getMessage());
        }

        // Extract decoder
        List<String> returnTypes;
        JSONObject queryDecoder = new JSONObject();
        try {
            returnTypes = (List<String>) decoder.get("returnTypes");
            decoder.put("return_types", returnTypes);
        } catch (Exception e) {
            // Log message and send empty decoder
            Logger.getGlobal().warning(e.getMessage());
            queryDecoder = new JSONObject();
        }

        try {
            result = new Query(this.getNetwork(), subnetwork, queryData, queryDecoder);
        } catch (Exception e) {
            throw new CreateQueryException(subnetwork, params, decoder, e.getMessage());
        }

        if(result == null) {
            throw new CreateQueryException(subnetwork, params, decoder, "Unknown error creating query");
        }
        return result;
    }

    @Override
    public @NotNull String getNetwork() {
        return NETWORK;
    }
}
