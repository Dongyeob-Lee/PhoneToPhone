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
        // 레이아웃이 존재하지 않으면 새로 생성합니다.
        
        if (null != convertView) {
        	layoutFriendItem = (LinearLayout)convertView;
        }
        else {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutFriendItem = (LinearLayout)layoutInflater.inflate(resourse, null);
        }
        
        //------------------------------------------------------------
        // 리턴할 레이아웃의 자식 뷰들의 내용들을 설정합니다.
        
        //image 는 등록이 되었있을 경우와 아닌 경우로 나누어서 설정..여기서 말고..
      
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
