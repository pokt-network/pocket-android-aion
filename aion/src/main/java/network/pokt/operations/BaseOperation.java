package network.pokt.operations;

import android.content.Context;
import android.support.annotation.Nullable;

import org.liquidplayer.javascript.JSContext;
import org.liquidplayer.node.Process;
import org.liquidplayer.node.Process.EventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import network.pokt.aion.R;

abstract class BaseOperation implements JSContext.IJSExceptionHandler, EventListener {

    private Context context;
    Semaphore semaphore;
    private Process process;

    public BaseOperation(Context context) {
        this.semaphore = new Semaphore(0);
        this.context = context;
    }

    // Creates the process
    public boolean startProcess() {
        if (process != null) {
            return false;
        }
        this.process = new Process(
                this.context,
                this.getOperationID(),
                Process.kMediaAccessPermissionsRead,
                this
        );
        this.semaphore.acquireUninterruptibly();
        return true;
    }

    @Override
    public void onProcessStart(Process process, JSContext jsContext) {
        try {
            // Configures the JS context to run
            jsContext.setExceptionHandler(this);
            jsContext.evaluateScript(this.readRawTextFile(R.raw.globals));
            jsContext.evaluateScript(this.readRawTextFile(R.raw.web3_aion));
            jsContext.evaluateScript(this.readRawTextFile(R.raw.aion_instance));
            // Calls execute operation
            this.executeOperation(jsContext);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void onProcessExit(Process process, int exitCode) {
        this.semaphore.release();
    }

    @Override
    public void onProcessFailed(Process process, Exception error) {
        this.semaphore.release();
    }

    @Override
    public void onProcessAboutToExit(Process process, int exitCode) {
        this.semaphore.release();
    }

    // Gets called by onProcessStart to run custom operation code
    abstract void executeOperation(JSContext jsContext);

    // Gets the unique id of this operation
    private String getOperationID() {
        int randomId = new Double(Math.random() * ((Integer.MAX_VALUE - 0) + 1) + 0).intValue();
        return String.format("%s-%d", this.getClass().getName(), randomId);
    }

    @Nullable
    String readRawTextFile(int resId) {
        InputStream inputStream = this.context.getResources().openRawResource(resId);

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader buffReader = new BufferedReader(inputStreamReader);
        String line;
        StringBuilder text = new StringBuilder();
        try {
            while (( line = buffReader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }
}
