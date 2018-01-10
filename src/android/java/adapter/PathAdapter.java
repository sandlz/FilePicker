package com.handsmap.filepicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handsmap.filepicker.FakeR;
import com.handsmap.filepicker.utils.Constant;
import com.handsmap.filepicker.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * 作者：Leon
 * 时间：2017/3/15 15:47
 */
public class PathAdapter extends RecyclerView.Adapter<PathAdapter.PathViewHolder> {
    public interface OnItemClickListener {
        void click(int position, boolean isCheckBox);
    }

    public interface OnCancelChoosedListener {
        void cancelChoosed(CheckBox checkBox);
    }
    private FakeR fakeR;
    private final String TAG = "FilePickerLeon";
    private List<File> mListData;
    private Context mContext;
    public OnItemClickListener onItemClickListener;
    private FileFilter mFileFilter;
    private boolean[] mCheckedFlags;
    private boolean mMutilyMode;
    private int mIconStyle;
    private int chooseMode;
    // 最大选择数量
    private int maxChooseNum = 0;

    public PathAdapter(List<File> mListData, Context mContext, FileFilter mFileFilter,
                       boolean mMutilyMode, int maxNum, int chooseMode, FakeR fakeR) {
        this.mListData = mListData;
        this.mContext = mContext;
        this.mFileFilter = mFileFilter;
        this.mMutilyMode = mMutilyMode;
        this.maxChooseNum = maxNum;
        this.chooseMode = chooseMode;
        mCheckedFlags = new boolean[mListData.size()];
        this.fakeR = fakeR;
    }

    @Override
    public PathViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(mContext, fakeR.getId("layout", "listitem"), null);
        PathViewHolder pathViewHolder = new PathViewHolder(view);
        return pathViewHolder;
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    @Override
    public void onBindViewHolder(final PathViewHolder holder, final int position) {
        final File file = mListData.get(position);
        if (file.isFile()) {
            // 文件
            updateFileIconStyle(holder.ivType);
            holder.tvName.setText(file.getName());
            holder.tvDetail.setText("文件大小 : " + " " + FileUtils.getReadableFileSize(file.length()));
            if (chooseMode == 0) {
                holder.cbChoose.setVisibility(View.VISIBLE);
            } else if (chooseMode == 1) {
                holder.cbChoose.setVisibility(View.GONE);
            } else {
                holder.cbChoose.setVisibility(View.VISIBLE);
            }
        } else {
            // 文件夹
            updateFloaderIconStyle(holder.ivType);
            holder.tvName.setText(file.getName());
            File[] files = file.listFiles(mFileFilter);
            if (files == null) {
                holder.tvDetail.setText("0 " + "项");
            } else {
                holder.tvDetail.setText(files.length + " " + "项");
            }
            if (chooseMode == 0) {
                holder.cbChoose.setVisibility(View.GONE);
            } else if (chooseMode == 1) {
                holder.cbChoose.setVisibility(View.VISIBLE);
            } else {
                holder.cbChoose.setVisibility(View.VISIBLE);
            }
        }
        if (!mMutilyMode) {
            holder.cbChoose.setVisibility(View.GONE);
        }
        holder.layoutRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file.isFile()) {
                    holder.cbChoose.setChecked(!holder.cbChoose.isChecked());
                }
                onItemClickListener.click(position, false);
            }
        });
        holder.cbChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //同步复选框和外部布局点击的处理
                onItemClickListener.click(position, true);
            }
        });
        holder.cbChoose.setOnCheckedChangeListener(null);//先设置一次CheckBox的选中监听器，传入参数null
        holder.cbChoose.setChecked(mCheckedFlags[position]);//用数组中的值设置CheckBox的选中状态
        //再设置一次CheckBox的选中监听器，当CheckBox的选中状态发生改变时，把改变后的状态储存在数组中
        holder.cbChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCheckedFlags[position] = b;
            }
        });
    }

    private void updateFloaderIconStyle(ImageView imageView) {
        switch (mIconStyle) {
            case Constant.ICON_STYLE_BLUE:
                imageView.setBackgroundResource(fakeR.getId("mipmap","folder_style_blue"));
                break;
            case Constant.ICON_STYLE_GREEN:
                imageView.setBackgroundResource(fakeR.getId("mipmap","folder_style_green"));
                break;
            case Constant.ICON_STYLE_YELLOW:
                imageView.setBackgroundResource(fakeR.getId("mipmap","folder_style_yellow"));
                break;
            case Constant.ICON_STYLE_YELLOW2:
                imageView.setBackgroundResource(fakeR.getId("mipmap","folder_style_yellow2"));
                break;
        }
    }

    private void updateFileIconStyle(ImageView imageView) {
        switch (mIconStyle) {
            case Constant.ICON_STYLE_BLUE:
                imageView.setBackgroundResource(fakeR.getId("mipmap","file_style_blue"));
                break;
            case Constant.ICON_STYLE_GREEN:
                imageView.setBackgroundResource(fakeR.getId("mipmap","file_style_green"));
                break;
            case Constant.ICON_STYLE_YELLOW:
                imageView.setBackgroundResource(fakeR.getId("mipmap","file_style_yellow"));
                break;
            case Constant.ICON_STYLE_YELLOW2:
                imageView.setBackgroundResource(fakeR.getId("mipmap","file_style_yellow"));
                break;
        }
    }

    public int getChoosedCount() {
        int count = 0;
        for (int i = 0; i < mCheckedFlags.length; i++) {
            boolean flag = mCheckedFlags[i];
            if (flag) {
                count++;
            }
        }
        return count;
    }

    /**
     * 设置监听器
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置数据源
     *
     * @param mListData
     */
    public void setmListData(List<File> mListData) {
        this.mListData = mListData;
        mCheckedFlags = new boolean[mListData.size()];
    }

    public void setmIconStyle(int mIconStyle) {
        this.mIconStyle = mIconStyle;
    }

    /**
     * 设置是否全选
     *
     * @param isAllSelected
     */
    public void updateAllSelelcted(boolean isAllSelected) {

        for (int i = 0; i < mCheckedFlags.length; i++) {
            mCheckedFlags[i] = isAllSelected;
        }
        notifyDataSetChanged();
    }

    class PathViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layoutRoot;
        private ImageView ivType;
        private TextView tvName;
        private TextView tvDetail;
        private CheckBox cbChoose;

        public PathViewHolder(View itemView) {
            super(itemView);
            ivType = (ImageView) itemView.findViewById(fakeR.getId("id", "iv_type"));
            layoutRoot = (RelativeLayout) itemView.findViewById(fakeR.getId("id", "layout_item_root"));
            tvName = (TextView) itemView.findViewById(fakeR.getId("id", "tv_name"));
            tvDetail = (TextView) itemView.findViewById(fakeR.getId("id", "tv_detail"));
            cbChoose = (CheckBox) itemView.findViewById(fakeR.getId("id", "cb_choose"));
        }
    }
}

