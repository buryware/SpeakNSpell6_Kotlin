//
//        Steven Otto Stansbury
//             Buryware 
//
//        Copyright 10/7/2010
//

package buryware.speak.spell6

import java.util.ArrayList

import buryware.speak.spell6.R
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

class BurywareSpeakNSpell : Activity() {

    //private EditText mSavedNote;
    private var mUserChoices: ListView? = null

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        //mSavedNote = (EditText) findViewById(R.string.LastSavedNote);
        mUserChoices = findViewById(R.id.user_choices) as ListView

        /*	mUserChoices.setOnClickListener(new OnClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id)
		    {
				for (int i = mUserChoices.getCount(); i > 0; i--) {
					if (i != position) {
				//		mUserChoices.removeViewAt(i);
					}

				}
		   //   String selectedFromList = (mUserChoices.getItemAtPosition(position).toString());
		    }

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}});*/

        // Set message to be appropriate for this screen.
        (findViewById(R.id.note_text) as TextView).setText(R.string.SavedNote)

        // Retrieve the last whose state we will save.
        //mSavedNote = (EditText) findViewById(R.id.note_text);

        Toast.makeText(this, "Welcome to Buryware's SpeakNSpell Kotlin!", Toast.LENGTH_SHORT).show()

        launchMicrophoneInputView()
    }

    /**
     * Upon being resumed we can retrieve the current state. This allows us to
     * update the state if it was changed at any time while paused.
     */
    override fun onResume() {
        super.onResume()

        val prefs = getPreferences(0)
        val restoredText = prefs.getString("text", null)
        if (restoredText != null) {
            //		mSavedNote.setText(restoredText, TextView.BufferType.EDITABLE);

            val selectionStart = prefs.getInt("selection-start", -1)
            val selectionEnd = prefs.getInt("selection-end", -1)
            if (selectionStart != -1 && selectionEnd != -1) {
                //		mSavedNote.setSelection(selectionStart, selectionEnd);
            }
        }
    }

    /**
     * Any time we are paused we need to save away the current state, so it will
     * be restored correctly when we are resumed.
     */
    override fun onPause() {
        super.onPause()

        val editor = getPreferences(0).edit()
        //	editor.putString("text", mSavedNote.getText().toString());
        //	editor.putInt("selection-start", mSavedNote.getSelectionStart());
        //	editor.putInt("selection-end", mSavedNote.getSelectionEnd());
        editor.commit()
    }

    override fun dispatchTouchEvent(me: MotionEvent): Boolean {
        return super.dispatchTouchEvent(me)
    }

    fun onSwipe(direction: Int) {
        launchMicrophoneInputView()
    }

    fun onDoubleTap() {
        launchMicrophoneInputView()
    }

    fun onLongTap() {
        launchMicrophoneInputView()
    }

    fun launchMicrophoneInputView() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak into your microphone...")
        try {
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE)

        } catch (a: ActivityNotFoundException) {
            val t = Toast.makeText(applicationContext,
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT)
            t.show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val resultdata = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            mUserChoices!!.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resultdata)


            //	this.mSavedNote.setText(text.get(0));

            //	((EditText) findViewById(R.id.note_text)).setText(resultstring.toString());
        }
    }

    companion object {
        protected val RESULT_SPEECH = 1
        private val VOICE_RECOGNITION_REQUEST_CODE = 1234
    }
}
