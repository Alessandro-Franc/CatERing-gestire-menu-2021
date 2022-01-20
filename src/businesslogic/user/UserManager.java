package businesslogic.user;

public class UserManager {
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void login(String userName){
        this.currentUser = User.loadUser(userName);
    }

    public void login(int userName){
        this.currentUser = User.loadUserById(userName);
    }

}
