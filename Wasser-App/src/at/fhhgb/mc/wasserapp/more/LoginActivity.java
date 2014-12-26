/*
 * 
 */
package at.fhhgb.mc.wasserapp.more;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import argo.jdom.JdomParser;
import argo.saj.InvalidSyntaxException;
import argo.staj.JsonStreamElement;
import argo.staj.JsonStreamElementType;
import argo.staj.StajParser;
import at.fhhgb.mc.wasserapp.mapactivity.MapActivity;
import at.fhhgb.mc.wasserapp.rssfeed.RssActivity;
import at.fhhgb.mc.wasserapp.rssfeed.WebViewActivity;
import at.fhhgb.mc.wasserapp.ChooseMarkerActivity;
import at.fhhgb.mc.wasserapp.HomeActivity;
import at.fhhgb.mc.wasserapp.R;

// TODO: Auto-generated Javadoc
/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity implements OnClickListener {

	/** The super user. */
	public static boolean superUser = true;

	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */

	private static ArrayList<String> list_of_credentials = new ArrayList<String>();
	
	/** The list_of_keys. */
	private static ArrayList<String> list_of_keys = new ArrayList<String>();

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	/** The m name. */
	private String mName;
	
	/** The m key. */
	private String mKey;

	// UI references.
	/** The m name view. */
	private EditText mNameView;
	
	/** The m key view. */
	private EditText mKeyView;
	
	/** The m login form view. */
	private View mLoginFormView;
	
	/** The m login status view. */
	private View mLoginStatusView;
	
	/** The m login status message view. */
	private TextView mLoginStatusMessageView;
	
	/** The m remove text name. */
	private Button mRemoveTextName;
	
	/** The m remove text key. */
	private Button mRemoveTextKey;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_login);
		setContentView(R.layout.actionbar);

		ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewflipper);
		vf.setDisplayedChild(10);
		overridePendingTransition(0, 0);

		HomeActivity.setAllButtonListener((ViewGroup)findViewById(R.id.rootActionbar), this);
		HomeActivity.setPositionToMark(this);


		// Set up the login form.
		mName = getIntent().getStringExtra(EXTRA_EMAIL);
		mNameView = (EditText) findViewById(R.id.email);
		mNameView.setText(mName);

		mKeyView = (EditText) findViewById(R.id.password);
		mKeyView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginFormView.setVisibility(View.VISIBLE);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusView.setVisibility(View.GONE);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		// ActionBar actionBar = getActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true);

		mRemoveTextName = (Button) findViewById(R.id.btn_login_removeName);
		mRemoveTextName.setOnClickListener(this);
		mRemoveTextKey = (Button) findViewById(R.id.btn_login_removeKey);
		mRemoveTextKey.setOnClickListener(this);
	}

	/**
	 * Fill list of login data.
	 */
	public void fillListOfLoginData() {

		// TODO: parse data from DB

		// fill list of keys
		String retrieveKeys = "";
		try {
			retrieveKeys = new RetrieveTaskKeys().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Set<String> fieldNames = new HashSet<String>();
		StajParser stajParser = null;
		stajParser = new StajParser(retrieveKeys);
		int counter = 0;
		// evaluate, how many entries are in the database
		while (stajParser.hasNext()) {
			JsonStreamElement next = stajParser.next();
			if (next.jsonStreamElementType() == JsonStreamElementType.START_FIELD) {
				fieldNames.add(next.text());
				counter++;
			}
		}

		for (int i = 0; i < counter - 1; i++) {
			try {
				list_of_keys.add(new JdomParser().parse(retrieveKeys)
						.getStringValue("keys", i, "key"));
			} catch (InvalidSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// fill list of credentials

		String retrieveUser = "";
		try {
			retrieveUser = new RetrieveTaskUser().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fieldNames = new HashSet<String>();
		stajParser = null;
		stajParser = new StajParser(retrieveUser);
		counter = 0;
		// evaluate, how many entries are in the database
		while (stajParser.hasNext()) {
			JsonStreamElement next = stajParser.next();
			if (next.jsonStreamElementType() == JsonStreamElementType.START_FIELD) {
				fieldNames.add(next.text());
				counter++;
			}
		}

		for (int i = 0; i < (counter - 1) / 2; i++) {
			try {
				String email = new JdomParser().parse(retrieveUser)
						.getStringValue("user", i, "email");
				String key = new JdomParser().parse(retrieveUser)
						.getStringValue("user", i, "key");
				list_of_credentials.add(email + ":" + key);
			} catch (InvalidSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * The Class RetrieveTaskUser.
	 */
	private class RetrieveTaskUser extends AsyncTask<Void, Void, String> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected String doInBackground(Void... params) {
			Log.e("retrieve", "entere do in background of retrieve task: ");

			String strUrl = "http://www.reecon.eu/ooewasser/api/v1/?request=retrieveUser";
			URL url = null;
			StringBuffer sb = new StringBuffer();
			try {
				url = new URL(strUrl);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.connect();
				InputStream iStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(iStream));
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
				iStream.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return sb.toString();
		}
	}

	/**
	 * The Class RetrieveTaskKeys.
	 */
	private class RetrieveTaskKeys extends AsyncTask<Void, Void, String> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected String doInBackground(Void... params) {
			Log.e("retrieve", "entere do in background of retrieve task: ");

			String strUrl = "http://www.reecon.eu/ooewasser/api/v1/?request=retrieveKeys";
			URL url = null;
			StringBuffer sb = new StringBuffer();
			try {
				url = new URL(strUrl);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.connect();
				InputStream iStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(iStream));
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
				iStream.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return sb.toString();
		}
	}

	/**
	 * The Class DeleteTaskKey.
	 */
	private class DeleteTaskKey extends AsyncTask<String, Void, Void> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Void doInBackground(String... params) {

			Log.e("send", "delete object from database");

			System.out.println(params[0]);

			String key = params[0];
			Log.d("key:", key);
			String strUrl = "http://www.reecon.eu/ooewasser/api/v1/?request=deleteKey";
			URL url = null;
			try {
				url = new URL(strUrl);

				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
						connection.getOutputStream());

				outputStreamWriter.write("key=" + key);
				outputStreamWriter.flush();
				outputStreamWriter.close();

				InputStream iStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(iStream));

				StringBuffer sb = new StringBuffer();
				Log.e("url", url.toString());

				String line = "";

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				Log.e("stringbuffer", sb.toString());

				reader.close();
				iStream.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

	}

	/**
	 * The Class SaveTaskUser.
	 */
	private class SaveTaskUser extends AsyncTask<String, Void, Void> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Void doInBackground(String... params) {

			Log.e("send", "send object to database");

			System.out.println(params[0]);

			String[] pieces = params[0].split(":");

			String email = pieces[0];
			String key = pieces[1];

			Log.d("email:", pieces[0]);
			Log.d("key:", pieces[1]);
			String strUrl = "http://www.reecon.eu/ooewasser/api/v1/?request=saveUser";
			URL url = null;
			try {
				url = new URL(strUrl);

				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
						connection.getOutputStream());

				outputStreamWriter.write("email=" + email + "&key=" + key);
				outputStreamWriter.flush();
				outputStreamWriter.close();

				InputStream iStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(iStream));

				StringBuffer sb = new StringBuffer();

				String line = "";

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				Log.e("save sb:", sb.toString());

				reader.close();
				iStream.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; goto parent activity.
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mNameView.setError(null);
		mKeyView.setError(null);

		// Store values at the time of the login attempt.
		mName = mNameView.getText().toString();
		mKey = mKeyView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mKey)) {
			mKeyView.setError(getString(R.string.error_field_required));
			focusView = mKeyView;
			cancel = true;
		} else if (mKey.length() < 4) {
			mKeyView.setError(getString(R.string.error_invalid_password));
			focusView = mKeyView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mName)) {
			mNameView.setError(getString(R.string.error_field_required));
			focusView = mNameView;
			cancel = true;
		} else if (!mName.contains("@")) {
			mNameView.setError(getString(R.string.error_invalid_email));
			focusView = mNameView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 *
	 * @param show the show
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.

		Log.i("show progress", "show progress");

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
			.alpha(show ? 1 : 0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginStatusView.setVisibility(show ? View.VISIBLE
							: View.GONE);
				}
			});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
			.alpha(show ? 0 : 1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginFormView.setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			// new user?
			for (String keys : list_of_keys) {
				if (keys.equals(mKey)) {
					list_of_credentials.add(mName + ":" + mKey);
					new SaveTaskUser().execute(mName + ":" + mKey);
					list_of_keys.remove(keys);
					new DeleteTaskKey().execute(keys);
					return true;
				}
			}

			for (String credential : list_of_credentials) {
				String[] pieces = credential.split(":");
				if (pieces[0].toUpperCase().equals(mName.toUpperCase())) {
					return pieces[1].equals(mKey);
				}
			}

			return false;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				superUser = true;
				Toast.makeText(getApplicationContext(), "login successful",
						2000).show();
				Intent i = new Intent(getApplicationContext(), ChooseMarkerActivity.class);
				startActivity(i);				
				finish();
			} else {
				mKeyView.setError(getString(R.string.error_incorrect_password));
				mKeyView.requestFocus();
			}
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View _v) {
		Intent i = new Intent();
		switch (_v.getId()) {

		//Actionbar
		case R.id.b_home:
			i = new Intent(this, HomeActivity.class);
			break;
		case R.id.b_position:
			i = new Intent();
			Intent map;
			if(LoginActivity.superUser){
				map = new Intent(this, ChooseMarkerActivity.class);
				map.putExtra("user", true);
			} else{
				map = new Intent(this, MapActivity.class);
				map.putExtra("user", false);	
				map.putExtra("m_markertype", "all");
			}
			startActivity(map);
			break;
		case R.id.b_news:
			i = new Intent(this, WebViewActivity.class);
			break;
		case R.id.b_more:
			i = new Intent(this, MoreActivity.class);
			break;
			//End Actionbar

		case R.id.btn_login_removeName:
			mNameView.setText("");
			break;
		case R.id.btn_login_removeKey:
			mKeyView.setText("");
			break;
		case R.id.sign_in_button:
			fillListOfLoginData();
			attemptLogin();
			break;
		case R.id.b_back:
			i = new Intent();
			onBackPressed();
			break;
		}
		if(i != null && i.getComponent() != null){
				i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);	
		}
	}
}
