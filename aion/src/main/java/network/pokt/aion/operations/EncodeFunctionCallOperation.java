package network.pokt.aion.operations;

import android.content.Context;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;
import org.liquidplayer.javascript.JSContext;
import org.liquidplayer.javascript.JSException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import network.pokt.aion.R;
import network.pokt.aion.abi.v2.Function;
import network.pokt.aion.util.RawFileUtil;

public class EncodeFunctionCallOperation extends BaseOperation {

    private String encodedFunctionCall;
    private Function function;
    private List<Object> params;

    EncodeFunctionCallOperation(Context context) {
        super(context);
    }

    public EncodeFunctionCallOperation(@NotNull Context context, @NotNull  Function function, @NotNull List<Object> params) {
        super(context);
        this.function = function;
        this.params = params;
    }

    @Override
    void executeOperation(JSContext jsContext) {
        // Convert parameters to string
        String functionJSONStr = this.function.getFunctionJSON().toString();
        String functionParamsStr = TextUtils.join(",", this.getJSParams());

        // Generate code to run
        String jsCode = String.format(RawFileUtil.readRawTextFile(this.context, R.raw.encode_function_call), functionJSONStr, functionParamsStr);

        // Evaluate code
        jsContext.evaluateScript(jsCode);

        // Extract value
        this.encodedFunctionCall = jsContext.property("functionCallData").toString();
    }

    public String getEncodedFunctionCall() {
        return encodedFunctionCall;
    }

    @Override
    public void handle(JSException exception) {
        super.handle(exception);
        this.encodedFunctionCall = null;
    }

    public List<String> getJSParams() {
        List<String> result = new ArrayList<>();
        for (Object objParam : this.params) {
            String currStr;
            if (objParam instanceof List) {
                currStr = "[" + TextUtils.join(",", (List)objParam) + "]";
            } else {
                currStr = this.objectAsString(objParam);
            }
            result.add(currStr);
        }
        return result;
    }

    private String objectAsString(Object objParam) {
        String currStr = null;

        if (objParam == null) {
            currStr = "null";
        }  else if (objParam instanceof Boolean ||
                objParam instanceof Double ||
                objParam instanceof Float ||
                objParam instanceof Integer ||
                objParam instanceof Long ||
                objParam instanceof Byte ||
                objParam instanceof Short) {
            currStr = objParam.toString();
        } else if (objParam instanceof String) {
            currStr = (String) objParam;
        } else if(objParam instanceof BigInteger) {
            currStr = ((BigInteger)objParam).toString(16);
        }

        return currStr;
    }
}
