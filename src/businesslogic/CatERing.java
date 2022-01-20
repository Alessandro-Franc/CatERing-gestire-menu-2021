package businesslogic;

import businesslogic.FRManagment.FRManager;
import businesslogic.event.EventManager;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuManager;
import businesslogic.recipe.RecipeManager;
import businesslogic.user.UserManager;
import persistence.FRPersistence;
import persistence.MenuPersistence;
import persistence.PersistenceManager;

public class CatERing {
    private static CatERing singleInstance;

    public static CatERing getInstance() {
        if (singleInstance == null) {
            singleInstance = new CatERing();
        }
        return singleInstance;
    }

    private MenuManager menuMgr;
    private RecipeManager recipeMgr;
    private UserManager userMgr;
    private EventManager eventMgr;
    private FRManager frManager;

    private MenuPersistence menuPersistence;
    private FRPersistence frPersistence;

    private CatERing() {
        menuMgr = new MenuManager();
        recipeMgr = new RecipeManager();
        userMgr = new UserManager();
        eventMgr = new EventManager();
        frManager = new FRManager();

        menuPersistence = new MenuPersistence();
        frPersistence = new FRPersistence();
        menuMgr.addEventReceiver(menuPersistence);
        frManager.addEventReceiver(frPersistence);
    }


    public MenuManager getMenuManager() {
        return menuMgr;
    }

    public RecipeManager getRecipeManager() {
        return recipeMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }

    public EventManager getEventManager() { return eventMgr; }

    public FRManager getFrManager() {
        return frManager;
    }
}
