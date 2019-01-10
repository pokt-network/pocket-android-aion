package network.pokt.aion.util;

import java.net.MalformedURLException;
import java.net.URL;

import network.pokt.pocketsdk.interfaces.Configuration;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockWebServer;

public class TestConfiguration implements Configuration {


    MockWebServer server = new MockWebServer();

    public TestConfiguration(Dispatcher dispatcher) {
        server.setDispatcher(dispatcher);
    }

    @Override
    public URL getNodeUrl() throws MalformedURLException {
        String serverURL = server.url("/").toString();
        return new URL(serverURL);
    }
}
