package jeffersondev;

public class mobObj {
        private String name;
    
        public mobObj(String name) {
            this.name = name; // Use "this" to access instance variables/methods from within the class
        }
    
        public static void test() {
            new mobObj("bob");
        }
    }