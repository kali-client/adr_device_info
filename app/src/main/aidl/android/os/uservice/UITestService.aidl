// UITestService.aidl
package android.os.uservice;

// Declare any non-default types here with import statements

interface UITestService {
      void putValue(String key, String value);
      String getValue(String key);
}