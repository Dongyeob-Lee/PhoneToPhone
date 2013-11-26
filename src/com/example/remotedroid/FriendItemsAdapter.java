package com.example.remotedroid;

import com.example.remotedroid.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FriendItemsAdapter extends ArrayAdapter<Friend>{

	Context context;
	int resourse;
	int textViewResourceId;
	Friend[] friends = null;
	public FriendItemsAdapter(Context context, int resource,
			int textViewResourceId, Friend[] objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.resourse=resource;
		this.textViewResourceId=textViewResourceId;
		this.friends=objects;
	}
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layoutFriendItem = null;
        
        //------------------------------------------------------------
        // ���̾ƿ��� �������� ������ ���� �����մϴ�.
        
        if (null != convertView) {
        	layoutFriendItem = (LinearLayout)convertView;
        }
        else {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutFriendItem = (LinearLayout)layoutInflater.inflate(resourse, null);
        }
        
        //------------------------------------------------------------
        // ������ ���̾ƿ��� �ڽ� ����� ������� �����մϴ�.
        
        //image �� ����� �Ǿ����� ���� �ƴ� ���� ����� ����..���⼭ ����..
      
        ImageView imageViewIcon
            = (ImageView)layoutFriendItem.findViewById(R.id.is_register_icon);
        TextView textViewFriendName
            = (TextView)layoutFriendItem.findViewById(R.id.friendName);
        TextView textViewFriendPhoneNumber
            = (TextView)layoutFriendItem.findViewById(R.id.textViewphonenumber);
        System.out.println("p:"+position);
        Friend currentFriendItem = friends[position];
       
        if(currentFriendItem.isRegistered()){
        	imageViewIcon.setImageResource(R.drawable.register_ok);
        }else{
        	imageViewIcon.setImageResource(R.drawable.register_no);
        }
        textViewFriendName.setText(currentFriendItem.getName());
        textViewFriendName.setTextColor(Color.parseColor("#701717"));
        textViewFriendPhoneNumber.setText(currentFriendItem.getPhonenumber());
        
        return (View)layoutFriendItem;
    }
}
