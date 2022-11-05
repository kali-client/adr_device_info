package android.os.uservice;

import android.os.RemoteException;

import java.util.HashMap;

public class TestService extends UITestService.Stub {
    private static HashMap<String, String> map = new HashMap<>();

    @Override
    public void putValue(String key, String value) throws RemoteException {
        map.put(key, value);
    }

    @Override
    public String getValue(String key) throws RemoteException {
        return map.get(key);
    }
}
