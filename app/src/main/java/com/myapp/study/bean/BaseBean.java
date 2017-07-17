package com.myapp.study.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lvgy on 2017/7/16.
 */

public class BaseBean implements Parcelable {

    public static final Parcelable.Creator<BaseBean> CREATOR = new Parcelable.Creator<BaseBean>() {
        @Override
        public BaseBean createFromParcel(Parcel source) {
            return new BaseBean(source);
        }

        @Override
        public BaseBean[] newArray(int size) {
            return new BaseBean[size];
        }
    };
    /**
     * id : 3
     * content : Hello, World!
     */

    private int id;
    private String content;

    public BaseBean() {
    }

    protected BaseBean(Parcel in) {
        this.id = in.readInt();
        this.content = in.readString();
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.content);
    }
}
