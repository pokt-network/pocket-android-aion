package network.pokt.aion.util;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class SimpleContractMockDispatcher extends Dispatcher {
    @Override
    public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        switch (request.getPath()) {
            case "/queries":
                return new MockResponse().setResponseCode(200).setBody("{\n" +
                        "          \"network\": \"AION\",\n" +
                        "          \"subnetwork\": \"mastery\",\n" +
                        "          \"query\": {},\n" +
                        "          \"decoder\": {},\n" +
                        "          \"result\": \"0x46\",\n" +
                        "          \"decoded\": false,\n" +
                        "          \"error\": false,\n" +
                        "          \"error_msg\": null\n" +
                        "        }");
            default:
                return new MockResponse().setResponseCode(404);
        }
    }
}
