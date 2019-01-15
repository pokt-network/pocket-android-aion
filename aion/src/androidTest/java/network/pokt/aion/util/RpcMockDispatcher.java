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
        //this.peek().setSocketPolicy(SocketPolicy.NO_RESPONSE);
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
                return new MockResponse().setResponseCode(200).setBody("{\\\"network\\\":\\\"AION" +
                        "\\\",\\\"subnetwork\\\":\\\"mastery\\\"," +
                        "\\\"query\\\":{\\\"rpc_method\\\":\\\"aion_protocolVersion\\\"," +
                        "\\\"rpc_params\\\":[]},\\\"decoder\\\":{},\\\"result\\\":\\\"54\\\"," +
                        "\\\"decoded\\\":false,\\\"error\\\":false,\\\"error_msg\\\":null}");
                //String responseBody = this.getRawResponse(requestRpcMethod);
//                if (responseBody == null) {
//                    return new MockResponse().setResponseCode(200).setBody("{}");
//                } else {
//                    return new MockResponse().setResponseCode(200).setBody(responseBody);
//                }
            default:
                return new MockResponse().setResponseCode(404);
        }
    }
}
