package com.example.oopproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> mTasks;
    private Context mContext;
    private OnTaskClickListener mListener;
    private OnTaskLongClickListener mLongListener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }
    public interface OnTaskLongClickListener {
        void onTaskLongClick(Task task);
    }

    public TaskAdapter(Context context, List<Task> tasks, OnTaskClickListener listener, OnTaskLongClickListener longClickListener) {
        mTasks = tasks;
        mContext = context;
        mListener = listener;
        mLongListener = longClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTitle;
        public TextView taskDescription; // Added a TextView for the description
        public Button completeButton;

        public ViewHolder(View itemView) {
            super(itemView);

            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription); // Initialize the description TextView
            completeButton = itemView.findViewById(R.id.completeButton);

            completeButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task task = mTasks.get(position);
                    if (task != null && !task.isCompleted()) {
                        TaskManager.getInstance(mContext).completeTask(task);
                        task.setCompleted(true); // Mark the task as completed
                        notifyItemChanged(position); // Notify only the changed item
                    }
                }
            });
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && mLongListener != null) {
                    Task task = mTasks.get(position);
                    mLongListener.onTaskLongClick(task);
                }
                return true; // indicates the callback consumed the long click
            });

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task task = mTasks.get(position);
                    mListener.onTaskClick(task);
                }
            });
        }
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View taskView = inflater.inflate(R.layout.task_item, parent, false);
        return new ViewHolder(taskView);
    }

    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder viewHolder, int position) {
        Task task = mTasks.get(position);

        viewHolder.taskTitle.setText(task.getTitle());
        // Set the description text to the first line of the description or the entire description if it does not contain new lines.
        String description = task.getDescription();
        viewHolder.taskDescription.setText(description.contains("\n") ? description.substring(0, description.indexOf("\n")) : description);

        viewHolder.completeButton.setVisibility(task.isCompleted() ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public void updateTasks(List<Task> newTasks) {
        mTasks.clear();
        mTasks.addAll(newTasks);
        notifyDataSetChanged();
    }

    public List<Task> getCurrentList() {
        return new ArrayList<>(mTasks); // Return a copy of the current list to avoid modification issues
    }
}
