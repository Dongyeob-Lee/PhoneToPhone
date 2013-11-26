package com.example.remotedroid;

import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.widget.Toast;

public class TabListener<T extends Fragment> implements ActionBar.TabListener {

	private final Activity mActivity;
	private final String mTag;
	private final Class<T> mClass;
	private Fragment mFragment;

	public TabListener(Activity activity, String tag, Class<T> clz) {
		mActivity = activity;
		mTag = tag;
		mClass = clz;
 
		mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
		if (mFragment != null && !mFragment.isDetached()) {
			FragmentTransaction fragmentTransaction = mActivity
					.getFragmentManager().beginTransaction();
			fragmentTransaction.detach(mFragment);
			fragmentTransaction.commit();
		}
	}

	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		if (mFragment == null) {
			mFragment = Fragment.instantiate(mActivity, mClass.getName(), null);
			fragmentTransaction.add(android.R.id.content, mFragment, mTag);
		} else {
			fragmentTransaction.attach(mFragment);
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
		if (mFragment != null) {
			fragmentTransaction.detach(mFragment);
		}
	}

	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
		Toast.makeText(mActivity, "onTabReselected!", Toast.LENGTH_SHORT)
				.show();
	}
}
