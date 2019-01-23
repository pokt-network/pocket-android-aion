package network.pokt.aion.util;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

import network.pokt.pocketsdk.interfaces.Configuration;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockWebServer;

public class TestConfiguration implements Configuration {


    MockWebServer server;
    URL serverURL;

    public TestConfiguration(@NotNull URL serverURL) {
        this.serverURL = serverURL;
    }

    public TestConfiguration(Dispatcher dispatcher) throws MalformedURLException{
        this.server = new MockWebServer();
        server.setDispatcher(dispatcher);
        this.serverURL = new URL(server.url("/").toString());
    }

    @Override
    public URL getNodeUrl() throws MalformedURLException {
        return this.serverURL;
    }
}
