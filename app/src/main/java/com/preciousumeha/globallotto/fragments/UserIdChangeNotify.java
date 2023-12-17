package com.preciousumeha.globallotto.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.FlashActivity;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.db_others.ViewModel;

import static android.content.Context.MODE_PRIVATE;

public class UserIdChangeNotify extends BottomSheetDialogFragment {
    ViewModel viewModel;
    TextView user_name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.ThemeOverlay_Demo_BottomSheetDialog);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        View v =  inflater.inflate(R.layout.user_id_change_notify, container, false);
        user_name = v.findViewById(R.id.user_name);
        String s = "Dear "+getActivity().getSharedPreferences(Constants.preference_user_detail,MODE_PRIVATE).getString(Constants.ud_name,null);
        user_name.setText(s);
        v.findViewById(R.id.user_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSharedPreferences(Constants.preference_user_detail, MODE_PRIVATE).edit().clear().apply();
                getActivity().getSharedPreferences(Constants.preference_user_status, MODE_PRIVATE).edit().clear().apply();
                getActivity().getSharedPreferences(Constants.preference_permission, MODE_PRIVATE).edit().clear().apply();
                viewModel.deleteAllDeposit();
                viewModel.deleteAllResults();
                viewModel.deleteAllShareFundHistory();
                viewModel.deleteAllNotification();
                viewModel.deleteAllWithdrawal();
                viewModel.deleteAllUser();
                dismiss();
                startActivity(new Intent(getContext(), FlashActivity.class));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    getActivity().finishAffinity();
                }else {
                    ActivityCompat.finishAffinity(getActivity());
                }
            }
        });
        return v;
    }
}
