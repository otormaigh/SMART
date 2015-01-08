package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
	private TextView main_View;
	private Button button_1;
	private Button button_2;
    private Button button_3;
	private Button button_4;
	private Button button_5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myapplayout);
		
		main_View = (TextView)findViewById(R.id.main_View);
		button_1 = (Button)findViewById(R.id.button_1);
		button_1.setOnClickListener(new ButtonListener());
		button_2 = (Button)findViewById(R.id.button_2);
		button_2.setOnClickListener(new ButtonListener());
		button_3 = (Button)findViewById(R.id.button_3);
		button_3.setOnClickListener(new ButtonListener());
		button_4 = (Button)findViewById(R.id.button_4);
		button_4.setOnClickListener(new ButtonListener());
		button_5 = (Button)findViewById(R.id.button_5);
		button_5.setOnClickListener(new ButtonListener());
		main_View.setText("Welcome!");
	}

	private class ButtonListener implements View.OnClickListener {
		
		public void onClick(View v) {
			
			switch(v.getId()) {
			
			case R.id.button_1 :
				System.out.println("I was pressed");
				break;
			case R.id.button_2 :
				break;
			case R.id.button_3 :
				break;
			case R.id.button_4 :
				break;
			case R.id.button_5 :
				System.out.println("I was pressed");
				break;
			}
		}
	}

}
