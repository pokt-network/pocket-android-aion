package network.pokt;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import network.pokt.operations.CreateWalletOperation;
import network.pokt.operations.ImportWalletOperation;
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
            throw new CreateWalletException(data, "Wallet creation process failed");
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
        return null;
    }

    @Override
    public @NotNull Query createQuery(@NotNull String subnetwork, Map<String, Object> params, Map<String, Object> decoder) throws CreateQueryException {
        return null;
    }

    @Override
    public @NotNull String getNetwork() {
        return NETWORK;
    }
}
