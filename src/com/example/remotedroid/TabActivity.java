package com.example.remotedroid;

import java.util.ArrayList;

import com.example.remotedroid.R;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.Menu;
import android.widget.ListView;

public class TabActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        
        ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.addTab(bar
				.newTab()
				.setText("Friends")
				.setTabListener(
						new TabListener<FriendsTab>(this, "tab1",
								FriendsTab.class)));
		bar.addTab(bar
				.newTab()
				.setText("Remote Control")
				.setTabListener(
						new TabListener<RemoteTab>(this, "tab2",
								RemoteTab.class)));
		bar.addTab(bar
				.newTab()
				.setText("Share View")
				.setTabListener(
						new TabListener<ShareTab>(this, "tab3",
								ShareTab.class)));

		if (savedInstanceState != null) {
			bar.setSelectedNavigationItem(savedInstanceState.getInt(
					"selectedTab", 0));
		}
    }
 
    protected class MyTabsListener implements ActionBar.TabListener{

    	private Fragment fragment;
    	
		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			ft.add(R.id.fragment_place, fragment, null);
			if(tab.getText()=="Friends")
			{
				
			}
		}
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}
   
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_tab, menu);
        return true;
    }

}
