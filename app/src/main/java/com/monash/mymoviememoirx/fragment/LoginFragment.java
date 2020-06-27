package com.monash.mymoviememoirx.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.activity.LoginSignUpActivity;
import com.monash.mymoviememoirx.network.task.FindCredentialByUsername;
import com.monash.mymoviememoirx.pojo.Credential;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initLoginButton(view);
        initSignUpButton(view);
        return view;
    }

    private void initSignUpButton(View view){
        Button signUpButton = view.findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSignUpActivity ac = (LoginSignUpActivity) getActivity();
                ac.replaceFragment(SignUpFragment.newInstance());
            }
        });
    }

    private void initLoginButton(final View view){
        Button loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                EditText usernameEditText = view.findViewById(R.id.username_edit_text);
                EditText passwordEditText = view.findViewById(R.id.password_edit_text);
                String username = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                if (!username.isEmpty() && !password.isEmpty()) {
                    new FindCredentialByUsername(){
                        @Override
                        protected void onPostExecute(Credential credential) {
                            if (credential != null){
                                LoginSignUpActivity ac = (LoginSignUpActivity) getActivity();
                                if (credential.getPassword().equals(ac.encrypt(password))){
                                    Intent intent = ac.getIntent();
                                    //save the information of current login user
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("currentCredential", credential);
                                    bundle.putParcelable("currentUser", credential.getUserId());
                                    intent.putExtras(bundle);
                                    ac.setResult(ac.RESULT_OK, intent);
                                    ac.finish();
                                }
                                else {
                                    Toast.makeText(getActivity(), "ERROR: username or password incorrect!", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(getActivity(), "ERROR: username or password incorrect!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }.execute(username);
                } else {
                    Toast.makeText(getActivity(), "ERROR: username or password is empty!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
