package com.beessoft.dyyd.dailywork;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.beessoft.dyyd.BaseActivity;
import com.beessoft.dyyd.R;
import com.beessoft.dyyd.check.MapActivity;
import com.beessoft.dyyd.utils.Escape;
import com.beessoft.dyyd.utils.GetInfo;
import com.beessoft.dyyd.utils.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WorkQueryActivity extends BaseActivity {
	private TextView textView1, textView2, textView3, textView4, textView5,
			textView6, textView7;
	private String mac, id;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.workquery_actions, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_read:
			Intent intent = new Intent(this, ReadActivity.class);
			intent.putExtra("idTarget", id);
			startActivity(intent);
			return true;
		case R.id.action_mileage:
			Intent intent1 = new Intent(WorkQueryActivity.this,
					MapActivity.class);
			intent1.putExtra("id", id);
			intent1.putExtra("department", "");
			intent1.putExtra("person", "");
			intent1.putExtra("itype", "run");
			intent1.putExtra("itime", "");
			startActivity(intent1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workquery);

		textView1 = (TextView) findViewById(R.id.query_person);
		textView2 = (TextView) findViewById(R.id.query_outtime);
		textView3 = (TextView) findViewById(R.id.query_yester);
		textView4 = (TextView) findViewById(R.id.query_summary);
		textView5 = (TextView) findViewById(R.id.query_plan);
		textView6 = (TextView) findViewById(R.id.query_advise);
		textView7 = (TextView) findViewById(R.id.query_time);

		// 使textview可以scrollbars上下滚动
		// textView3.setMovementMethod(ScrollingMovementMethod.getInstance());

		// textView2.setInputType(InputType.TYPE_NULL);
		// textView3.setInputType(InputType.TYPE_NULL);
		// textView4.setInputType(InputType.TYPE_NULL);
		mac = GetInfo.getIMEI(this);
		id = getIntent().getStringExtra("idTarget");

		visitServer(WorkQueryActivity.this);

	}

	// 访问服务器http post
	private void visitServer(Context context) {
		String httpUrl = User.mainurl + "sf/fragment_check";
		AsyncHttpClient client_request = new AsyncHttpClient();
		RequestParams parameters_userInfo = new RequestParams();

		parameters_userInfo.put("mac", mac);
		parameters_userInfo.put("id", id);

		client_request.post(httpUrl, parameters_userInfo,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						System.out.println("response:" + response);
						try {
							JSONObject dataJson = new JSONObject(Escape
									.unescape(response));
							if (dataJson.getString("code").equals("0")) {
								JSONArray array = dataJson.getJSONArray("list");
								for (int i = 0; i < array.length(); i++) {
									JSONObject obj = array.getJSONObject(0);
									textView1.setText(new String(obj
											.getString("username")));
									textView2.setText(new String(obj
											.getString("cmakertime")));
									textView3.setText(new String(obj
											.getString("ytomplan")));
									textView4.setText(new String(obj
											.getString("todsummary")));
									textView5.setText(new String(obj
											.getString("tomplan")));
									textView6.setText(new String(obj
											.getString("veropinion")));
									textView7.setText(new String(obj
											.getString("checktime")));
								}
							} else {
								Toast.makeText(WorkQueryActivity.this, "无工作数据",
										Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}
}
