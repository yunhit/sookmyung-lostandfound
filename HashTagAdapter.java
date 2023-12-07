package pack.mp_team5project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HashTagAdapter extends RecyclerView.Adapter<HashTagAdapter.ViewHolder> {

    private final Context context;
    private final List<String> hashtagList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference HashRef = mRootRef.child("HashTag_Data");
    DatabaseReference HashDataRef = HashRef.child("HashTag_" + mAuth.getCurrentUser().getUid());

    public HashTagAdapter(Context context, List<String> hashtagList) {
        this.context = context;
        this.hashtagList = hashtagList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hashtag_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String hashtag = hashtagList.get(position);
        holder.hashTag_text.setText("#" + hashtag);

        //해시태그별 delete_btn클릭시 리사이클 뷰에서 제거
        //firebase 데이터베이스에서도 제거
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    hashtagList.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    updateFirebaseData(hashtagList);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return hashtagList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView hashTag_text;
        public final ImageButton delete_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            hashTag_text = itemView.findViewById(R.id.hashTag_text);
            delete_btn = itemView.findViewById(R.id.delete_btn);
        }
    }
    private void updateFirebaseData(List<String> updatedList) {
        HashDataRef.setValue(updatedList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ((AlarmActivity) context).updatePostRecyclerView(updatedList);
                } else {
                    // 데이터베이스 업데이트 실패
                    Toast.makeText(context, "데이터를 업데이트하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
