package util;

public class Menu {
    //print the options of the menu
    public static void print(String name, String[] selections) {
        System.out.println(name);
        System.out.println();
        for (int i = 0; i < selections.length; i++) {
            System.out.println("" + (i + 1) + "." + selections[i]);
        }
        System.out.println("Enter other key to exit");
        System.out.println();
    }
    
    //clear screen
    public static void clear() {
        for (int i = 0 ; i < 25 ; i++) {
            System.out.println();
        }
    }
}
