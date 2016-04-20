package com.patrykkrawczyk.liveo;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Patryk Krawczyk on 09.04.2016.
 */
public class NfcReader {//implements NfcAdapter.ReaderCallback {

    private static NfcAdapter mNfcAdapter;
    private static NfcReader  mInstance;


    private NfcReader(Activity activity) {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);
    }

    public static synchronized NfcReader getInstance(Activity activity) {
        if (mInstance == null) {
            mInstance = new NfcReader(activity);
        }

        return mInstance;
    }


    public void enableNfcReader(Activity activity) {
        //mNfcAdapter.enableReaderMode(activity, this, mNfcAdapter.FLAG_READER_NFC_A | mNfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
    }

    public void disableNfcReader(Activity activity) {
        //mNfcAdapter.disableReaderMode(activity);
    }


    //@Override
    public void onTagDiscovered(Tag tag) {
       // String serial = tagIdToString(tag);
        //EventBus.getDefault().post(new NfcSerialDetected(serial));
    }

    private String tagIdToString(Tag tag) {
        byte[] byteId = tag.getId();

        final StringBuilder builder = new StringBuilder();
        for (byte b : byteId) {
            builder.append(String.format("%02X", b));
            builder.append(':');
        }

        return builder.toString().substring(0, builder.length()-1);
    }
}