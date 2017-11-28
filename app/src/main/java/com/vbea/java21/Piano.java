package com.vbea.java21;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class Piano extends AppCompatActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.piano);
		//ImageButton back = (ImageButton) findViewById(R.id.btn_back);
		init();
		/*back.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				finishAfterTransition();
			}
		});*/
	}
	
	public void init()
	{
		//A~A16
		Button btnA = (Button) findViewById(R.id.pianoA);//1
		btnA.setOnTouchListener(new MyPlay());
		Button btnA1 = (Button) findViewById(R.id.pianoA1);//2
		btnA1.setOnTouchListener(new MyPlay());
		Button btnA2 = (Button) findViewById(R.id.pianoA2);//3
		btnA2.setOnTouchListener(new MyPlay());
		Button btnA3 = (Button) findViewById(R.id.pianoA3);//4
		btnA3.setOnTouchListener(new MyPlay());
		Button btnA4 = (Button) findViewById(R.id.pianoA4);//5
		btnA4.setOnTouchListener(new MyPlay());
		Button btnA5 = (Button) findViewById(R.id.pianoA5);//6
		btnA5.setOnTouchListener(new MyPlay());
		Button btnA6 = (Button) findViewById(R.id.pianoA6);//7
		btnA6.setOnTouchListener(new MyPlay());
		Button btnA7 = (Button) findViewById(R.id.pianoA7);//8
		btnA7.setOnTouchListener(new MyPlay());
		Button btnA8 = (Button) findViewById(R.id.pianoA8);//9
		btnA8.setOnTouchListener(new MyPlay());
		Button btnA9 = (Button) findViewById(R.id.pianoA9);//10
		btnA9.setOnTouchListener(new MyPlay());
		Button btnA10 = (Button) findViewById(R.id.pianoA10);//11
		btnA10.setOnTouchListener(new MyPlay());
		Button btnA11 = (Button) findViewById(R.id.pianoA11);//12
		btnA11.setOnTouchListener(new MyPlay());
		Button btnA12 = (Button) findViewById(R.id.pianoA12);//13
		btnA12.setOnTouchListener(new MyPlay());
		Button btnA13 = (Button) findViewById(R.id.pianoA13);//14
		btnA13.setOnTouchListener(new MyPlay());
		Button btnA14 = (Button) findViewById(R.id.pianoA14);//15
		btnA14.setOnTouchListener(new MyPlay());
		Button btnA15 = (Button) findViewById(R.id.pianoA15);//16
		btnA15.setOnTouchListener(new MyPlay());
		Button btnA16 = (Button) findViewById(R.id.pianoA16);//17
		btnA16.setOnTouchListener(new MyPlay());
		//B~B10
		Button btnB = (Button) findViewById(R.id.pianoB);//18
		btnB.setOnTouchListener(new MyPlay());
		Button btnB1 = (Button) findViewById(R.id.pianoB1);//19
		btnB1.setOnTouchListener(new MyPlay());
		Button btnB2 = (Button) findViewById(R.id.pianoB2);//20
		btnB2.setOnTouchListener(new MyPlay());
		Button btnB3 = (Button) findViewById(R.id.pianoB3);//21
		btnB3.setOnTouchListener(new MyPlay());
		Button btnB4 = (Button) findViewById(R.id.pianoB4);//22
		btnB4.setOnTouchListener(new MyPlay());
		Button btnB5 = (Button) findViewById(R.id.pianoB5);//23
		btnB5.setOnTouchListener(new MyPlay());
		Button btnB6 = (Button) findViewById(R.id.pianoB6);//24
		btnB6.setOnTouchListener(new MyPlay());
		Button btnB7 = (Button) findViewById(R.id.pianoB7);//25
		btnB7.setOnTouchListener(new MyPlay());
		Button btnB8 = (Button) findViewById(R.id.pianoB8);//26
		btnB8.setOnTouchListener(new MyPlay());
		Button btnB9 = (Button) findViewById(R.id.pianoB9);//27
		btnB9.setOnTouchListener(new MyPlay());
		Button btnB10 = (Button) findViewById(R.id.pianoB10);//28
		btnB10.setOnTouchListener(new MyPlay());
		//C~C36
		Button btnC = (Button) findViewById(R.id.pianoC);//29
		btnC.setOnTouchListener(new MyPlay());
		Button btnC1 = (Button) findViewById(R.id.pianoC1);//30
		btnC1.setOnTouchListener(new MyPlay());
		Button btnC2 = (Button) findViewById(R.id.pianoC2);//31
		btnC2.setOnTouchListener(new MyPlay());
		Button btnC3 = (Button) findViewById(R.id.pianoC3);//32
		btnC3.setOnTouchListener(new MyPlay());
		Button btnC4 = (Button) findViewById(R.id.pianoC4);//33
		btnC4.setOnTouchListener(new MyPlay());
		Button btnC5 = (Button) findViewById(R.id.pianoC5);//34
		btnC5.setOnTouchListener(new MyPlay());
		Button btnC6 = (Button) findViewById(R.id.pianoC6);//35
		btnC6.setOnTouchListener(new MyPlay());
		Button btnC7 = (Button) findViewById(R.id.pianoC7);//36
		btnC7.setOnTouchListener(new MyPlay());
		Button btnC8 = (Button) findViewById(R.id.pianoC8);//37
		btnC8.setOnTouchListener(new MyPlay());
		Button btnC9 = (Button) findViewById(R.id.pianoC9);//38
		btnC9.setOnTouchListener(new MyPlay());
		Button btnC10 = (Button) findViewById(R.id.pianoC10);//39
		btnC10.setOnTouchListener(new MyPlay());
		Button btnC11 = (Button) findViewById(R.id.pianoC11);//40
		btnC11.setOnTouchListener(new MyPlay());
		Button btnC12 = (Button) findViewById(R.id.pianoC12);//41
		btnC12.setOnTouchListener(new MyPlay());
		Button btnC13 = (Button) findViewById(R.id.pianoC13);//42
		btnC13.setOnTouchListener(new MyPlay());
		Button btnC14 = (Button) findViewById(R.id.pianoC14);//43
		btnC14.setOnTouchListener(new MyPlay());
		Button btnC15 = (Button) findViewById(R.id.pianoC15);//44
		btnC15.setOnTouchListener(new MyPlay());
		Button btnC16 = (Button) findViewById(R.id.pianoC16);//45
		btnC16.setOnTouchListener(new MyPlay());
		Button btnC17 = (Button) findViewById(R.id.pianoC17);//46
		btnC17.setOnTouchListener(new MyPlay());
		Button btnC18 = (Button) findViewById(R.id.pianoC18);//47
		btnC18.setOnTouchListener(new MyPlay());
		Button btnC19 = (Button) findViewById(R.id.pianoC19);//48
		btnC19.setOnTouchListener(new MyPlay());
		Button btnC20 = (Button) findViewById(R.id.pianoC20);//49
		btnC20.setOnTouchListener(new MyPlay());
		Button btnC21 = (Button) findViewById(R.id.pianoC21);//50
		btnC21.setOnTouchListener(new MyPlay());
		Button btnC22 = (Button) findViewById(R.id.pianoC22);//51
		btnC22.setOnTouchListener(new MyPlay());
		Button btnC23 = (Button) findViewById(R.id.pianoC23);//52
		btnC23.setOnTouchListener(new MyPlay());
		Button btnC24 = (Button) findViewById(R.id.pianoC24);//53
		btnC24.setOnTouchListener(new MyPlay());
		Button btnC25 = (Button) findViewById(R.id.pianoC25);//54
		btnC25.setOnTouchListener(new MyPlay());
		Button btnC26 = (Button) findViewById(R.id.pianoC26);//55
		btnC26.setOnTouchListener(new MyPlay());
		Button btnC27 = (Button) findViewById(R.id.pianoC27);//56
		btnC27.setOnTouchListener(new MyPlay());
		Button btnC28 = (Button) findViewById(R.id.pianoC28);//57
		btnC28.setOnTouchListener(new MyPlay());
		Button btnC29 = (Button) findViewById(R.id.pianoC29);//58
		btnC29.setOnTouchListener(new MyPlay());
		Button btnC30 = (Button) findViewById(R.id.pianoC30);//59
		btnC30.setOnTouchListener(new MyPlay());
		Button btnC31 = (Button) findViewById(R.id.pianoC31);//60
		btnC31.setOnTouchListener(new MyPlay());
		Button btnC32 = (Button) findViewById(R.id.pianoC32);//61
		btnC32.setOnTouchListener(new MyPlay());
		Button btnC33 = (Button) findViewById(R.id.pianoC33);//62
		btnC33.setOnTouchListener(new MyPlay());
		Button btnC34 = (Button) findViewById(R.id.pianoC34);//63
		btnC34.setOnTouchListener(new MyPlay());
		Button btnC35 = (Button) findViewById(R.id.pianoC35);//64
		btnC35.setOnTouchListener(new MyPlay());
		Button btnC36 = (Button) findViewById(R.id.pianoC36);//65
		btnC36.setOnTouchListener(new MyPlay());
		//D~Z
		Button btnD = (Button) findViewById(R.id.pianoD);//66
		btnD.setOnTouchListener(new MyPlay());
		Button btnE = (Button) findViewById(R.id.pianoE);//67
		btnE.setOnTouchListener(new MyPlay());
		Button btnF = (Button) findViewById(R.id.pianoF);//68
		btnF.setOnTouchListener(new MyPlay());
		Button btnG = (Button) findViewById(R.id.pianoG);//69
		btnG.setOnTouchListener(new MyPlay());
		Button btnH = (Button) findViewById(R.id.pianoH);//70
		btnH.setOnTouchListener(new MyPlay());
		Button btnI = (Button) findViewById(R.id.pianoI);//71
		btnI.setOnTouchListener(new MyPlay());
		Button btnJ = (Button) findViewById(R.id.pianoJ);//72
		btnJ.setOnTouchListener(new MyPlay());
		Button btnK = (Button) findViewById(R.id.pianoK);//73
		btnK.setOnTouchListener(new MyPlay());
		Button btnL = (Button) findViewById(R.id.pianoL);//74
		btnL.setOnTouchListener(new MyPlay());
		Button btnM = (Button) findViewById(R.id.pianoM);//75
		btnM.setOnTouchListener(new MyPlay());
		Button btnN = (Button) findViewById(R.id.pianoN);//76
		btnN.setOnTouchListener(new MyPlay());
		Button btnO = (Button) findViewById(R.id.pianoO);//77
		btnO.setOnTouchListener(new MyPlay());
		Button btnP = (Button) findViewById(R.id.pianoP);//78
		btnP.setOnTouchListener(new MyPlay());
		Button btnQ = (Button) findViewById(R.id.pianoQ);//79
		btnQ.setOnTouchListener(new MyPlay());
		Button btnW = (Button) findViewById(R.id.pianoR);//80
		btnW.setOnTouchListener(new MyPlay());
		Button btnR = (Button) findViewById(R.id.pianoS);//81
		btnR.setOnTouchListener(new MyPlay());
		Button btnS = (Button) findViewById(R.id.pianoT);//82
		btnS.setOnTouchListener(new MyPlay());
		Button btnT = (Button) findViewById(R.id.pianoU);//83
		btnT.setOnTouchListener(new MyPlay());
		Button btnU = (Button) findViewById(R.id.pianoV);//84
		btnU.setOnTouchListener(new MyPlay());
		Button btnV = (Button) findViewById(R.id.pianoW);//85
		btnV.setOnTouchListener(new MyPlay());
		Button btnX = (Button) findViewById(R.id.pianoX);//86
		btnX.setOnTouchListener(new MyPlay());
		Button btnY = (Button) findViewById(R.id.pianoY);//87
		btnY.setOnTouchListener(new MyPlay());
		Button btnZ = (Button) findViewById(R.id.pianoZ);//88
		btnZ.setOnTouchListener(new MyPlay());
	}
	
	class MyPlay implements View.OnTouchListener
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				try
				{
					Common.SOUND.play(((Button)v).getText().toString());
				}
				catch (Exception e)
				{
					ExceptionHandler.log("PianoTouched", e.getMessage());
				}
			}
			//if (event.getAction() == MotionEvent.ACTION_MOVE)
				//v.setFocusableInTouchMode(true);
			return false;
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
}
