package com.app.happy;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;

import com.app.happy.util.XmppTool;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class Register extends Activity {

	private XMPPConnection connect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		connect = new XmppTool().getConnection();

		Registration reg = new Registration();
		reg.setType(IQ.Type.SET);
		reg.setTo(connect.getServiceName());
		reg.setUsername("lexus18");// username.getText().toString());
		reg.setPassword("1");// password.getText().toString());
		reg.addAttribute("android", "geolo_createUser_android");
		System.out.println("reg:" + reg);
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				reg.getPacketID()), new PacketTypeFilter(IQ.class));
		PacketCollector collector = connect.createPacketCollector(filter);
		connect.sendPacket(reg);

		IQ result = (IQ) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		// Stop queuing results
		collector.cancel();
		if (result == null) {
			Toast.makeText(getApplicationContext(), "服务器没有返回结果", Toast.LENGTH_LONG)
					.show();
		} else if (result.getType() == IQ.Type.ERROR) {
			if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
				Toast.makeText(getApplicationContext(), "这个账号已经存在", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(getApplicationContext(), "恭喜你，注册失败", Toast.LENGTH_LONG)
						.show();
			}
		} else if (result.getType() == IQ.Type.RESULT) {
			System.out.println("good");
			Toast.makeText(getApplicationContext(), "预祝聊天愉快", Toast.LENGTH_LONG)
					.show();
		}

	}

}
