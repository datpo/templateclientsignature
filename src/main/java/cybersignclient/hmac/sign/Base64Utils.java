package cybersignclient.hmac;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Base64Utils {
    public static final int ERROR_EXCEPTION           = 0;
    public static final int SUCCESS                   = 1;
    public static final int ERROR_BAD_DATA            = 2;
    public static final int ERROR_BAD_SERIAL          = 3;
    public static final int ERROR_SIGN_DATA_FAIL      = 4;
    public static final int ERROR_CERT_ENCODE_FAIL    = 5;
    public static final int ERROR_NOT_TOKEN           = 6;
    public static final int ERROR_LOAD_LIB_FAIL       = 7;
    public static final int ERROR_PIN_FAIL            = 8;
    public static final int ERROR_LIB_JAVA_VERSION    = 9;
    public static final int ERROR_NOT_CERT            = 10;
    public static final int ERROR_SIGN_OOXML_FAIL     = 11;
    public static final int ERROR_SIGN_PDF_FAIL       = 12;

    private static byte[] mBase64EncMap, mBase64DecMap;

    /**
     * Class initializer. Initializes the Base64 alphabet (specified in
     * RFC-2045).
     */
    static {
        byte[] base64Map = {
            (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F',
            (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
            (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R',
            (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X',
            (byte) 'Y', (byte) 'Z',
            (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
            (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l',
            (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r',
            (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x',
            (byte) 'y', (byte) 'z',
            (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
            (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '+', (byte) '/'};
        mBase64EncMap = base64Map;
        mBase64DecMap = new byte[128];
        for (int i = 0; i < mBase64EncMap.length; i++) {
            mBase64DecMap[mBase64EncMap[i]] = (byte) i;
        }
    }

    /**
     * This class isn't meant to be instantiated.
     */
    private Base64Utils() {
    }

    /**
     * Encodes the given byte[] using the Base64-encoding, as specified in
     * RFC-2045 (Section 6.8).
     *
     * @param aData the data to be encoded
     * @return the Base64-encoded <var>aData</var>
     * @exception IllegalArgumentException if NULL or empty array is passed
     */
    public static String base64Encode(byte[] aData) {
        if ((aData == null) || (aData.length == 0)) {
            throw new IllegalArgumentException("Can not encode NULL or empty byte array.");
        }

        byte encodedBuf[] = new byte[((aData.length + 2) / 3) * 4];

        // 3-byte to 4-byte conversion
        int srcIndex, destIndex;
        for (srcIndex = 0, destIndex = 0; srcIndex < aData.length - 2; srcIndex += 3) {
            encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex] >>> 2) & 077];
            encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex + 1] >>> 4) & 017
                    | (aData[srcIndex] << 4) & 077];
            encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex + 2] >>> 6) & 003
                    | (aData[srcIndex + 1] << 2) & 077];
            encodedBuf[destIndex++] = mBase64EncMap[aData[srcIndex + 2] & 077];
        }

        // Convert the last 1 or 2 bytes
        if (srcIndex < aData.length) {
            encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex] >>> 2) & 077];
            if (srcIndex < aData.length - 1) {
                encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex + 1] >>> 4) & 017
                        | (aData[srcIndex] << 4) & 077];
                encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex + 1] << 2) & 077];
            } else {
                encodedBuf[destIndex++] = mBase64EncMap[(aData[srcIndex] << 4) & 077];
            }
        }

        // Add padding to the end of encoded data
        while (destIndex < encodedBuf.length) {
            encodedBuf[destIndex] = (byte) '=';
            destIndex++;
        }

        String result = new String(encodedBuf);
        return result;
    }

    /**
     * Decodes the given Base64-encoded data, as specified in RFC-2045 (Section
     * 6.8).
     *
     * @param aData the Base64-encoded aData.
     * @return the decoded <var>aData</var>.
     * @exception IllegalArgumentException if NULL or empty data is passed
     */
    public static byte[] base64Decode(String aData) {
        if ((aData == null) || (aData.length() == 0)) {
            throw new IllegalArgumentException("Can not decode NULL or empty string.");
        }

        byte[] data = aData.getBytes();

        // Skip padding from the end of encoded data
        int tail = data.length;
        while (data[tail - 1] == '=') {
            tail--;
        }

        byte decodedBuf[] = new byte[tail - data.length / 4];

        // ASCII-printable to 0-63 conversion
        for (int i = 0; i < data.length; i++) {
            data[i] = mBase64DecMap[data[i]];
        }

        // 4-byte to 3-byte conversion
        int srcIndex, destIndex;
        for (srcIndex = 0, destIndex = 0; destIndex < decodedBuf.length - 2;
                srcIndex += 4, destIndex += 3) {
            decodedBuf[destIndex] = (byte) (((data[srcIndex] << 2) & 255)
                    | ((data[srcIndex + 1] >>> 4) & 003));
            decodedBuf[destIndex + 1] = (byte) (((data[srcIndex + 1] << 4) & 255)
                    | ((data[srcIndex + 2] >>> 2) & 017));
            decodedBuf[destIndex + 2] = (byte) (((data[srcIndex + 2] << 6) & 255)
                    | (data[srcIndex + 3] & 077));
        }

        // Handle last 1 or 2 bytes
        if (destIndex < decodedBuf.length) {
            decodedBuf[destIndex] = (byte) (((data[srcIndex] << 2) & 255)
                    | ((data[srcIndex + 1] >>> 4) & 003));
        }
        if (++destIndex < decodedBuf.length) {
            decodedBuf[destIndex] = (byte) (((data[srcIndex + 1] << 4) & 255)
                    | ((data[srcIndex + 2] >>> 2) & 017));
        }

        return decodedBuf;
    }

    public static byte[] getImageBytes(String base64Source)
    {
        // tokenize the data
        String[] parts = base64Source.split(",");
        String imageString = parts[1];
        return base64Decode(imageString);
    }

}
