package com.lelo.sdk.lelo_sdk_f1_android_lib;

import android.util.Log;

public class Utils {

    public static String byte2String(byte[] b) {
        if (b != null && b.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(b.length);
            for (byte byteChar : b)
                stringBuilder.append(String.format("%02X", byteChar));

            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    public static int hex2Decimal(String value) {
        String digits = "0123456789ABCDEF";
        value = value.toUpperCase();
        int result = 0;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            int d = digits.indexOf(c);
            result = 16 * result + d;
        }
        return result;
    }

    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

    public static byte[] parseHexBinary(String s) {
        final int len = s.length();

        // "111" is not a valid hex encoding.
        if (len % 2 != 0)
            throw new IllegalArgumentException("hexBinary needs to be even-length: " + s);

        byte[] out = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            int h = hexToBin(s.charAt(i));
            int l = hexToBin(s.charAt(i + 1));
            if (h == -1 || l == -1)
                throw new IllegalArgumentException("contains illegal character for hexBinary: " + s);

            out[i / 2] = (byte) (h * 16 + l);
        }

        return out;
    }

    public static int hexToBin(char ch) {
        if ('0' <= ch && ch <= '9') return ch - '0';
        if ('A' <= ch && ch <= 'F') return ch - 'A' + 10;
        if ('a' <= ch && ch <= 'f') return ch - 'a' + 10;
        return -1;
    }

    public static String asciiToHex(String asciiStr) {
        if (asciiStr.length() > 14) {
            asciiStr = asciiStr.substring(0, 14);
        }
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        byte[] buildBytes = new byte[15];
        buildBytes[0] = (byte) 0xff;
        int counter = 1;
        for (char ch : chars) {

            //buildBytes[counter]=(byte)Integer.toHexString((int) ch);
            hex.append(Integer.toHexString((int) ch));
            String numberInHex = Integer.toHexString((int) ch);

            Log.d("CHAR", "" + Integer.toHexString((int) ch) + " : " + buildBytes[counter]);
            counter++;
        }

        return hex.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if (digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: " + hexChar);
        }
        return digit;
    }
}
