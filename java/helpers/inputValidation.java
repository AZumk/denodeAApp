package helpers;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.required;
import static app.denode.denode.R.id.email_cadastro;

/**
 * Created by Ana Zumk on 20/10/2017.
 */

public  class inputValidation{
private Context context;

    public inputValidation(Context context){
        this.context = context;
    }
    private static final String textRegex = "[A-Za-z0-9 ]";
    private static final String emailRegex = "^([_a-zA-Z0-9-]+(\\\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\\\.[a-zA-Z0-9-]+)*(\\\\.[a-zA-Z]{1,6}))?$";

    public static boolean notEmpty(EditText inputText){
        String value = inputText.getText().toString().trim();
        return !value.isEmpty();
    }

    public static boolean isEmail(EditText editText, boolean required) {
        String value = editText.getText().toString().trim();
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public boolean inputMatch(EditText input1, EditText input2){
        String value1 = input1.getText().toString().trim();
        String value2 = input2.getText().toString().trim();
        return value1.contentEquals(value2);
    }

    public static boolean isValidText(EditText editText, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        // text required and editText is blank, so return false
        if (required && !notEmpty(editText)) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(textRegex, text)) {

            return false;
        } else {
            return true;
        }
    }

    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }


    public static boolean isRadioChecked(RadioGroup radioGroup){
        if(radioGroup.getCheckedRadioButtonId() != -1){
            return true;
        } else{
            return false;
        }
    }
}
