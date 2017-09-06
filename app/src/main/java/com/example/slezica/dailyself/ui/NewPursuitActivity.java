package com.example.slezica.dailyself.ui;

import android.os.Bundle;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.slezica.dailyself.R;
import com.example.slezica.dailyself.model.Pursuit;
import io.reactivex.Single;

public class NewPursuitActivity extends BaseActivity {

    @BindView(R.id.name)
    EditText name;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_new_pursuit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("New pursuit");
        name.requestFocus();
    }

    @OnClick(R.id.submit)
    protected void onSubmit() {
        Pursuit p = new Pursuit();

        p.setName(name.getText().toString());

        final Single<Pursuit> op = dataStore
                .insert(p)
                .doOnSuccess(i -> finish());

        subscribeTo(op);
    }
}
