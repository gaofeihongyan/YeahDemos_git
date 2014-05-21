
package org.yeah.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.yeah.R;

public class CustomizeDialog extends Dialog implements android.view.View.OnClickListener {

    private Button btn_ok;
    private Button btn_cancel;
    private TextView tv_title;
    private TextView tv_message;

    public CustomizeDialog(Context context, String title, String message) {
        super(context);
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /** Design the dialog in main.xml file */
        setContentView(R.layout.customize_dialog);

        tv_title = ((TextView) findViewById(R.id.title));
        this.setUserTitle(title);

        tv_message = ((TextView) findViewById(R.id.message));
        this.setUserMessage(message);

        btn_ok = (Button) findViewById(R.id.ok_btn);
        btn_ok.setOnClickListener(this);
        btn_cancel = (Button) findViewById(R.id.cancel_btn);
        btn_cancel.setOnClickListener(this);
    }

    public void setUserTitle(String title) {
        tv_title.setText(title);
    }

    public void setUserMessage(String message) {
        tv_message.setText(message);
    }

    public void setPositiveButton(String text, android.view.View.OnClickListener listener) {
        btn_ok.setVisibility(View.VISIBLE);
        btn_ok.setText(text);
        btn_ok.setOnClickListener(listener);
    }

    public void setNegativeButton(String text, android.view.View.OnClickListener listener) {
        btn_cancel.setVisibility(View.VISIBLE);
        btn_cancel.setText(text);
        btn_cancel.setOnClickListener(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:
            case R.id.cancel_btn:
                dismiss();
                break;
        }

    }

}
