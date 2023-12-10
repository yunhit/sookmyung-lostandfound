package pack.mp_team5project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.bumptech.glide.Glide;
import java.util.List;
import java.util.function.Consumer;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PostModel post);
        void onDeleteButtonClick(Integer position);
    }

    private OnItemClickListener listener;
    private List<PostModel> postList;
    private Context context;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PostAdapter(Context context, List<PostModel> postList) {
        this.context = context;
        this.postList = postList;
    }

    private boolean isSearchScreen = false;

    public PostAdapter(Context context, List<PostModel> postList, boolean isSearchScreen) {
        this.context = context;
        this.postList = postList;
        this.isSearchScreen = isSearchScreen;
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view, postList, listener);
    }

    // PostAdapter의 onBindViewHolder() 메서드에서 이미지를 Firebase Storage에서 로드하여 표시하는 부분 수정
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel post = postList.get(position);

        holder.textTitle.setText(post.getTitle());
        holder.textDate.setText(post.getDate());
        holder.textDescription.setText(post.getDescription());

        if (post.isDeleteButtonVisible()) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
        } else {
            holder.deleteBtn.setVisibility(View.GONE);
        }


        // Firebase Storage에서 이미지 로드하여 표시
        Glide.with(context).clear(holder.imageView);
        if (post.getImageUrl() != null) {
            Glide.with(context)
                    .load(post.getImageUrl())
                    .into(holder.imageView);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(postList.get(position));
                    }
                }
            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textTitle, textDate, textDescription;
        Button deleteBtn;
        List<PostModel> postList;
        DatabaseReference databaseReference; // Firebase Database 참조

        public PostViewHolder(@NonNull View itemView, List<PostModel> postList, OnItemClickListener listener) {
            super(itemView);
            this.postList = postList;
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Data"); // 데이터베이스 참조 설정

            imageView = itemView.findViewById(R.id.imageView);
            textTitle = itemView.findViewById(R.id.text_title);
            textDate = itemView.findViewById(R.id.text_date);
            textDescription = itemView.findViewById(R.id.text_description);
            deleteBtn = itemView.findViewById(R.id.deleteButton);


            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // RecyclerView에서 아이템 제거
                        listener.onDeleteButtonClick(position);
                    }
                }
            });
        }
    }
}
