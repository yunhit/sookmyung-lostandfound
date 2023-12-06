package pack.mp_team5project;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentLoader {
    public interface CommentLoadListener {
        void onCommentsLoaded(List<CommentModel> comments);
        void onNoComments();
        void onError(DatabaseError databaseError);


    }

    public static void loadCommentsForPost(String postKey, CommentLoadListener listener) {
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference().child("comments");
        commentsRef.orderByChild("targetPostKey").equalTo(postKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<CommentModel> comments = new ArrayList<>();

                        for(DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                            CommentModel comment = commentSnapshot.getValue(CommentModel.class);
                            if(comment != null){
                                comments.add(comment);
                            }
                        }
                        if(comments.isEmpty()) {
                            if(listener != null) {
                                listener.onNoComments();
                            }
                        } else {
                            if (listener != null) {
                                listener.onCommentsLoaded(comments);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        if(listener != null) {
                            listener.onError(databaseError);
                        }

                    }
                });

    }
}
