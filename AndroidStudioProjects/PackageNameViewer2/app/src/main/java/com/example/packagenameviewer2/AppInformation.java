package com.example.packagenameviewer2;

import android.graphics.drawable.Drawable;

public class AppInformation {
    public String PackageName;
    private String ActivityName;
    private CharSequence AppName;
    private Drawable Icon;
    private boolean isSystemAppFlag;
    public String getPackageName(){
        return PackageName;
    }
    public void setPackageName(String packageName){
        this.PackageName=packageName;
    }
    public String getActivityName(){
        return ActivityName;
    }
    public void setActivityName(String ActivityName){
        this.ActivityName=ActivityName;
    }
    public CharSequence getAppName( ){
        return AppName;
    }
    public void setAppName(CharSequence AppName){
        this.AppName=AppName;
    }
    public Drawable getIcon() {
        return Icon;
    }
    public void setIcon(Drawable Icon) {
        this.Icon = Icon;
    }
    public boolean getisSystemAppFlag() {
        return isSystemAppFlag;
    }
    public void setisSystemAppFlag(boolean isSystemAppFlag) {
        this.isSystemAppFlag = isSystemAppFlag;
    }
}
