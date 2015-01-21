package com.raizlabs.android.broker.multipart;

/**
 * Description: Holds information in regards to a part of a MultiPart request
 */
public class RequestEntityPart {

    private final String mName;

    private final String mValue;

    private final boolean isFile;

    /**
     * Creates a new object to store data for a multipart request.
     *
     * @param mName  The name of the part
     * @param mValue The value that the part contains, if it's a file this should be a path to the file.
     * @param isFile if true, then the value will be a path.
     */
    public RequestEntityPart(String mName, String mValue, boolean isFile) {
        this.mName = mName;
        this.mValue = mValue;
        this.isFile = isFile;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }

    public boolean isFile() {
        return isFile;
    }
}
