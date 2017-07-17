package com.myapp.study.etc;

public class HttpConfigModel {
    public int id;
    public int method;
    public String action;
    public boolean hasHeader;
    public boolean needCache;
    public int timeout;
    public boolean dispatchUI;
    public boolean needLogin;
    public boolean isForm = true;

    public boolean showToast;

    public boolean isHttps;


    @Override
    public String toString() {
        return "HttpConfigModel{" +
                "id=" + id +
                ", method=" + method +
                ", action='" + action + '\'' +
                ", hasHeader=" + hasHeader +
                ", needCache=" + needCache +
                ", timeout=" + timeout +
                ", dispatchUI=" + dispatchUI +
                ", needLogin=" + needLogin +
                ", isForm=" + isForm +
                ", showToast=" + showToast +
                ", isHttps=" + isHttps +
                '}';
    }
}
