package com.monash.mymoviememoirx.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.activity.MainActivity;
import com.monash.mymoviememoirx.network.task.AssignBitmapForMovies;
import com.monash.mymoviememoirx.network.task.FindAllCinemas;
import com.monash.mymoviememoirx.network.task.POSTCinema;
import com.monash.mymoviememoirx.network.task.POSTMemoir;
import com.monash.mymoviememoirx.pojo.Cinema;
import com.monash.mymoviememoirx.pojo.DetailMovie;
import com.monash.mymoviememoirx.pojo.Memoir;
import com.monash.mymoviememoirx.pojo.User;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMemoirFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMemoirFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private DetailMovie movie;
    private User user;

    public AddMemoirFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AddToMemoirFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMemoirFragment newInstance(DetailMovie param1, User param2) {
        AddMemoirFragment fragment = new AddMemoirFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable(ARG_PARAM1);
            user = getArguments().getParcelable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_memoir, container, false);
        initDatePicker(view);
        initSpinners(view);
        initShowText(view);
        initImage(view);
        initCreateButton(view);
        initSubmitButton(view);
        return view;
    }

    public void initSubmitButton(final View view) {
        Button submit = view.findViewById(R.id.memoir_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO validation
                EditText commentET = view.findViewById(R.id.memoir_comment);
                String comment = commentET.getText().toString();
                Button button = view.findViewById(R.id.memoir_date);
                String watchDate = button.getText().toString();
                // 1. comment not empty
                if (comment.isEmpty()) {
                    Toast.makeText(getContext(), "ERROR: Comment is empty!", Toast.LENGTH_LONG).show();
                }else if(watchDate.equals("Choose a date")) {
                    Toast.makeText(getContext(), "ERROR: Date is empty!", Toast.LENGTH_LONG).show();
                }
                else {
                    Spinner spinner = view.findViewById(R.id.memoir_cinema_name);
                    RatingBar ratingBar = view.findViewById(R.id.memoir_scores);
                    double score = ratingBar.getRating();
                    Cinema selectedCinema = (Cinema) spinner.getSelectedItem();
                    String movieName = movie.getMovieName();
                    Date releaseDate = null;
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(movie.getReleaseDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String strReleaseDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz").format(releaseDate).replace("GMT", "");
                    Date watchTime = null;
                    try {
                        watchTime = new SimpleDateFormat("yyyy-MM-dd").parse(watchDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String strWatchTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz").format(watchTime).replace("GMT", "");
                    double ratingScore = score;
                    Integer memoirId = -1;
                    Memoir newMemoir = new Memoir(movieName, strReleaseDate, strWatchTime, comment, ratingScore, memoirId, selectedCinema, user);
                    //TODO post
                    new POSTMemoir(){
                        @Override
                        protected void onPostExecute(String s) {
                            if (s != null){
                                Toast.makeText(getContext(), "Succeed", Toast.LENGTH_LONG).show();
                                getActivity().finish();
                            }
                            else {
                                Toast.makeText(getActivity(), "ERROR: Network issue, please try again!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }.execute(newMemoir);
                }
            }
        });
    }

    public void initCreateButton(final View view) {
        Button create = view.findViewById(R.id.memoir_is_create);
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.cinema_create);
        final EditText cinemaName = dialog.findViewById(R.id.create_name);
        final EditText cinemaPostcode = dialog.findViewById(R.id.create_postcode);
        Button confirm = dialog.findViewById(R.id.create_confirm);
        Button cancel = dialog.findViewById(R.id.create_cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = view.findViewById(R.id.memoir_cinema_name);
                String name = cinemaName.getText().toString().trim();
                String postcode = cinemaPostcode.getText().toString().trim();
                int num = spinner.getAdapter().getCount();
                for (int i = 0; i < num; i++) {
                    if (spinner.getAdapter().getItem(i).equals(name + " " + postcode)) {
                        Toast.makeText(getContext(), "ERROR: This cinema already exists", Toast.LENGTH_LONG).show();
                        break;
                    }else if(name.isEmpty() || postcode.isEmpty()){
                        Toast.makeText(getContext(), "ERROR: Cinema name or postcode is empty", Toast.LENGTH_LONG).show();
                        break;
                    }
                    else {
                        //TODO post
                        Cinema newCinema = new Cinema(-1, name, postcode);
                        new POSTCinema(){
                            @Override
                            protected void onPostExecute(String s) {
                                if (s != null){
                                    initSpinners(view);
                                    Toast.makeText(getActivity(), "Add Cinema Succeed!", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(getActivity(), "ERROR: Network issue, please try again!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }.execute(newCinema);
                        break;
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    public void initImage(final View view) {
        List<DetailMovie> dm = new ArrayList<>();
        dm.add(movie);
        new AssignBitmapForMovies(){
            @Override
            protected void onPostExecute(List<String> strings) {
                ImageView movieImage = view.findViewById(R.id.memoir_image);
                if (movie.getBitmap() != null){
                    movieImage.setImageBitmap(movie.getBitmap());
                }
                else {
                    Toast.makeText(getActivity(), "ERROR: Network issue, please try again!", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(dm);
    }

    public void initShowText(View view) {
        TextView nameTV = view.findViewById(R.id.memoir_movie_name);
        String showText = movie.getMovieName() + " " + movie.getReleaseDate();
        nameTV.setText(showText);
    }

    public void initSpinners(final View view) {
        new FindAllCinemas(){
            @Override
            protected void onPostExecute(List<Cinema> cinemas) {
                if (cinemas != null){
                    Spinner spinner = view.findViewById(R.id.memoir_cinema_name);
                    ArrayAdapter<Cinema> spinnerAdapter =
                            new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cinemas);
                    spinner.setAdapter(spinnerAdapter);
                }
                else {
                    Toast.makeText(getActivity(), "ERROR: Network issue, please try again!", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    public void initDatePicker(View view) {
        Button dateTV = view.findViewById(R.id.memoir_date);
        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getActivity().getSupportFragmentManager(), "date picker");
            }
        });
    }
}
