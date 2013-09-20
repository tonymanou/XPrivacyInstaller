package biz.bokhorst.xprivacy.installer;

import biz.bokhorstxprivacy.installer.R;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;

public class ActivityMain extends Activity {

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Uri inputUri = Uri.parse(intent.getDataString());
			if (inputUri.getScheme().equals("package")) {
				String packageName = inputUri.getSchemeSpecificPart();
				if ("de.robv.android.xposed.installer".equals(packageName)) {
					CheckBox cbXposed = (CheckBox) findViewById(R.id.cbXposedInstalled);
					cbXposed.setChecked(true);
				} else if ("biz.bokhorst.xprivacy".equals(packageName)) {
					CheckBox cbXPrivacy = (CheckBox) findViewById(R.id.cbXPrivacy);
					cbXPrivacy.setChecked(true);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Register for new package notifications
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		intentFilter.addDataScheme("package");
		registerReceiver(mReceiver, intentFilter);

		// Reference controls
		CheckBox cbAndroid = (CheckBox) findViewById(R.id.cbAndroid);
		TextView tvAndroid = (TextView) findViewById(R.id.tvAndroid);
		Button btnWhatIs = (Button) findViewById(R.id.btnWhatIs);
		LinearLayout llRoot = (LinearLayout) findViewById(R.id.llRoot);
		CheckBox cbRoot = (CheckBox) findViewById(R.id.cbRoot);
		Button btnRoot = (Button) findViewById(R.id.btnRoot);
		LinearLayout llBackup = (LinearLayout) findViewById(R.id.llBackup);
		CheckBox cbBackup = (CheckBox) findViewById(R.id.cbBackup);
		final LinearLayout llSettings = (LinearLayout) findViewById(R.id.llSettings);
		final CheckBox cbSettings = (CheckBox) findViewById(R.id.cbSettings);
		final Button btnSettings = (Button) findViewById(R.id.btnSettings);
		final LinearLayout llXposedInstalled = (LinearLayout) findViewById(R.id.llXposedInstalled);
		final CheckBox cbXposedInstalled = (CheckBox) findViewById(R.id.cbXposedInstalled);
		final Button btnXposedInstalled = (Button) findViewById(R.id.btnXposedInstalled);
		final LinearLayout llXPosedEnabled = (LinearLayout) findViewById(R.id.llXPosedEnabled);
		final CheckBox cbXposedEnabled = (CheckBox) findViewById(R.id.cbXposedEnabled);
		final Button btnXposedEnabled = (Button) findViewById(R.id.btnXposedEnabled);
		final LinearLayout llXPrivacy = (LinearLayout) findViewById(R.id.llXPrivacy);
		final CheckBox cbXPrivacy = (CheckBox) findViewById(R.id.cbXPrivacy);
		final Button btnXPrivacy = (Button) findViewById(R.id.btnXPrivacy);
		final LinearLayout llEnabled = (LinearLayout) findViewById(R.id.llEnabled);
		final CheckBox cbEnabled = (CheckBox) findViewById(R.id.cbEnabled);
		final Button btnEnabled = (Button) findViewById(R.id.btnEnabled);
		final Button btnReboot = (Button) findViewById(R.id.btnReboot);
		final Button btnHelp = (Button) findViewById(R.id.btnHelp);

		// What is? help
		View.OnClickListener xda = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://forum.xda-developers.com/showthread.php?t=2320783"));
				startActivity(intent);
			}
		};
		btnWhatIs.setOnClickListener(xda);
		btnHelp.setOnClickListener(xda);

		// Android version
		cbAndroid.setChecked(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			tvAndroid.setVisibility(View.VISIBLE);
		else {
			// Root
			boolean root = RootTools.isAccessGiven();
			cbRoot.setChecked(root);
			if (root)
				llBackup.setVisibility(View.VISIBLE);
			else
				btnRoot.setVisibility(View.VISIBLE);
			llRoot.setVisibility(View.VISIBLE);
		}

		// Root
		btnRoot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://www.androidcentral.com/root"));
				startActivity(intent);
			}
		});

		// Backup
		cbBackup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked)
					btnSettings.setVisibility(cbSettings.isChecked() ? View.GONE : View.VISIBLE);
				llSettings.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		// Security settings
		cbSettings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnSettings.setVisibility(isChecked ? View.GONE : View.VISIBLE);
				if (isChecked) {
					boolean installed = isInstalled("de.robv.android.xposed.installer");
					cbXposedInstalled.setChecked(installed);
					btnXposedInstalled.setVisibility(installed ? View.GONE : View.VISIBLE);
				}
				llXposedInstalled.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		btnSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
				startActivity(intent);
			}
		});

		// Xposed installed
		cbXposedInstalled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnXposedInstalled.setVisibility(isChecked ? View.GONE : View.VISIBLE);
				if (isChecked)
					btnXposedEnabled.setVisibility(cbXposedEnabled.isChecked() ? View.GONE : View.VISIBLE);
				llXPosedEnabled.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		btnXposedInstalled.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://forum.xda-developers.com/showthread.php?t=1574401"));
				startActivity(intent);
			}
		});

		// Xposed enabled
		cbXposedEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnXposedEnabled.setVisibility(isChecked ? View.GONE : View.VISIBLE);
				if (isChecked) {
					boolean installed = isInstalled("biz.bokhorst.xprivacy");
					cbXPrivacy.setChecked(installed);
					btnXPrivacy.setVisibility(installed ? View.GONE : View.VISIBLE);
				}
				llXPrivacy.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		btnXposedEnabled.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getPackageManager().getLaunchIntentForPackage("de.robv.android.xposed.installer");
				if (intent != null)
					startActivity(intent);
			}
		});

		// XPrivacy installed
		cbXPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnXPrivacy.setVisibility(isChecked ? View.GONE : View.VISIBLE);
				if (isChecked)
					btnEnabled.setVisibility(cbEnabled.isChecked() ? View.GONE : View.VISIBLE);
				llEnabled.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		btnXPrivacy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://repo.xposed.info/module/biz.bokhorst.xprivacy"));
				startActivity(intent);
			}
		});

		// XPrivacy enabled
		cbEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnEnabled.setVisibility(isChecked ? View.GONE : View.VISIBLE);
				btnReboot.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		btnEnabled.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getPackageManager().getLaunchIntentForPackage("de.robv.android.xposed.installer");
				if (intent != null)
					startActivity(intent);
			}
		});

		btnReboot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					CommandCapture command = new CommandCapture(0, "reboot now");
					RootTools.getShell(true).add(command);
				} catch (Throwable ex) {
				}
			}
		});
	}

	public boolean isInstalled(String packageName) {
		try {
			getPackageManager().getPackageInfo(packageName, 0);
			return true;
		} catch (NameNotFoundException ignored) {
			return false;
		} catch (Throwable ex) {
			return false;
		}
	}
}
