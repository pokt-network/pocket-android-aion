package network.pokt.aion.util;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class PluginMockDispatcher extends Dispatcher {
    @Override
    public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        switch (request.getPath()) {
            case "/transactions":
                return new MockResponse().setResponseCode(200).setBody("{\n" +
                        "          \"network\": \"AION\",\n" +
                        "          \"subnetwork\": \"mastery\",\n" +
                        "          \"serialized_tx\": \"0x000\",\n" +
                        "          \"tx_metadata\": {},\n" +
                        "          \"hash\": \"0x000\",\n" +
                        "          \"metadata\": {},\n" +
                        "          \"error\": false,\n" +
                        "          \"error_msg\": null\n" +
                        "        }");
            case "/queries":
                return new MockResponse().setResponseCode(200).setBody("{\n" +
                        "          \"network\": \"AION\",\n" +
                        "          \"subnetwork\": \"mastery\",\n" +
                        "          \"query\": {},\n" +
                        "          \"decoder\": {},\n" +
                        "          \"result\": \"0x0\",\n" +
                        "          \"decoded\": false,\n" +
                        "          \"error\": false,\n" +
                        "          \"error_msg\": null\n" +
                        "        }");
            default:
                return new MockResponse().setResponseCode(404);
        }
    }
}
