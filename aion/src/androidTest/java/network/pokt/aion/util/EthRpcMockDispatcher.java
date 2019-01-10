package network.pokt.aion.util;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class EthRpcMockDispatcher extends Dispatcher {

    private Context context;

    public EthRpcMockDispatcher(Context context) {
        this.context = context;
    }

    private String getRawResponse(String rpcMethod) throws InterruptedException {
        if (rpcMethod == null) {
            throw new InterruptedException("Invalid rpc method provided");
        }

        String rawResName = rpcMethod.substring(4, rpcMethod.length()).toLowerCase();
        int resId = context.getResources().getIdentifier(rawResName, "raw", context.getPackageName());
        return RawFileUtil.readRawTextFile(context, resId);
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
                return new MockResponse().setResponseCode(200).setBody(this.getRawResponse(requestRpcMethod));
            default:
                return new MockResponse().setResponseCode(404);
        }
    }
}
