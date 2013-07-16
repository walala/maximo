package com.zt.maximo.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class DialogUtil {

	public static AlertDialog newAlertDialog(Context context,String title,String message){
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton("确定", null).create();
		dialog.setCancelable(false);
		return dialog;
	}
	
	public static AlertDialog newAlertDialog(Context context,String title,String message,String yesMsg,OnClickListener yesClickListener,String noMsg,OnClickListener noClickListener) {
		AlertDialog dialog = new AlertDialog.Builder(context)
							  .setTitle(title)
							  .setMessage(message)
							  .setPositiveButton(yesMsg, yesClickListener)
							  .setNegativeButton(noMsg, noClickListener).create();
		dialog.setCancelable(false);
		return dialog;
	}
}
