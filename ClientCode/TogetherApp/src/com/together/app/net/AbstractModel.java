package com.together.app.net;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public abstract class AbstractModel implements IHttpRequestListener {
    public static final int MODEL_ACTION_BASE = 0;
    public static final int MODEL_ACTION_LOCAL_ERROR = MODEL_ACTION_BASE + 1;
    public static final int MODEL_ACTION_SHOW_ERROR_MESSAGE = MODEL_ACTION_BASE + 2;
    public static final int MODEL_ACTION_LOGIN = MODEL_ACTION_BASE + 3;

    public static final int MODEL_ACTION_SINA_BASE = 1000;
    public static final int MODEL_ACTION_SINA_UID = MODEL_ACTION_SINA_BASE + 1;
    public static final int MODEL_ACTION_SINA_INFO = MODEL_ACTION_SINA_BASE + 2;

    public static final int MODEL_ACTION_TENCENT_BASE = 2000;
    public static final int MODEL_ACTION_TENCENT_INFO = MODEL_ACTION_TENCENT_BASE + 1;

    public static final Integer KEY_ABSTRACT_BASE = 100;
    public static final Integer KEY_DIALOG_ERROR_MSG = KEY_ABSTRACT_BASE + 1;
    public static final Integer KEY_DIALOG_ERROR_CODE = KEY_ABSTRACT_BASE + 2;
    public static final Integer KEY_SUBMIT_INFO = KEY_ABSTRACT_BASE + 3;

    public static final int MODEL_EVENT_BASE = 1000;

    private Vector<IModelListener> mListeners = new Vector<IModelListener>();

    /**
     * The data content
     */
    private Hashtable mContent = new Hashtable();

    /**
     * Get the value according to the key
     * 
     * @param key
     * @return value
     */
    public synchronized Object get(Integer key) {
        if (key == null) {
            return null;
        }
        return mContent.get(key);
    }

    /**
     * Get the value according to the key and then remove it.
     * 
     * @param key
     * @return value
     */
    public synchronized Object fetch(Integer key) {
        if (key == null) {
            return null;
        }

        Object value = mContent.get(key);
        mContent.remove(key);
        return value;
    }

    /**
     * Get the boolean value according to the key
     * 
     * @param key
     * @return value
     */
    public synchronized boolean getBool(Integer key) {
        Object o = get(key);
        if (o != null && o instanceof Boolean) {
            Boolean b = (Boolean) o;
            return b.booleanValue();
        }

        return false;
    }

    /**
     * return default value if key does not exist
     * 
     * @param key
     * @param defaultValue
     */
    public synchronized boolean optBool(Integer key, boolean defaultValue) {
        Object o = get(key);
        if (o != null && o instanceof Boolean) {
            Boolean b = (Boolean) o;
            return b.booleanValue();
        }

        return defaultValue;
    }

    /**
     * Get the boolean value according to the key and then remove it.
     * 
     * @param key
     * @return value
     */
    public synchronized boolean fetchBool(Integer key) {
        Object o = fetch(key);
        if (o != null && o instanceof Boolean) {
            Boolean b = (Boolean) o;
            return b.booleanValue();
        }

        return false;
    }

    /**
     * Get the string value according to the key
     * 
     * @param key
     * @return value
     */
    public synchronized String getString(Integer key) {
        Object o = get(key);
        if (o != null && o instanceof String) {
            return (String) o;
        }

        return null;
    }

    /**
     * return "" if key does not exist
     * 
     * @param key
     */
    public synchronized String optString(Integer key) {
        return optString(key, "");
    }

    /**
     * return default value if key does not exist
     * 
     * @param key
     * @param defaultValue
     */
    public synchronized String optString(Integer key, String defaultValue) {
        Object o = get(key);
        if (o != null && o instanceof String) {
            return (String) o;
        }

        return defaultValue;
    }

    /**
     * Get the string value according to the key and then remove it.
     * 
     * @param key
     * @return value
     */
    public synchronized String fetchString(Integer key) {
        Object o = fetch(key);
        if (o != null && o instanceof String) {
            return (String) o;
        }

        return null;
    }

    /**
     * Get the int value according to the key.
     * 
     * NOTE: if the key does not exist, it will return -1.
     * 
     * @param key
     * @return value
     */
    public synchronized int getInt(Integer key) {
        Object o = get(key);
        if (o != null && o instanceof Integer) {
            Integer i = (Integer) o;
            return i.intValue();
        }

        return -1;
    }

    public synchronized int optInt(Integer key, int defaultValue) {
        Object o = get(key);
        if (o != null && o instanceof Integer) {
            Integer i = (Integer) o;
            return i.intValue();
        }

        return defaultValue;
    }

    /**
     * Get the int value according to the key and then remove it.
     * 
     * NOTE: if the key does not exist, it will return -1.
     * 
     * @param key
     * @return value
     */
    public synchronized int fetchInt(Integer key) {
        Object o = fetch(key);
        if (o != null && o instanceof Integer) {
            Integer i = (Integer) o;
            return i.intValue();
        }

        return -1;
    }

    /**
     * Get the vector value according to the key.
     * 
     * @param key
     * @return value
     */
    public synchronized Vector getVector(Integer key) {
        Object o = get(key);
        if (o != null && o instanceof Vector) {
            return (Vector) o;
        }

        return new Vector();
    }

    /**
     * Get the vector value according to the key and then remove it.
     * 
     * @param key
     * @return value
     */
    public synchronized Vector fetchVector(Integer key) {
        Object o = fetch(key);
        if (o != null && o instanceof Vector) {
            return (Vector) o;
        }

        return new Vector();
    }

    /**
     * Put the key-value pair
     * 
     * @param key
     * @param value
     */
    public synchronized void put(Integer key, Object value) {
        if (key == null) {
            return;
        }

        if (value == null) {
            remove(key);
            return;
        }

        mContent.put(key, value);
    }

    /**
     * Put the key with an int value
     * 
     * @param key
     * @param value
     */
    public synchronized void put(Integer key, int value) {
        Integer i = new Integer(value);
        put(key, i);
    }

    /**
     * Put the key with a boolean value.
     * 
     * @param key
     * @param value
     */
    public synchronized void put(Integer key, boolean value) {
        Boolean b = new Boolean(value);
        put(key, b);
    }

    /**
     * Remove a key-value pair according to the key, do nothing if doesn't find
     * the key
     * 
     * @param key
     */
    public synchronized void remove(Integer key) {
        if (key == null) {
            return;
        }
        mContent.remove(key);
    }

    /**
     * Remove all
     */
    public synchronized void removeAll() {
        mContent.clear();
    }

    /**
     * To string format.
     */
    @Override
    public String toString() {
        Enumeration en = mContent.keys();
        StringBuffer sb = new StringBuffer();
        while (en.hasMoreElements()) {
            Object key = en.nextElement();
            Object value = mContent.get(key);

            if (value == this) {
                // ignore itself to avoid StackOverflowError
                continue;
            }

            sb.append('(');
            sb.append(key);
            sb.append(',');
            sb.append(value);
            sb.append(')');

        }
        return sb.toString();
    }

    protected void postModelEvent(int action, boolean success) {
        for (int i = 0; i < mListeners.size(); i++) {
            IModelListener listener = mListeners.elementAt(i);
            listener.onActionResult(action, success);
        }
    }

    public void addListener(IModelListener listener) {
        mListeners.addElement(listener);
    }

    public void removeListener(IModelListener listener) {
        mListeners.removeElement(listener);
    }

    public IModelListener getTopListner() {
        return mListeners.lastElement();
    }
};
