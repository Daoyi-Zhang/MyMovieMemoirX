package com.monash.mymoviememoirx.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.activity.LoginSignUpActivity;
import com.monash.mymoviememoirx.network.task.FindCredentialByUsername;
import com.monash.mymoviememoirx.network.task.POSTCredential;
import com.monash.mymoviememoirx.pojo.Credential;
import com.monash.mymoviememoirx.pojo.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    public SignUpFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initDatePicker(view);
        initSpinner(view);
        initRegister(view);
        return view;
    }

    private void initDatePicker(View view) {
        Button dobTextView = view.findViewById(R.id.dob_button);
        dobTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getActivity().getSupportFragmentManager(), "date picker");
            }
        });
    }

    private void initSpinner(View view) {
        Spinner stateSpinner = view.findViewById(R.id.sign_up_state_spinner);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initRegister(final View view) {
        Button register = view.findViewById(R.id.register_button);
        final EditText email = view.findViewById(R.id.sign_up_email_edit_text);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doValidation(view)) {
                    //2. check email exists
                    new FindCredentialByUsername() {
                        @Override
                        protected void onPostExecute(Credential credential) {
                            if (credential != null) {
                                Toast.makeText(getActivity(), "ERROR: this email is already registered!", Toast.LENGTH_LONG).show();
                            }
                            else {
                                // post
                                new POSTCredential(){
                                    @Override
                                    protected void onPostExecute(String s) {
                                        if(s != null){
                                            LoginSignUpActivity ac = (LoginSignUpActivity) getActivity();
                                            Toast.makeText(ac, "Register Succeed!", Toast.LENGTH_LONG).show();
                                            ac.replaceFragment(LoginFragment.newInstance());
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "ERROR: Network issue, please try again!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }.execute(prepareObject());
                            }
                        }
                        private Credential prepareObject(){
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                            EditText nameEditText = view.findViewById(R.id.sign_up_first_name_edit_text);
                            EditText surnameEditText = view.findViewById(R.id.sign_up_surname_edit_text);
                            RadioButton maleButton = view.findViewById(R.id.sign_up_radio_group_male_button);
                            EditText addressEditText = view.findViewById(R.id.sign_up_address_edit_text);
                            EditText postcodeEditText = view.findViewById(R.id.sign_up_post_code);
                            Button dobButton = view.findViewById(R.id.dob_button);
                            EditText passwordEditText = view.findViewById(R.id.sign_up_password_edit_text);
                            EditText email = view.findViewById(R.id.sign_up_email_edit_text);
                            Spinner stateSpinner = view.findViewById(R.id.sign_up_state_spinner);
                            User newUser = null;
                            try {
                                newUser = new User(nameEditText.getText().toString(),
                                        surnameEditText.getText().toString(),
                                        maleButton.isChecked()?"M":"F",
                                        sdf1.format(sdf2.parse(dobButton.getText().toString())).replace("GMT", ""),
                                        addressEditText.getText().toString(),
                                        stateSpinner.getSelectedItem().toString(),
                                        postcodeEditText.getText().toString(),
                                        -1);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            LoginSignUpActivity ac = (LoginSignUpActivity) getActivity();

                            sdf1.setTimeZone(TimeZone.getDefault());
                            Date now = new Date();
                            String signUpDate = sdf1.format(now).replace("GMT", "");
                            Credential newCredential = new Credential(email.getText().toString(),
                                    ac.encrypt(passwordEditText.getText().toString()),
                                    signUpDate,
                                    newUser,
                                    -1);
                            return newCredential;
                        }
                    }.execute(email.getText().toString().trim());
                }
            }
        });
    }


    private boolean doValidation(View view) {
        EditText nameEditText = view.findViewById(R.id.sign_up_first_name_edit_text);
        EditText surnameEditText = view.findViewById(R.id.sign_up_surname_edit_text);
        RadioButton maleButton = view.findViewById(R.id.sign_up_radio_group_male_button);
        RadioButton femaleButton = view.findViewById(R.id.sign_up_radio_group_female_button);
        EditText addressEditText = view.findViewById(R.id.sign_up_address_edit_text);
        EditText postcodeEditText = view.findViewById(R.id.sign_up_post_code);
        Button dobButton = view.findViewById(R.id.dob_button);
        EditText passwordEditText = view.findViewById(R.id.sign_up_password_edit_text);
        EditText confirmPasswordEditText = view.findViewById(R.id.sign_up_password_confirm_edit_text);
        EditText email = view.findViewById(R.id.sign_up_email_edit_text);

        Boolean isAllNotBlank = true;
        if (nameEditText.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "ERROR: name is empty!", Toast.LENGTH_LONG).show();
            isAllNotBlank = false;
        } else if (surnameEditText.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "ERROR: surname is empty!", Toast.LENGTH_LONG).show();
            isAllNotBlank = false;
        } else if (!maleButton.isChecked() && !femaleButton.isChecked()) {
            Toast.makeText(getActivity(), "ERROR: please choose a gender!", Toast.LENGTH_LONG).show();
            isAllNotBlank = false;
        } else if (addressEditText.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "ERROR: address is empty!", Toast.LENGTH_LONG).show();
            isAllNotBlank = false;
        } else if (postcodeEditText.getText().toString().isEmpty() || postcodeEditText.getText().toString().length() != 4) {
            Toast.makeText(getActivity(), "ERROR: incorrect postcode!", Toast.LENGTH_LONG).show();
            isAllNotBlank = false;
        } else if (dobButton.getText().toString().equals("Choose a date")) {
            Toast.makeText(getActivity(), "ERROR: please choose your birthday!", Toast.LENGTH_LONG).show();
            isAllNotBlank = false;
        } else if (passwordEditText.getText().toString().isEmpty() || confirmPasswordEditText.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "ERROR: password is empty!", Toast.LENGTH_LONG).show();
            isAllNotBlank = false;
        } else if (!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
            Toast.makeText(getActivity(), "ERROR: confirm your password again!", Toast.LENGTH_LONG).show();
            isAllNotBlank = false;
        } else if (email.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "ERROR: address is empty!", Toast.LENGTH_LONG).show();
            isAllNotBlank = false;
        }
        return isAllNotBlank;
    }
}
