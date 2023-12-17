package com.preciousumeha.globallotto.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textview.MaterialTextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecieveFragment extends BottomSheetDialogFragment {
    MaterialTextView textViewwallet;
    ImageView wallet_img;

    public RecieveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL,R.style.ThemeOverlay_Demo_BottomSheetDialog);
        // Inflate the layout for this fragment
       View v =  inflater.inflate(R.layout.fragment_recieve, container, false);


        textViewwallet = v.findViewById(R.id.share_wallet);
        wallet_img = v.findViewById(R.id.share_img);
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.preference_user_detail, Context.MODE_PRIVATE);
        String wallet=preferences.getString(Constants.ud_wallet,null);
        String user_wallet = wallet + " my wallet id";
        textViewwallet.setText(user_wallet);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(wallet, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            wallet_img.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }





        return v;
    }

}
