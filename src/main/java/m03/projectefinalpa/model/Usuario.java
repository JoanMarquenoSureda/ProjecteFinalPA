/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model;

/**
 *
 * @author User
 */
public class Usuario {
     public  String user = "";
    public  String passwd = "";

    public Usuario(String user, String passwd) {
        this.user = user;
        this.passwd = passwd;
    }

    public Usuario() {
    }

    public String getUser() {
        return user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    
    
    

}
