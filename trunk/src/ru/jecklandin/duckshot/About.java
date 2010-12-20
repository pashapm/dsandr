package ru.jecklandin.duckshot;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;  
import android.widget.TextView;

public class About extends Activity {
	
	private final String CREDITS_TEXT = "Evgeny Balandin, 2010";
	private final String CREDITS_TEXT2 = "Music: Shake That Little Foot - West Fork Gals";
	private final String LINKS_TEXT = "Send me a <a href=\"mailto:balandin.evgeny@gmail.com\">letter</a>"
		+ " or see my other <a href=\'market://search?q=pub:\"Jeck Landin\"\'>apps</a>.";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.about);
		
		TextView links = (TextView) findViewById(R.id.links_text);
		links.setText(Html.fromHtml(LINKS_TEXT));
		links.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView creds = (TextView) findViewById(R.id.creds);
		creds.setText(CREDITS_TEXT2);
	}
	
}
