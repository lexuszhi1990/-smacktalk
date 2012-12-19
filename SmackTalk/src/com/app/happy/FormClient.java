package com.app.happy;

import java.util.ArrayList;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.happy.util.ChatContent;
import com.app.happy.util.ChatMsg;
import com.app.happy.util.TimeRender;
import com.app.happy.util.XmppTool;

public class FormClient extends Activity {

	private MyAdapter adapter;
	private ArrayList<ChatMsg> listMsg;
	private ListView listView;
	private String pUSERID;
	private String pTalkerID;
	private String talkMsgList;
	private EditText msgText;
	private XMPPConnection connect;
	private ChatContent chatContent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formclient);

		listMsg = new ArrayList<ChatMsg>();
		chatContent = new ChatContent();
		connect = new XmppTool().getConnection();
		this.msgText = (EditText) findViewById(R.id.formclient_text);
		this.listView = (ListView) findViewById(R.id.formclient_listview);
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		this.adapter = new MyAdapter(this);
		listView.setAdapter(adapter);

		pUSERID = getIntent().getStringExtra("USERID");
		pTalkerID = getIntent().getStringExtra("TALKID");
		talkMsgList = pTalkerID.substring(0, pTalkerID.indexOf("@"));
		listMsg = new ChatContent().getList(talkMsgList);

		System.out.println("talkID " + pTalkerID + "userid : " + pUSERID);

		// message listener
		ChatManager cm = connect.getChatManager();
		final Chat newchat = cm.createChat(pTalkerID, null);
		try {
			newchat.sendMessage("3dsa");
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// send message
		Button btsend = (Button) findViewById(R.id.formclient_btsend);
		btsend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String msg = msgText.getText().toString();
				if (msg.length() > 0) {
					new ChatContent().addChat(talkMsgList, new ChatMsg(pUSERID,
							msg, TimeRender.getDate(), "OUT"));
					mhandler.sendEmptyMessage(11);
					try {
						newchat.sendMessage(msg);
					} catch (XMPPException e) {
						e.printStackTrace();
					}
				}
				msgText.setText("");
			}
		});
		new RefreshUI().start();
	}

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 11:
				listMsg = new ChatContent().getList(talkMsgList);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				break;
			default:
				break;
			}
		};
	};

	public class RefreshUI extends Thread {

		public void run() {
			while (true) {
				mhandler.sendEmptyMessage(11);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	class MyAdapter extends BaseAdapter {

		private Context cxt;
		private LayoutInflater inflater;

		public MyAdapter(FormClient formClient) {
			this.cxt = formClient;
		}

		@Override
		public int getCount() {
			if (listMsg != null) {
				return listMsg.size();
			}
			return -1;
		}

		@Override
		public Object getItem(int position) {
			if (listMsg != null) {
				return listMsg.get(position);
			}
			return -1;
		}

		@Override
		public long getItemId(int position) {
			if (listMsg != null) {
				return position;
			}
			return -1;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			this.inflater = (LayoutInflater) this.cxt
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (listMsg.get(position).from.equals("IN")) {
				convertView = this.inflater.inflate(
						R.layout.formclient_chat_in, null);
			} else {
				convertView = this.inflater.inflate(
						R.layout.formclient_chat_out, null);
			}
			TextView useridView = (TextView) convertView
					.findViewById(R.id.formclient_row_userid);
			TextView dateView = (TextView) convertView
					.findViewById(R.id.formclient_row_date);
			TextView msgView = (TextView) convertView
					.findViewById(R.id.formclient_row_msg);
			useridView.setText(listMsg.get(position).userid);
			dateView.setText(listMsg.get(position).date);
			msgView.setText(listMsg.get(position).msg);
			return convertView;
		}
	}
}