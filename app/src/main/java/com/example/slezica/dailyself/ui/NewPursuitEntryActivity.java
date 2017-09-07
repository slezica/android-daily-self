package com.example.slezica.dailyself.ui;

import android.os.Bundle;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.slezica.dailyself.R;
import com.example.slezica.dailyself.model.Pursuit;
import com.example.slezica.dailyself.model.PursuitEntry;
import io.reactivex.Observable;
import org.threeten.bp.ZonedDateTime;

public class NewPursuitEntryActivity extends BaseActivity {

    public static final String PURSUIT_ID = "pursuitId";

    @BindView(R.id.score)
    EditText score;

    @BindView(R.id.comment)
    EditText comment;

    private Pursuit pursuit;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_new_pursuit_entry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int pursuitId = getIntent().getIntExtra(PURSUIT_ID, 0);
        loadPursuit(pursuitId);

        score.requestFocus();
    }

    protected void loadPursuit(int pursuitId) {
        final Observable<Pursuit> observable = dataStore.select(Pursuit.class)
                .where(Pursuit.ID.eq(pursuitId))
                .get()
                .observable();

        subscribeTo(observable, this::onPursuitLoaded);
    }

    protected void onPursuitLoaded(Pursuit pursuit) {
        this.pursuit = pursuit;
        setTitle(pursuit.getName());
    }

    @OnClick(R.id.submit)
    protected void onSubmit() {
        PursuitEntry entry = new PursuitEntry();

        entry.setPursuit(pursuit);
        entry.setDatetime(ZonedDateTime.now());
        entry.setScore(Integer.parseInt(score.getText().toString()));
        entry.setComment(comment.getText().toString());

        subscribeTo(dataStore.insert(entry), this::finish);
    }
}
