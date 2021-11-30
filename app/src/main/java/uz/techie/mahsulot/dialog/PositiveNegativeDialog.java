package uz.techie.mahsulot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import uz.techie.mahsulot.R;


public class PositiveNegativeDialog extends Dialog {
    TextView tvTitle, tvMessage;
    Button btnBack;
    ImageView imageView;
    PositiveNegativeListener listener;

    public PositiveNegativeDialog(Context context, PositiveNegativeListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_positive_negative);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        imageView = findViewById(R.id.positive_negative_image);
        tvTitle = findViewById(R.id.positive_negative_title);
        tvMessage = findViewById(R.id.positive_negative_text);
        btnBack = findViewById(R.id.positive_negative_btnOk);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onBackBtnClick();
            }
        });

    }


    public void setData(String title, String message, boolean isPositive){
        tvTitle.setText(title);
        tvMessage.setText(message);

        if (isPositive){
            imageView.setImageResource(R.drawable.success);
        }
        else {
            imageView.setImageResource(R.drawable.error);
        }

    }


    public interface PositiveNegativeListener{
        void onBackBtnClick();
    }

}
