package customactivitytransition;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.yun.activie.anim.lib.R;

public class TransitionMainActivity extends AppCompatActivity {

    View mCommentImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        mCommentImg = findViewById(R.id.comment_img);

        mCommentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransitionMainActivity.this, CommentActivity.class);
                transitionTo(intent);
            }
        });
    }


    void transitionTo(Intent i) {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, new Pair<View, String>(mCommentImg, "comment"));
        startActivity(i, transitionActivityOptions.toBundle());
    }
}
