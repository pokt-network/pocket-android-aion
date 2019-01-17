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
in the case of AION, besides Mainnet (subnetwork `256`), you also have access to the Mastery testnet (subnetwork `32`). 
In the case of connecting to a custom network, make sure the Pocket Node you are connecting to supports the given subnetwork by executing the following `curl` command.

```
// Request
curl -X GET https://aion.pokt.network/health

// Result
{
    "version":"0.0.12",
    "networks":[
        {"network":"AION","version":"0.0.7","package_name":"pnp-aion","subnetworks":["256","32"]}
    ]
}

```

This is useful to allow users to hop between networks, or for establishing differences between your application's 
test environment and production environments.

# Using a Pocket Android Plugin
Just import the `PocketAion` class and call into any of the functions described below.

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
Wallet newWallet = pocketAion.createWallet("32", null);
```

And to import:

```
String privateKey = "0x";
String address = "0x";

Wallet importedWallet = pocketAion.importWallet(privateKey, "32", address, null);
```

## Querying Data
Currently there are 2 supported namespaces in Pocket Node for AION: `net` and `eth`.
In the examples below you will see how you can query the supported RPC calls in both 
namespaces.

```
// Example accessing a net RPC call 
try {
    pocketAion.net.version("32", new RPCCallback<String>() {
        @Override
        public void onResult(String result, Exception exception) {
            // The result here is the `result` of the RPC call
        }
    });
} catch (CreateQueryException e) {
    e.printStackTrace();
}
```

```
// Example accessing a eth RPC call 
try {
    pocketAion.eth.getTransactionCount("0xa0f9b0086fdf6c29f67c009e98eb31e1ddf1809a6ef2e44296a377b37ebb9827", null, "32", new RPCCallback<BigInteger>() {
        @Override
        public void onResult(BigInteger result, Exception exception) {
            // The result here is the parsed `result` of the eth_getTransactionCount RPC method.
        }
    });
} catch (CreateQueryException e) {
    e.printStackTrace();
}                                                                                                                                                                                                                                                                                                             
```

## Sending a transaction
To send a transaction just use the `sendTransaction` function in the `eth` namespace, like the example 
below:

```
wallet = pocketAion.createWallet("32", null);
pocketAion.eth.sendTransaction("32", wallet, "0xa0f9b0086fdf6c29f67c009e98eb31e1ddf1809a6ef2e44296a377b37ebb9827", new BigInteger("21000"), new BigInteger("10000000000"), new BigInteger("1"), null, new BigInteger("0"), new RPCCallback<String>() {
    @Override
    public void onResult(String result, Exception exception) {
        // Result is the transaction hash
    }
});                       
```

## Interacting with a smart contract
To interact with an AION smart contract you must use the `AionContract` class. 

### Initializing an AionContract instance
Here's an example on how to initialize your `AionContract`:

```
// You must create an instance of PocketAion first
PocketAion pocketAion = new PocketAion(new Configuration() {
    @Override
    public URL getNodeUrl() throws MalformedURLException {
        return new URL("https://aion.pokt.network");
    }
}, appContext);

// Then initialize the JSONArray containing your contract's abiInterface
String rawJSON = "[{\"outputs\":[{\"name\":\"d\",\"type\":\"uint128\"}]," +
    "\"constant\":true,\"payable\":false,\"inputs\":[{\"name\":\"a\"," +
    "\"type\":\"uint128\"}],\"name\":\"multiply\",\"type\":\"function\"}]";
JSONArray abiInterface = new JSONArray(rawJSON);
    
// Finally initialize your AionContract
AionContract contract = new AionContract(pocketAion, abiInterface, "0xa0f9b0086fdf6c29f67c009e98eb31e1ddf1809a6ef2e44296a377b37ebb9827", "32");
```

### Calling an AionContract function
There are 2 main distinctions when calling a smart contract function, whether or not calling it alters 
the state of the smart contract, which is indicated in the `constant` attribute of the JSON.

To call a constant function follow the example below.
```
// Prepare parameters
List<Object> functionParams = new ArrayList<>();
functionParams.add(new BigInteger("10"));

// Execute function (null values are optional and the defaults will be used if not provided)
contract.executeConstantFunction("multiply", null, functionParams, null, null, null, new RPCCallback<Object>() {

    @Override
    public void onResult(Object result, Exception exception) {
        // Since we know from JSON ABI that the return value is a uint128 we can check if it's of type String
        // Result should be input * 7
        // Since input was 10, result = 70
    }
});
                    
```

To call a non-constant function it's a similar flow as before, we just need a `Wallet` to sign the transaction object.
```
// Prepare parameters
List<Object> functionParams = new ArrayList<>();
functionParams.add(new BigInteger("10"));

// Execute function (null values are optional and the defaults will be used if not provided)
contract.executeFunction("multiply", wallet, functionParams, null, null, null, null, new RPCCallback<String>() {
    @Override
    public void onResult(String result, Exception exception) {
        // The result is the transaction hash generated.
    }
});
```

# Advanced Usage
In addition to the functions above you can use the functions below to create and send `Transaction` and `Query` objects to your configured Pocket Node, either synchronously or asynchronously.

## Creating and sending a Transaction
Follow the example below to create a `Transaction` object to write to the given AION network with the parameters below and `subnetwork`. 
Throws `CreateTransactionException` in case of errors.

```
// First import the sender's wallet
String privateKey = "0x";
String address = "0x";

Wallet senderWallet = pocketAion.importWallet(privateKey, "32", address, null);

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
Transaction transaction = pocketAion.createTransaction(senderWallet, "32", txParams);
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
Query query = this.pocketAion.createQuery("32", params, null);
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
