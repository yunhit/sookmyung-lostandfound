package pack.mp_team5project;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<CommentModel> commentList;
    private Context context;

    public CommentAdapter(Context context, List<CommentModel> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentModel comment = commentList.get(position);

        holder.textCmtContent.setText(comment.getCommentContent());
        holder.textCmtAuthor.setText(comment.getAuthor());
        holder.textCmtTimestamp.setText(comment.getTimeStamp());

        Log.d("CommentAdapter", "Comment Content: " + commentList.get(position).getCommentContent());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView textCmtContent, textCmtAuthor, textCmtTimestamp;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            textCmtContent = itemView.findViewById(R.id.textCommentContent);
            textCmtAuthor = itemView.findViewById(R.id.textCommentAuthor);
            textCmtTimestamp = itemView.findViewById(R.id.textCommentTimestamp);
        }
    }
}


