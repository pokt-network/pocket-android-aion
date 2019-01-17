package network.pokt.aion.util;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class RpcMockDispatcher extends Dispatcher {

    private Context context;
    private int suffixLength;

    public RpcMockDispatcher(Context context, int suffixLength) {
        this.context = context;
        this.suffixLength = suffixLength;
    }

    private String getRawResponse(String rpcMethod) throws InterruptedException {
        if (rpcMethod == null) {
            throw new InterruptedException("Invalid rpc method provided");
        }

        String rawResName = rpcMethod.substring(suffixLength, rpcMethod.length()).toLowerCase();
        int resId = context.getResources().getIdentifier(rawResName, "raw", context.getPackageName());
        String result = RawFileUtil.readRawTextFile(context, resId);
        return result == null ? "" : result;
    }

    @Override
    public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        switch (request.getPath()) {
            case "/queries":
                String requestRpcMethod = null;
                try {
                    JSONObject requestBody = new JSONObject(request.getBody().readUtf8());
                    JSONObject query = requestBody.getJSONObject("query");
                    requestRpcMethod = query.optString("rpc_method");
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new InterruptedException("Invalid request body");
                }
                String responseBody = this.getRawResponse(requestRpcMethod);
                if (responseBody == null) {
                    return new MockResponse().setResponseCode(200).setBody("{}");
                } else {
                    return new MockResponse().setResponseCode(200).setBody(responseBody);
                }
            case "/transactions":
                return new MockResponse().setResponseCode(200).setBody("{\n" +
                        "          \"network\": \"AION\",\n" +
                        "          \"subnetwork\": \"32\",\n" +
                        "          \"serialized_tx\": \"0x000\",\n" +
                        "          \"tx_metadata\": {},\n" +
                        "          \"hash\": \"0x416aed4e6c58ec1a31b1625cb7c20bcfc650f0d7a92ab9f479ecbdf2ed226e55\",\n" +
                        "          \"metadata\": {},\n" +
                        "          \"error\": false,\n" +
                        "          \"error_msg\": null\n" +
                        "        }");
            default:
                return new MockResponse().setResponseCode(404);
        }
    }
}
