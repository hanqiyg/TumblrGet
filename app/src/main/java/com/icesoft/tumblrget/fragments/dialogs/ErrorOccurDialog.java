package com.icesoft.tumblrget.fragments.dialogs;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.icesoft.tumblrget.R;


/**
 * Created by Administrator on 2018/5/8.
 */

public class ErrorOccurDialog extends DialogFragment
{
    private static final String BUNDLE_TITLE = "ErrorOccurDialog.bundle";
    private View rootView;
    private TextView tvMessage;
    private Button btnCancel;
    public static ErrorOccurDialog newInstance(String message)
    {
        ErrorOccurDialog fragment = new ErrorOccurDialog();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, message);
        fragment.setArguments(bundle);   //设置参数
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    getDialog().dismiss();
                    return true;
                }
                return false;
            }
        });
        if(null == rootView){
            rootView = inflater.inflate(R.layout.fragment_dialog_notfound_error,container,false);
            btnCancel  = rootView.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });
            tvMessage = rootView.findViewById(R.id.tvMessage);
        }
        Bundle bundle = getArguments();
        if(bundle != null)
        {
            String message = bundle.getString(BUNDLE_TITLE);
            tvMessage.setText(message);
        }else{
            tvMessage.setText("Unknow Error Occur.");
        }
        return rootView;
    }
}
