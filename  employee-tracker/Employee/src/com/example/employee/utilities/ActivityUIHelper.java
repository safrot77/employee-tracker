package com.example.employee.utilities;

import java.util.Stack;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.employee.R;


public class ActivityUIHelper extends Activity {

	private static ProgressDialog progressDialog;

	public static final String TITLE_KEY = "title";
	public static final String MESSAGE_KEY = "message";
	public static final String ICON_KEY = "icon";
	public static final String LEADS_OFF_KEY = "leadsOff";

	// public static Vector<Class<? extends Activity>> connectedActivities = new
	// Vector<Class<? extends Activity>>();
	public static Vector<Intent> connectedActivities = new Vector<Intent>();
	public static final int MAX_AVAILABLE_CONNECTIONS = 2;
	public static final String FINGER_PRINT_KEY = "finger_print";
	Stack<Intent> stack;
	AlertDialog.Builder dialogBuilder;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		Intent intent = getIntent();
		stack = (Stack<Intent>) getLastNonConfigurationInstance();
		if (stack == null)
			stack = new Stack<Intent>();
		stack.push(intent);
		Log.d("after on create push stack size : ", "" + stack.size());

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!stack.isEmpty()) {
			Intent intent = stack.lastElement();
			Bundle bundle = intent.getExtras();
			String message = bundle.getString(MESSAGE_KEY);
			int title = bundle.getInt(TITLE_KEY);
			int icon = bundle.getInt(ICON_KEY);
			if (dialogBuilder != null) {
				dialogBuilder.create().dismiss();
			}
			showAlertDialogFromStack(title, icon, message);
		}
	}

	/**
	 * Shows adialog whose data in the top intent in the stack
	 * 
	 * @param title
	 * @param icon
	 * @param msg
	 */
	private void showAlertDialogFromStack(int title, int icon, String msg) {
		dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(title);
		dialogBuilder.setIcon(icon);
		dialogBuilder.setMessage(msg);
		if (stack.lastElement().getExtras().getInt(LEADS_OFF_KEY) == 0) {
			dialogBuilder.setNeutralButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						// click listener on the alert box
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							dismissAndDisplayBack();
						}

					});
		} else {
			dialogBuilder.setCancelable(true);
			dialogBuilder.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
		}
		new Handler(this.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				dialogBuilder.create().show();
			}
		});
	}

	private void dismissAndDisplayBack() {
		if (dialogBuilder != null) {
			dialogBuilder.create().dismiss();
		}
		stack.pop();
		Log.d("on dismiss removing top ", "stack size : " + stack.size());
		Log.d("on dismiss removing top ", "stack isempty : " + stack.isEmpty());
		if (!stack.isEmpty()) {
			Log.d("on dismiss removing top ", "stack isempty : insideif");

			Intent intent = stack.lastElement();
			Bundle bundle = intent.getExtras();
			String message = bundle.getString(MESSAGE_KEY);
			int title = bundle.getInt(TITLE_KEY);
			int icon = bundle.getInt(ICON_KEY);
			showAlertDialogFromStack(title, icon, message);
		} else {
			Log.d("on dismiss removing top ", "stack isempty : inside else");
			finish();
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return stack;
	}

	// /===========================Static
	// Methods==================================\\\\\
	/**
	 * this method display an Alert Dialog with Ok button
	 * 
	 * @param title
	 *            title of the Alert Dialog
	 * @param icon
	 *            icon of the Alert Dialog
	 * @param msg
	 *            the message that will be shown in the body of the dialog
	 * @param parentActivity
	 *            the context in which the dialog will be displayed
	 * @return TODO
	 */
	public static AlertDialog showAlertDialog(int title, int icon, String msg,
			Context parentActivity) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
				parentActivity);
		dialogBuilder.setTitle(title);
		dialogBuilder.setIcon(icon);
		dialogBuilder.setMessage(msg);
		final AlertDialog dialog = dialogBuilder.create();
		dialogBuilder.setNeutralButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					// click listener on the alert box
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						dialog.dismiss();
					}
				});
		new Handler(parentActivity.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});
		return dialog;
	}

	public ActivityUIHelper() {
		super();
	}

	/**
	 * this method shows an Error dialog like the Alert one in the previous
	 * method
	 * 
	 * @param message
	 * @param parentActivity
	 */
	public static void showErrorDialog(String message, Context parentActivity) {
		showAlertDialog(R.string.error,  R.drawable.error, message,
				parentActivity);
	}

	/**
	 * this method display an Alert Dialog with Ok button and finishes the
	 * activity when ok pressed
	 * 
	 * @param title
	 *            title of the Alert Dialog
	 * @param icon
	 *            icon of the Alert Dialog
	 * @param msg
	 *            the message that will be shown in the body of the dialog
	 * @param parentActivity
	 *            the context in which the dialog will be displayed
	 */
	public static void showAlertDialog(int title, int icon, String msg,
			Context parentActivity, boolean finishActivity) {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
				parentActivity);
		final boolean finish = finishActivity;
		final Context activity = parentActivity;
		dialogBuilder.setTitle(title);
		dialogBuilder.setIcon(icon);
		dialogBuilder.setMessage(msg);
		dialogBuilder.setNeutralButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					// click listener on the alert box
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (finish) {
							dialogBuilder.create().dismiss();
							((Activity) activity).finish();
						}
					}
				});
		new Handler(parentActivity.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				dialogBuilder.create().show();
			}
		});
	}

	/**
	 * @deprecated Use {@link #showProgressDialog(Context)} instead
	 */
	@Deprecated
	public static void showProgressDialog(Context parentActivity,
			boolean finishActivity) {
		showProgressDialog(parentActivity);
	}

	/**
	 * shows a progress dialog over the given activity
	 * 
	 * @param parentActivity
	 *            for the dialog to be dispalayed over
	 */
	public static void showProgressDialog(Context parentActivity) {
		Log.v("Progress: ", "------progress----------");
		int title = R.string.loading;
		int msg = R.string.please_wait;
		progressDialog = ProgressDialog.show(parentActivity,
				parentActivity.getString(title), parentActivity.getString(msg),
				true);
	}

	public static void dismissProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

	public static void checkInput(AlertDialog dialog, final EditText field) {
		final Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
		button.setEnabled(false);
		field.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (field.getText() == null || "".equals(field.getText())) {
					button.setEnabled(false);
				} else {
					button.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

}
