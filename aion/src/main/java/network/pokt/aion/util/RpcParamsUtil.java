package network.pokt.aion.util;

import android.text.TextUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RpcParamsUtil {

    public static List<String> formatRpcParams(List<Object> rpcParams) {
        List<String> result = new ArrayList<>();
        for (Object objParam : rpcParams) {
            String currStr;
            if (objParam instanceof List) {
                currStr = "[" + TextUtils.join(",", objectsAsRpcParams((List<Object>)objParam)) + "]";
            } else {
                currStr = objectAsRpcParam(objParam);
            }
            result.add(currStr);
        }
        return result;
    }

    private static List<String> objectsAsRpcParams(List<Object> objParams) {
        List<String> result = new ArrayList<>();
        for (Object objParam : objParams) {
            result.add(objectAsRpcParam(objParam));
        }
        return result;
    }

    private static String objectAsRpcParam(Object objParam) {
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
            currStr = "\"" + objParam + "\"";
        } else if(objParam instanceof BigInteger) {
            currStr = "BigInt(" + "\"" + HexStringUtil.prependZeroX(((BigInteger)objParam).toString(16)) + "\"" + ")";
        }
        return currStr;
    }
}
