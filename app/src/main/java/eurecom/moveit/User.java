package eurecom.moveit;

/**
 * Created by mrabet on 12/9/2017.
 */

public class User {
    private String UserName;
    private String Pwd;
    private String Phone;
    private String Mail;

    public String getUserName() {return UserName;}
    public String getPwd() {return Pwd;}
    public String getPhone() {return Phone;}
    public String getMail() {return Mail;}
    public void setUserName(String UserName) {this.UserName = UserName;}
    public void setPwd(String Pwd) {this.Pwd = Pwd;}
    public void setPhone(String Phone) {this.Phone = Phone;}
    public void setMail(String Mail) {this.Mail = Mail;}

}
