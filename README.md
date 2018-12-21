# Pocket Android AION Plugin
AION Android Plugin to connect to any AION compatible Pocket Node. 
For more information about Pocket Node you can checkout the repo [here](https://github.com/pokt-network/pocket-node).

# Installation
This project is hosted in Github and you can install it using [Jitpack](https://www.jitpack.io/).

First add the following to your root `build.gradle`:

```
    allprojects {
		repositories {
			maven { url 'https://www.jitpack.io' }
		}
	}
```

Add the following to your Gradle file `dependencies` closure:

`implementation 'com.github.pokt-network:pocket-android-aion:0.0.1'`

***Optional***

In the case of having errors installing the dependency with the above steps, try adding the following
to the `dependencies` closure:

`implementation 'com.android.support:support-core-utils'`

# About this plugin
A Pocket Network plugin will allow your application to send `Transaction` and `Query` objects to any given Pocket Node
that supports the AION network.

A `Transaction` refers to any calls that alter the state of the network: sending AION from one account to another, calling a smart contract, etc.

A `Query` refers to any calls that read data from the current state of the network: Getting an account balance, reading from a smart contract.

## Subnetwork considerations
A subnetwork in terms of a Pocket Node is any given parallel network for a decentralized system, for example
in the case of AION, besides Mainnet (subnetwork `main`), you also have access to the Mastery testnet (subnetwork `mastery`). 
In the case of connecting to a custom network, make sure the Pocket Node you are connecting to supports the given subnetwork.

This is useful to allow users to hop between networks, or for establishing differences between your application's 
test environment and production environments.

# Using a Pocket Android Plugin
Just import the `PocketAion` class and call into any of the functions described below. In addition to that you can use
the functions below to send the created `Transaction` and `Query` objects to your configured Pocket Node, either synchronously or asynchronously.

## The Configuration object
The constructor for any given `PocketAion` instance requires a class implementing the `Configuration` interface, 
and an instance of your Android application `Context`. Let's take a look at the example below:

```
PocketAion pocketAion = new PocketAion(new Configuration() {
    @Override
    public URL getNodeUrl() throws MalformedURLException {
        return new URL("https://aion.pokt.network");
    }
}, appContext);
```

## Creating and Importing a Wallet
Follow the following example to create an AION Wallet:

```
Wallet newWallet = pocketAion.createWallet("mastery", null);
```

And to import:

```
String privateKey = "0x";
String address = "0x";

Wallet importedWallet = pocketAion.importWallet(privateKey, "mastery", address, null);
```

## Creating and sending a Transaction
Follow the example below to create a `Transaction` object to write to the given AION network with the parameters below and `subnetwork`. 
Throws `CreateTransactionException` in case of errors.

```
// First import the sender's wallet
String privateKey = "0x";
String address = "0x";

Wallet senderWallet = pocketAion.importWallet(privateKey, "mastery", address, null);

// Build your transaction parameters
Map<String, Object> txParams = new HashMap<>();
txParams.put("nonce", "0x0");
txParams.put("to", "0x");
txParams.put("value", "0x989680");
// You can pass in correctly encoded data argument to your transaction in the case of calling a smart contract.
txParams.put("data", null);
txParams.put("nrg", "0x989680");
txParams.put("nrgPrice", "0x989680");

// Create and sign your Transaction object
Transaction transaction = pocketAion.createTransaction(senderWallet, "mastery", txParams);
```

To send your newly created `Transaction` to the node use the `sendTransaction` method:

```
try {
   Transaction response = pocketAion.sendTransaction(transaction);
} catch(JSONException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}
```

## Creating and sending a Query
Follow the example below to create a `Transaction` object to write to the given AION network with the parameters below and `subnetwork`. 
Throws `CreateQueryException` in case of errors.

```
String[] rpcParams = {"0x0", "latest"};
Map<String, Object> params = new HashMap<>();
params.put("rpcMethod", "eth_getBalance");
params.put("rpcParams", Arrays.asList(rpcParams));
Query query = this.pocketAion.createQuery("4", params, null);
```

To send your `Query` to the node use the `executeQuery` method:

```
try {
   Query response = pocketAion.executeQuery(query);
} catch(JSONException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}
```

# References
Refer to the AION JSON RPC documentation [here](https://github.com/aionnetwork/aion/wiki/JSON-RPC-API-Docs) for more information on the available RPC methods you can call from your application.
