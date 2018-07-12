package com.jsl.ktv.karaok;

import com.jsl.ktv.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.Window;
import android.view.WindowManager;
import android.view.KeyEvent;
import android.util.Log;
import com.player.boxplayer.karaok.JNILib;
public class InputDialog2 extends Dialog
{
    // 定义回调事件，用于dialog的点击事�?  
	private String name;
    EditText etName;

    public InputDialog2(Context context, String name)
    {
        super(context);
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dig2);
        //setTitle(name);
        etName = (EditText) findViewById(R.id.edit_input2);
        Button clickBtn = (Button) findViewById(R.id.click_ok2);
		String str = JNILib.getCurrentInput();
		if(str!=null){
			etName.setText(str);
			if(str.length()<25)
			etName.setSelection(str.length());
			else
			etName.setSelection(25);	
		}
        clickBtn.setOnClickListener(clickListener);
        etName.addTextChangedListener(textWatcher);
		Window window = InputDialog2.this.getWindow(); 
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 0f;
		lp.x=300;
		lp.y=300;
		window.setAttributes(lp);
    }

    private View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            //这种方式是输入完然后再往下传
	   //JNILib.searchKey(etName.getText().toString());
	   //打印信息是一串字符串，没有问�?	   System.out.println("searchKey :"+ etName.getText().toString());
	   //JNILib.searchKey("end");		
	   InputDialog2.this.dismiss();
        }
    };
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
			InputDialog2.this.dismiss();
             JNILib.keyBack();
			Log.i("====JSL====", "KEYCODE_BACK");
        }
	return super.onKeyDown(keyCode, event);	
    }

    
    
    private TextWatcher textWatcher = new TextWatcher()
    {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count)
        {
            //System.out.println("-1-onTextChanged-->"
            //        + et_search.getText().toString() + "<--");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after)
        {
            //System.out.println("-2-beforeTextChanged-->"
            //        + et_search.getText().toString() + "<--");
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            //这种方式是每输入�?��字符就往下传，动态改�?            JNILib.searchKey(s.toString());
        }
    };
}