package com.example.remotedroid;


import com.example.remotedroid.R;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class FriendsTab extends Fragment {

	ListView friendList;
	EditText etSearchFriend;
	ImageButton btSearch;
	
	private Context mContext;
		// 버튼이미지 연결~

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_friends_tab, container,
				false);
		etSearchFriend = (EditText) v.findViewById(R.id.etSearchFriend);
		friendList = (ListView) v.findViewById(R.id.friendlist);
		btSearch = (ImageButton)v.findViewById(R.id.friendSearchIcon);
		mContext = v.getContext();
		
		btSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//searchClick();
			}
		});
		Friendlist friendlist = new Friendlist(mContext);
		friendList.setAdapter(friendlist.getFriendItemsAdapter());
		friendList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long l_position) {
				// TODO Auto-generated method stub
				 Friend friend = (Friend)parent.getAdapter().getItem(position);
				 Toast.makeText(mContext, friend.getName(), Toast.LENGTH_SHORT).show();
			}
		});
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// ReadFromDB();

	}

}
